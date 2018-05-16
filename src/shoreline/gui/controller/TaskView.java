/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.gui.controller;

import com.jfoenix.controls.JFXSpinner;
import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import shoreline.be.ConvTask;
import shoreline.gui.model.ModelManager;

/**
 *
 * @author madst
 */
public class TaskView extends BorderPane implements IController {

    ModelManager model;
    ConvTask task;

    @FXML
    private Label lblTaskName;
    @FXML
    private Label lblTargetDir;
    @FXML
    private Label lblStatus;
    @FXML
    private VBox vBox;

    public TaskView(ConvTask task) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/shoreline/gui/view/TaskView.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
            this.task = task;
        } catch (IOException ex) {
            //to be done 
        }
    }

    @Override
    public void postInit(ModelManager model) {
        this.model = model;
        setInfo(task);
        setWidthListener();
    }

    private void setInfo(ConvTask task) {
        this.task = task;
        lblStatus.setText(task.getStatus().getValue());
        task.getStatus().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue.equals(ConvTask.Status.Running.getValue())) {
                JFXSpinner spin = new JFXSpinner();
                vBox.getChildren().set(0, spin);
            } else {
                vBox.getChildren().set(0, lblStatus);
                lblStatus.setText(task.getStatus().getValue());
            }
        });
        pathName(vBox.getWidth());
        lblTaskName.setText(task.getName());

    }

    private void setWidthListener() {
        vBox.widthProperty().addListener((observable, oldValue, newValue) -> {
            pathName(newValue);
        });
    }

    private void pathName(Number size) {
        // 7 PIXELS PER BOGSTAV
        int iSize = size.intValue();
        int characters = (iSize / 7);
        String path = task.getTarget().getAbsolutePath();
        String root = path.substring(0, 3);
        if (path.length() > characters) {
            path = root + ".." + path.substring(path.length() - characters);
        }
        lblTargetDir.setText(path);
    }

    public ConvTask getTask() {
        return task;
    }
}
