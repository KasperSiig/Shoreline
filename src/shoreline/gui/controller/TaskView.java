/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.gui.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import shoreline.be.ConvTask;
import shoreline.gui.model.MainModel;

/**
 *
 * @author madst
 */
public class TaskView extends BorderPane implements IController {

    MainModel model;
    ConvTask task;

    @FXML
    private Label lblTaskName;
    @FXML
    private Label lblTargetDir;
    @FXML
    private Label lblStatus;

    public TaskView(ConvTask task) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/shoreline/gui/view/TaskView.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
            this.task = task;
            setInfo(task);
        } catch (IOException ex) {
            //to be done 
        }
    }

    @Override
    public void postInit(MainModel model) {
        this.model = model;
    }

    private void setInfo(ConvTask task) {

        lblStatus.setText(String.valueOf(task.isRunning()));
        
        String temp = task.getTarget().getAbsolutePath();
        String temp2 = temp.substring(0,3);
        temp = temp.substring(temp.length() - 20, temp.length());
        
        lblTargetDir.setText(temp2 + "..." + temp);
        lblTaskName.setText(task.getName());

    }

    public ConvTask getTask() {
        return task;
    }
}
