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
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import shoreline.gui.model.MainModel;

/**
 * FXML Controller class
 *
 * @author madst
 */
public class TaskWindowController implements Initializable, IController {

    List<TaskView> taskViewList = new ArrayList();
    List<TaskView> selectedTaskes = new ArrayList();

    MainModel model;

    @FXML
    private VBox vBox;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void handleTaskPlay(ActionEvent event) {
        for (TaskView taskView : taskViewList) {
            taskView.getTask().setIsRunning();
            System.out.println("din mor er grum!");
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

        genTasksForList(model);

        taskViewList.forEach((taskView) -> {
            vBox.getChildren().add(taskView);
        });
    }

    private void genTasksForList(MainModel model) {
        model.getTaskList().forEach((convTask) -> {
            TaskView taskView = new TaskView(convTask);
            Tooltip tt = new Tooltip(convTask.getTarget().toString());
            Tooltip.install(taskView, tt);
            taskView.setOnMouseClicked((event) -> {
                if (selectedTaskes.contains(taskView)) {
                    selectedTaskes.remove(taskView);
                    taskView.setStyle("-fx-border-color: transparent");
                } else {
                    selectedTaskes.add(taskView);
                    taskView.setStyle("-fx-border-color: #2e6da4; -fx-border-radius: 4px; -fx-background-color: derive(#337ab7, 80%); "
                            + "-fx-background-radius: 4px; -fx-text-fill: white");
                }
            });
            taskViewList.add(taskView);
        });
    }

}
