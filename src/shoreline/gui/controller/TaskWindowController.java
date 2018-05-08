package shoreline.gui.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import shoreline.be.ConvTask;
import shoreline.bll.ThreadPool;
import shoreline.exceptions.GUIException;
import shoreline.gui.model.MainModel;
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

    private MainModel model;

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
        selectedPenTasks.forEach((taskView) -> {
            model.startTask(taskView.getTask());
            try {
                model.addLog(model.getUser().getId(), Alert.AlertType.INFORMATION, model.getUser().getfName() + " has started task " + taskView.getTask().getName());
            } catch (GUIException ex) {
                Window.openExceptionWindow("There was a problem with a log", ex.getStackTrace());
            }
        });
        selectedPenTasks.clear();
        clearSelected(selectedPenTasks);
    }

    /**
     * Pauses all selected tasks
     *
     * @param event
     */
    @FXML
    private void handleTaskPause(ActionEvent event) {
        if (pauseTask(selectedPenTasks)) {
            return;
        }
    }

    private boolean pauseTask(List<TaskView> tasks) {
        ThreadPool tp = ThreadPool.getInstance();
        if (tasks.size() > 1) {
            if (openConfirmWindow("Are you sure you want to pause " + tasks.size() + " tasks?", null, null, false)) {
                tasks.forEach((task) -> {
                    tp.pauseTask(task.getTask());
                    try {
                        model.addLog(model.getUser().getId(), Alert.AlertType.INFORMATION, model.getUser().getfName() + " has paused task " + task.getTask().getName());
                    } catch (GUIException ex) {
                        Window.openExceptionWindow("There was a problem with a log", ex.getStackTrace());
                    }
                });
            } else {
                return true;
            }
        } else if (tasks.isEmpty()) {
            Window.openExceptionWindow("No tasks are selected.");
        } else {
            ConvTask task = tasks.get(0).getTask();
            tp.pauseTask(task);
            try {
                model.addLog(model.getUser().getId(), Alert.AlertType.INFORMATION, model.getUser().getfName() + " has paused task " + task.getName());
            } catch (GUIException ex) {
                Window.openExceptionWindow("There was a problem with a log", ex.getStackTrace());
            }
        }
        return false;
    }

    private boolean cancelTask(List<TaskView> tasks) {
        ThreadPool tp = ThreadPool.getInstance();
        if (tasks.size() > 1) {
            if (openConfirmWindow("Are you sure you want to stop " + tasks.size() + " tasks?", null, null, false)) {
                tasks.forEach((task) -> {
                    tp.cancelTask(task.getTask());
                    try {
                        model.addLog(model.getUser().getId(), Alert.AlertType.INFORMATION, model.getUser().getfName() + " has stopped task " + task.getTask().getName());
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
            tp.cancelTask(task);
            tasks.clear();
            try {
                model.addLog(model.getUser().getId(), Alert.AlertType.INFORMATION, model.getUser().getfName() + " has paused task " + task.getName());
            } catch (GUIException ex) {
                Window.openExceptionWindow("There was a problem with a log", ex.getStackTrace());
            }
        }
        return false;
    }

    @Override
    public void postInit(MainModel model) {
        this.model = model;

        if (model.getPendingTasks().isEmpty()) {
            return;
        }

//        addTaskViews(selectedPenTasks, vBoxPen);
//        addTaskViews(selectedFinTasks, vBoxFin);
//        addTaskViews(selectedCanTasks, vBoxCan);
        setTasks(selectedPenTasks, model.getPendingTasks(), vBoxPen);
        setTasks(selectedFinTasks, model.getFinishedTasks(), vBoxFin);
        setTasks(selectedCanTasks, model.getCancelledTasks(), vBoxCan);

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
        vBox.getChildren().clear();
        tasks.forEach((convTask) -> {
            TaskView taskView = makeTaskView(selectedTasks, vBox, convTask);
            vBox.getChildren().add(taskView);
        });
    }

    private TaskView makeTaskView(List<TaskView> selectedTasks, VBox vBox, ConvTask convTask) {
        TaskView taskView = new TaskView(convTask);
        taskView.postInit(model);
        Tooltip tt = new Tooltip(convTask.getTarget().toString());
        Tooltip.install(taskView, tt);
        taskView.setOnMouseClicked((event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                cMenu.hide();
                if (selectedTasks.contains(taskView)) {
                    toggleSelected(taskView, selectedTasks, false);
                } else {
                    toggleSelected(taskView, selectedTasks, true);
                }
            }
            if (event.getButton().equals(MouseButton.SECONDARY)) {
                cMenu.show(vBox, event.getScreenX(), event.getScreenY());
                if (!selectedTasks.contains(taskView)) {
                    toggleSelected(taskView, selectedTasks, true);
                }
            }
        });
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
        taskViews.forEach((taskView) -> {
            taskView.setStyle("-fx-border-color: transparent");
        });
    }


    /**
     * Generates MenuItem for starting task
     */
    private void genRightClickStart() {
        MenuItem startItem = new MenuItem("Start selected tasks");
        startItem.setOnAction((event) -> {
            selectedPenTasks.forEach((selectedTask) -> {
                model.startTask(selectedTask.getTask());
            });
        });
        cMenu.getItems().addAll(startItem);
    }

    /**
     * Generates MenuItem for delete task
     */
    private void genRightClickDel() {
//        cMenu.getItems().remove(0);
        MenuItem delItem = new MenuItem("Delete selected task");
        delItem.setOnAction((event) -> {
            if (selectedPenTasks.size() > 1) {
                if (openConfirmWindow("Are you sure you want to delete " + selectedPenTasks.size() + " tasks?", null, null, false)) {
                    selectedPenTasks.forEach((task) -> {
                        model.getPendingTasks().remove(task.getTask());
                        try {
                            model.addLog(model.getUser().getId(), Alert.AlertType.INFORMATION, model.getUser().getfName() + " has deleted task " + task.getTask().getName());
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
                    model.getPendingTasks().remove(selectedTask.getTask());
                    try {
                        model.addLog(model.getUser().getId(), Alert.AlertType.INFORMATION, model.getUser().getfName() + " has deleted task " + selectedTask.getTask().getName());
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
            pauseTask(selectedPenTasks);
        });
        cMenu.getItems().add(item);
    }

    private void genRightClickStop() {
        MenuItem item = new MenuItem("Stop selected task");
        item.setOnAction((event) -> {
            cancelTask(selectedPenTasks);
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
        if (cancelTask(selectedPenTasks)) {
            return;
        }
    }

    @FXML
    private void handleStartAgainFin(ActionEvent event) {
    }

    @FXML
    private void handleStartAgainCan(ActionEvent event) {
    }
}
