package shoreline.gui.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ListChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
    private List<TaskView> selectedTasks;

    private ContextMenu cMenu;

    private MainModel model;

    /* JavaFX Variables */
    @FXML
    private VBox vBox;
    @FXML
    private AnchorPane aPane;

    public TaskWindowController() {
        this.cMenu = new ContextMenu();
        this.selectedTasks = new ArrayList();
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
        for (TaskView taskView : selectedTasks) {
            System.out.println(taskView.getTask().getMapper());
            model.startTask(taskView.getTask());
            try {
                model.addLog(model.getUser().getId(), Alert.AlertType.INFORMATION, model.getUser().getfName() + " has started task " + taskView.getTask().getName());
            } catch (GUIException ex) {
                Window.openExceptionWindow("There was a problem with a log", ex.getStackTrace());
            }
        }
    }

    /**
     * Pauses all selected tasks
     *
     * @param event
     */
    @FXML
    private void handleTaskPause(ActionEvent event) {
        if (pauseTask()) {
            return;
        }
    }

    private boolean pauseTask() {
        ThreadPool tp = ThreadPool.getInstance();
        if (selectedTasks.size() > 1) {
            if (openConfirmWindow("Are you sure you want to pause " + selectedTasks.size() + " tasks?", null, null, false)) {
                selectedTasks.forEach((task) -> {
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
        } else if (selectedTasks.isEmpty()) {
            Window.openExceptionWindow("No tasks are selected.");
        } else {
            ConvTask task = selectedTasks.get(0).getTask();
            tp.pauseTask(task);
            try {
                model.addLog(model.getUser().getId(), Alert.AlertType.INFORMATION, model.getUser().getfName() + " has paused task " + task.getName());
            } catch (GUIException ex) {
                Window.openExceptionWindow("There was a problem with a log", ex.getStackTrace());
            }
        }
        return false;
    }

    /**
     * Cancels all selected tasks
     *
     * @param event
     */
    @FXML
    private void handleTaskStop(ActionEvent event) {
        if (stopTask()) {
            return;
        }
    }

    private boolean stopTask() {
        ThreadPool tp = ThreadPool.getInstance();
        if (selectedTasks.size() > 1) {
            if (openConfirmWindow("Are you sure you want to stop " + selectedTasks.size() + " tasks?", null, null, false)) {
                selectedTasks.forEach((task) -> {
                    tp.cancelTask(task.getTask());
                    model.getTaskList().remove(task.getTask());
                    try {
                        model.addLog(model.getUser().getId(), Alert.AlertType.INFORMATION, model.getUser().getfName() + " has stopped task " + task.getTask().getName());
                    } catch (GUIException ex) {
                        Window.openExceptionWindow("There was a problem with a log", ex.getStackTrace());
                    }
                });
                selectedTasks.clear();
            } else {
                return true;
            }
        } else if (selectedTasks.isEmpty()) {
            Window.openExceptionWindow("No tasks are selected.");
        } else {
            ConvTask task = selectedTasks.get(0).getTask();
            tp.cancelTask(task);
            model.getTaskList().remove(task);
            selectedTasks.clear();
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

        if (model.getTaskList().isEmpty()) {
            return;
        }

        for (ConvTask convTask : model.getTaskList()) {
            System.out.println(convTask.getMapper());
        }

        genTasksForList(model);

        addListener();

        genRightClickStart();
        genRightClickDel();
        genRightClickPause();
        genRightClickStop();
    }

    /**
     * Generates TaskViews for List
     * 
     * @param model 
     */
    private void genTasksForList(MainModel model) {
        vBox.getChildren().clear();
        model.getTaskList().forEach((convTask) -> {
            TaskView taskView = new TaskView(convTask);
            Tooltip tt = new Tooltip(convTask.getTarget().toString());
            Tooltip.install(taskView, tt);
            taskView.setOnMouseClicked((event) -> {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    cMenu.hide();
                    if (selectedTasks.contains(taskView)) {
                        selectedTasks.remove(taskView);
                        taskView.setStyle("-fx-border-color: transparent");
                    } else {
                        selectedTasks.add(taskView);
                        taskView.setStyle("-fx-border-color: #2e6da4; -fx-border-radius: 4px; -fx-background-color: derive(#337ab7, 80%); "
                                + "-fx-background-radius: 4px; -fx-text-fill: white");
                    }
                }
                if (event.getButton().equals(MouseButton.SECONDARY)) {
                    cMenu.show(vBox, event.getScreenX(), event.getScreenY());
                    if (!selectedTasks.contains(taskView)) {
                        selectedTasks.add(taskView);
                        taskView.setStyle("-fx-border-color: #2e6da4; -fx-border-radius: 4px; -fx-background-color: derive(#337ab7, 80%); "
                                + "-fx-background-radius: 4px; -fx-text-fill: white");
                    }
                }
            });
            vBox.getChildren().add(taskView);
        });
    }

    @FXML
    private void showRigthClick(MouseEvent event) {
//        if (event.getButton().equals(MouseButton.SECONDARY)) {
//            cMenu.show(vBox, event.getSceneX(), event.getSceneY());
//        }

    }

    /**
     * Generates MenuItem for starting task
     */
    private void genRightClickStart() {
        MenuItem startItem = new MenuItem("Start selected tasks");
        startItem.setOnAction((event) -> {
            selectedTasks.forEach((selectedTask) -> {
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
            if (selectedTasks.size() > 1) {
                if (openConfirmWindow("Are you sure you want to delete " + selectedTasks.size() + " tasks?", null, null, false)) {
                    selectedTasks.forEach((task) -> {
                        model.getTaskList().remove(task.getTask());
                        try {
                            model.addLog(model.getUser().getId(), Alert.AlertType.INFORMATION, model.getUser().getfName() + " has deleted task " + task.getTask().getName());
                        } catch (GUIException ex) {
                            Window.openExceptionWindow("There was a problem with a log", ex.getStackTrace());
                        }

                    });
                    selectedTasks.clear();
                } else {
                    return;
                }
            } else {
                selectedTasks.forEach((selectedTask) -> {
                    model.getTaskList().remove(selectedTask.getTask());
                    try {
                        model.addLog(model.getUser().getId(), Alert.AlertType.INFORMATION, model.getUser().getfName() + " has deleted task " + selectedTask.getTask().getName());
                    } catch (GUIException ex) {
                        Window.openExceptionWindow("There was a problem with a log", ex.getStackTrace());
                    }
                });
                selectedTasks.clear();
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
            pauseTask();
        });
        cMenu.getItems().add(item);
    }

    private void genRightClickStop() {
        MenuItem item = new MenuItem("Stop selected task");
        item.setOnAction((event) -> {
            stopTask();
        });
        cMenu.getItems().add(item);
    }

    private void addListener() {
        model.getTaskList().addListener((ListChangeListener.Change<? extends ConvTask> c) -> {
            while (c.next()) {
                if (c.wasAdded() || c.wasPermutated() || c.wasRemoved() || c.wasReplaced() || c.wasUpdated()) {
                    genTasksForList(model);
                }
            }
        });
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
}
