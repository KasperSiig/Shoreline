package shoreline.gui.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import shoreline.be.ConvTask;
import shoreline.bll.ThreadPool;
import shoreline.exceptions.GUIException;
import shoreline.gui.model.ModelManager;
import shoreline.statics.Window;

/**
 * FXML Controller class
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class TaskWindowController implements Initializable, IController {

    /* Java Variables */
    private List<TaskView> selectedPenTasks;
    private List<TaskView> selectedFinTasks;
    private List<TaskView> selectedCanTasks;

    private ContextMenu cMenu;

    private ModelManager model;

    /* JavaFX Variables */
    @FXML
    private VBox vBoxPen;
    @FXML
    private VBox vBoxFin;
    @FXML
    private VBox vBoxCan;
    @FXML
    private ScrollPane scrlPanePen;
    @FXML
    private ScrollPane scrlPaneFin;
    @FXML
    private ScrollPane scrlPaneCan;
    @FXML
    private TabPane tabPane;

    public TaskWindowController() {
        this.cMenu = new ContextMenu();
        this.selectedPenTasks = new ArrayList();
        this.selectedFinTasks = new ArrayList();
        this.selectedCanTasks = new ArrayList();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    /**
     * Starts all the selected tasks
     *
     * @param event
     */
    @FXML
    private void handleTaskPlay(ActionEvent event) {
        startTask(selectedPenTasks);
    }

    private void startTask(List<TaskView> selectedTasks) {
        List<TaskView> temp = new ArrayList(selectedTasks);
        temp.forEach((taskView) -> {
            model.getTaskModel().start(taskView.getTask());
            toggleSelected(taskView, selectedTasks, false);
            try {
                model.getLogModel().add(model.getUserModel().getUser().getId(), Alert.AlertType.INFORMATION, model.getUserModel().getUser().getfName() + " has started task " + taskView.getTask().getName());
            } catch (GUIException ex) {
                Window.openExceptionWindow("There was a problem with a log", ex.getStackTrace());
            }
        });
    }

    /**
     * Pauses all selected tasks
     *
     * @param event
     */
    @FXML
    private void handleTaskPause(ActionEvent event) {
        pauseOrCancelTask(selectedPenTasks, false);
    }

    /**
     * Stops or cancels the task, based on cancel parameter
     * 
     * @param tasks List of tasks to be paused or canceled
     * @param cancel true for cancel, false for pause
     * @return 
     */
    private boolean pauseOrCancelTask(List<TaskView> tasks, boolean cancel) {
        ThreadPool tp = ThreadPool.getInstance();
        if (tasks.size() > 1) {
            if (openConfirmWindow("Are you sure you want to stop " + tasks.size() + " tasks?", null, null, false)) {
                List<TaskView> temp = new ArrayList<>(tasks);
                temp.forEach((task) -> {
                    if (cancel) {
                        tp.cancelTask(task.getTask());
                    } else {
                        tp.pauseTask(task.getTask());
                    }
                    try {
                        model.getLogModel().add(model.getUserModel().getUser().getId(), Alert.AlertType.INFORMATION, model.getUserModel().getUser().getfName() + " has stopped task " + task.getTask().getName());
                    } catch (GUIException ex) {
                        Window.openExceptionWindow("There was a problem with a log", ex.getStackTrace());
                    }
                });
                tasks.clear();
            } else {
                return true;
            }
        } else if (tasks.isEmpty()) {
            Window.openExceptionWindow("No tasks are selected.");
        } else {
            ConvTask task = tasks.get(0).getTask();
            if (cancel) {
                tp.cancelTask(task);
            } else {
                tp.pauseTask(task);
            }
            tasks.clear();
            try {
                model.getLogModel().add(model.getUserModel().getUser().getId(), Alert.AlertType.INFORMATION, model.getUserModel().getUser().getfName() + " has paused task " + task.getName());
            } catch (GUIException ex) {
                Window.openExceptionWindow("There was a problem with a log", ex.getStackTrace());
            }
        }
        return false;
    }

    @Override
    public void postInit(ModelManager model) {
        this.model = model;

//        if (model.getTaskModel().getPendingTasks().isEmpty()) {
//            return;
//        }
        
        setTasks(selectedPenTasks, model.getTaskModel().getPendingTasks(), vBoxPen);
        setTasks(selectedFinTasks, model.getTaskModel().getFinishedTasks(), vBoxFin);
        setTasks(selectedCanTasks, model.getTaskModel().getCancelledTasks(), vBoxCan);

        genRightClickStart();
        genRightClickDel();
        genRightClickPause();
        genRightClickStop(); 
    }

    private void setTasks(List<TaskView> selectedTasks, ObservableList<ConvTask> tasks, VBox vBox) {
        setTaskView(selectedTasks, tasks, vBox);
        tasks.addListener((ListChangeListener.Change<? extends ConvTask> c) -> {
            while (c.next()) {
                if (c.wasAdded() || c.wasPermutated() || c.wasRemoved() || c.wasReplaced() || c.wasUpdated()) {
                    setTaskView(selectedTasks, tasks, vBox);
                }
            }
        });
    }

    /**
     * Generates TaskViews for List
     *
     * @param model
     */
    private void setTaskView(List<TaskView> selectedTasks, ObservableList<ConvTask> tasks, VBox vBox) {
        int i = 0;
        vBox.getChildren().clear();
        selectedTasks.clear();
        for (ConvTask convTask : tasks) {
            TaskView taskView = makeTaskView(selectedTasks, vBox, convTask);
            taskView.setId(String.valueOf(i));
            vBox.getChildren().add(taskView);
            i++;
        }
    }

    private TaskView makeTaskView(List<TaskView> selectedTasks, VBox vBox, ConvTask convTask) {
        TaskView taskView = new TaskView(convTask);
        taskView.postInit(model);
        Tooltip tt = new Tooltip(convTask.getTarget().toString());
        Tooltip.install(taskView, tt);
        taskView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    cMenu.hide();
                    if (event.isControlDown()) {
                        if (selectedTasks.contains(taskView)) {
                            toggleSelected(taskView, selectedTasks, false);
                        } else {
                            toggleSelected(taskView, selectedTasks, true);
                        }
                    } else if (event.isShiftDown()) {
                        toggleSelected(taskView, selectedTasks, true);
                        int id1 = Integer.valueOf(selectedTasks.get(0).getId());

                        if (selectedTasks.size() > 1) {
                            int id2 = Integer.valueOf(selectedTasks.get(selectedTasks.size() - 1).getId());

                            //TO BE DONE!
                            List<TaskView> allTasks = new ArrayList();

                            vBox.getChildren().forEach((node) -> {
                                allTasks.add((TaskView) node);
                            });

                            for (TaskView task : allTasks) {
                                if (Integer.valueOf(task.getId()) > id1 && Integer.valueOf(task.getId()) < id2) {
                                    toggleSelected(task, selectedTasks, true);
                                } else if (Integer.valueOf(task.getId()) < id1 && Integer.valueOf(task.getId()) > id2) {
                                    toggleSelected(task, selectedTasks, true);
                                }
                            }
                        }

                    } else {
                        if (selectedTasks.contains(taskView)) {
                            clearSelected(selectedTasks);

                        } else {
                            clearSelected(selectedTasks);
                            toggleSelected(taskView, selectedTasks, true);
                        }
                    }


                    if (event.getClickCount() % 2 == 0) {
                        try {
                            String temp = convTask.getTarget().getParentFile().getPath();
                            temp = temp.replaceAll("\\\\", "/");
                            Desktop.getDesktop().browse(new URI(temp));
                        } catch (URISyntaxException | IOException ex) {
                            Logger.getLogger(TaskWindowController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                if (event.getButton().equals(MouseButton.SECONDARY)) {
                    cMenu.show(vBox, event.getScreenX(), event.getScreenY());
                    if (!selectedTasks.contains(taskView)) {
                        toggleSelected(taskView, selectedTasks, true);
                    }
                }
            }
        }
        );
        return taskView;
    }

    private void toggleSelected(TaskView taskView, List<TaskView> selectedTasks, boolean selected) {
        if (selected) {
            selectedTasks.add(taskView);
            taskView.setStyle("-fx-border-color: #2e6da4; -fx-border-radius: 4px; "
                    + "-fx-background-color: derive(#337ab7, 80%); "
                    + "-fx-background-radius: 4px; -fx-text-fill: white");
        } else {
            selectedTasks.remove(taskView);
            taskView.setStyle("-fx-border-color: transparent");
        }
    }

    private void clearSelected(List<TaskView> taskViews) {
        List<TaskView> temp = new ArrayList(taskViews);
        temp.forEach((taskView) -> {
            toggleSelected(taskView, taskViews, false);
        });
    }

    /**
     * Generates MenuItem for starting task
     */
    private void genRightClickStart() {
        MenuItem startItem = new MenuItem("Start selected tasks");
        startItem.setOnAction((event) -> {
            selectedPenTasks.forEach((selectedTask) -> {
                model.getTaskModel().start(selectedTask.getTask());
            });
        });
        cMenu.getItems().addAll(startItem);
    }

    /**
     * Generates MenuItem for delete task
     */
    private void genRightClickDel() {
        MenuItem delItem = new MenuItem("Delete selected task");
        delItem.setOnAction((event) -> {
            if (selectedPenTasks.size() > 1) {
                if (openConfirmWindow("Are you sure you want to delete " + selectedPenTasks.size() + " tasks?", null, null, false)) {
                    selectedPenTasks.forEach((task) -> {
                        model.getTaskModel().getPendingTasks().remove(task.getTask());
                        try {
                            model.getLogModel().add(model.getUserModel().getUser().getId(), Alert.AlertType.INFORMATION, model.getUserModel().getUser().getfName() + " has deleted task " + task.getTask().getName());
                        } catch (GUIException ex) {
                            Window.openExceptionWindow("There was a problem with a log", ex.getStackTrace());
                        }

                    });
                    selectedPenTasks.clear();
                } else {
                    return;
                }
            } else {
                selectedPenTasks.forEach((selectedTask) -> {
                    model.getTaskModel().getPendingTasks().remove(selectedTask.getTask());
                    try {
                        model.getLogModel().add(model.getUserModel().getUser().getId(), Alert.AlertType.INFORMATION, model.getUserModel().getUser().getfName() + " has deleted task " + selectedTask.getTask().getName());
                    } catch (GUIException ex) {
                        Window.openExceptionWindow("There was a problem with a log", ex.getStackTrace());
                    }
                });
                selectedPenTasks.clear();
            }
        });
        cMenu.getItems().add(delItem);
    }

    /**
     * Adds listener to TaskList in model
     */
    private void genRightClickPause() {
        MenuItem item = new MenuItem("Pause selected task");
        item.setOnAction((event) -> {
            pauseOrCancelTask(selectedPenTasks, false);
        });
        cMenu.getItems().add(item);
    }

    private void genRightClickStop() {
        MenuItem item = new MenuItem("Stop selected task");
        item.setOnAction((event) -> {
            pauseOrCancelTask(selectedPenTasks, true);
        });
        cMenu.getItems().add(item);
    }

    /**
     * Opens window with yes and no buttons
     *
     * @param msg
     * @param map
     * @return
     */
    private boolean openConfirmWindow(String msg, HashMap map, File file, boolean txtField) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(Window.View.Confirm.getView()));
            Parent root = fxmlLoader.load();

            ConfirmationWindowController cwc = fxmlLoader.getController();
            cwc.postInit(model);
            cwc.setInfo(msg, map, file, txtField);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Confirmation");
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.showAndWait();
            return cwc.getConfirmation();
        } catch (IOException ex) {
            Window.openExceptionWindow("Couldn't open confirmation window.");
        }
        return false;
    }

    @FXML
    private void handleTaskCancel(ActionEvent event) {
        pauseOrCancelTask(selectedPenTasks, true);
    }

    @FXML
    private void handleStartAgainFin(ActionEvent event) {
        startTask(selectedFinTasks);
    }

    @FXML
    private void handleStartAgainCan(ActionEvent event) {
        startTask(selectedCanTasks);
    }
}
