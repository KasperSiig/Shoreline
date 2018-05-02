/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.gui.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import shoreline.be.ConvTask;
import shoreline.gui.model.MainModel;

/**
 * FXML Controller class
 *
 * @author madst
 */
public class TaskWindowController implements Initializable, IController {

    List<TaskView> taskViewList = new ArrayList();
    List<TaskView> selectedTasks = new ArrayList();
    ContextMenu cMenu = new ContextMenu();

    MainModel model;

    @FXML
    private VBox vBox;
    @FXML
    private AnchorPane aPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void handleTaskPlay(ActionEvent event) {
        for (TaskView taskView : selectedTasks) {
            System.out.println(taskView.getTask().getMapper());
            taskView.getTask().setIsRunning();
            model.startTask(taskView.getTask());
        }
    }

    @FXML
    private void handleTaskPause(ActionEvent event) {
    }

    @FXML
    private void handleTaskStop(ActionEvent event) {
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

        taskViewList.forEach((taskView) -> {
            vBox.getChildren().add(taskView);
        });
        genRightClick();
    }

    private void genTasksForList(MainModel model) {
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
                }
            });
            taskViewList.add(taskView);
        });
    }

    @FXML
    private void showRigthClick(MouseEvent event) {
//        if (event.getButton().equals(MouseButton.SECONDARY)) {
//            cMenu.show(vBox, event.getSceneX(), event.getSceneY());
//        }

    }

    private void genRightClick() {
        MenuItem startItem = new MenuItem("Start selected tasks");
        startItem.setOnAction((event) -> {
            for (TaskView selectedTask : selectedTasks) {
                model.startTask(selectedTask.getTask());
            }
        });

        MenuItem delItem = new MenuItem("Delete task");
        delItem.setOnAction((event) -> {
            
        });
        cMenu.getItems().addAll(startItem);
    }

}
