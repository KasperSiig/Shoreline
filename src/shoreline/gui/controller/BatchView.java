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
import shoreline.be.Batch;
import shoreline.be.ConvTask;
import shoreline.gui.model.ModelManager;

/**
 *
 * @author madst
 */
public class BatchView extends BorderPane implements IController {

    ModelManager model;
    Batch batch;

    @FXML
    private Label lblTaskName;
    @FXML
    private Label lblTargetDir;
    @FXML
    private Label lblPending;
    @FXML
    private Label lblHandled;
    @FXML
    private Label lblFailed;
    @FXML
    private VBox vBox;

    public BatchView(Batch batch) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/shoreline/gui/view/BatchView.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
            this.batch = batch;
        } catch (IOException ex) {
            //to be done 
        }
    }

    @Override
    public void postInit(ModelManager model) {
        this.model = model;
        setInfo(batch);
        setWidthListener();
//        lblTaskName.setWrapText(false);
        lblTaskName.prefWidthProperty().bind(vBox.widthProperty());
    }

    private void setInfo(Batch batch) {
        pathName(vBox.getWidth());
        lblTaskName.setText(batch.getName());

        lblPending.setText(String.valueOf(batch.getPendingTasks().size()));
        batch.getFilesPending().addListener((observable, oldValue, newValue) -> {
            lblPending.setText(String.valueOf(newValue));
        });
        batch.getFilesHandled().addListener((observable, oldValue, newValue) -> {
            lblHandled.setText(String.valueOf(newValue));
        });
        batch.getFilesFailed().addListener((observable, oldValue, newValue) -> {
            lblFailed.setText(String.valueOf(newValue));
        });
    }

    private void setWidthListener() {
        vBox.widthProperty().addListener((observable, oldValue, newValue) -> {
            pathName(newValue);
        });
    }

    private void pathName(Number size) {
        // 7 PIXELS PER BOGSTAV
        int iSize = size.intValue();
        int characters = (iSize / 9);
        String path = batch.getTargetDir().getAbsolutePath();
        String root = path.substring(0, 3);
        if (path.length() > characters) {
            path = root + ".." + path.substring(path.length() - characters);
        }
        lblTargetDir.setText(path);
    }

    public Batch getBatch() {
        return batch;
    }

}
