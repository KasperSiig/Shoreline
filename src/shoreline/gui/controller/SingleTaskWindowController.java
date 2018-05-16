/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.gui.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import shoreline.be.ConvTask;
import shoreline.exceptions.GUIException;
import shoreline.gui.model.ModelManager;
import shoreline.statics.Window;
import shoreline.be.Config;
import shoreline.exceptions.BLLException;
import shoreline.statics.Styling;

/**
 * FXML Controller class
 *
 * @author madst
 */
public class SingleTaskWindowController implements Initializable, IController {

    ModelManager model;
    HashMap cellIndexMap;
    File importFile;
    File targetFile;

    @FXML
    private BorderPane bPane;
    @FXML
    private JFXTextField txtFileName;
    @FXML
    private JFXComboBox<Config> cbbConfig;
    @FXML
    private TextField txtImportPath;
    @FXML
    private TextField txtTargetPath;
    @FXML
    private BorderPane bPaneSplit;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    public void postInit(ModelManager model) {
        this.model = model;

        cbbConfig.setItems(model.getConfigModel().getConfigList());

        try {
            Window.openView(model, bPaneSplit, Window.View.TaskView, "center");
        } catch (GUIException ex) {
            Logger.getLogger(SingleTaskWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All supported types", "*.xlsx", "*.csv"),
                new FileChooser.ExtensionFilter("XLSX files (.xlsx)", "*.xlsx"),
                new FileChooser.ExtensionFilter("CSV files (.cvs)", "*.csv")
        // ADD NEW EXTENSIONS HERE, Seperate with comma (,)
        );
        File tempFile = fileChooser.showOpenDialog(bPane.getScene().getWindow());

        if (tempFile != null) {
            importFile = tempFile;
            txtImportPath.setText(importFile.getAbsolutePath());

            List temp = new ArrayList();
            try {
                for (Config config : model.getConfigModel().getAllConfigs()) {
                    String extension = "";

                    int i = importFile.getAbsolutePath().lastIndexOf('.');
                    if (i > 0) {
                        extension = importFile.getAbsolutePath().substring(i + 1);
                    }
                    if (config.getExtension().equals(extension)) {
                        temp.add(config);
                    }
                }
                cbbConfig.getItems().clear();
                cbbConfig.getItems().addAll(temp);

                txtFileName.setText(importFile.getName());
                
            } catch (BLLException ex) {
                Logger.getLogger(SingleTaskWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void handleImportFileBtn(ActionEvent event) {
        chooseFile();
    }

    @FXML
    private void handleTargetFolderBtn(ActionEvent event) {
        chooseTarget();
    }

    private void chooseTarget() {
        DirectoryChooser dirChoose = new DirectoryChooser();
        File file = dirChoose.showDialog(null);

        if (file != null) {
            targetFile = file;
            txtTargetPath.setText(targetFile.getAbsolutePath());
        }
    }

    @FXML
    private void handleCreateTask(ActionEvent event) {
        createTask();
    }

    private void createTask() {

        if (checkRequired()) {
            return;
        }

        String name = txtFileName.getText();

        Config config = cbbConfig.getSelectionModel().getSelectedItem();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());

        File tempFile = new File(targetFile + "\\" + date + " - " + name + ".json");

        try {
            cellIndexMap = model.getConfigModel().getTitles(importFile);

            HashMap temp = new HashMap(config.getMap());
            HashMap cellTemp = new HashMap(cellIndexMap);

            ConvTask task = new ConvTask(cellTemp, temp, name, importFile, tempFile);

            model.getTaskModel().addToPendingTasks(task);
            model.getTaskModel().addCallable(task);
        } catch (GUIException ex) {
            Logger.getLogger(SingleTaskWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Checks if there is an input file. Checks if the JSONmap is empty. Checks
     * if there is a file name. Checks if there is a target path.
     *
     * @return
     */
    private boolean checkRequired() {
        if (importFile == null) {
            Window.openSnack("Please choose an input file", bPane, "red");
            return true;
        } else {
            //to do 
        }
        if (txtFileName.getText().equals("")) {
            Styling.redOutline(txtFileName);
            Window.openSnack("Please enter a file name", bPane, "red");
            return true;
        } else {
            Styling.clearRedOutline(txtFileName);
        }
        if (targetFile == null) {
            Window.openSnack("Please choose a target folder", bPane, "red");
            return true;
        } else {
            //To do
        }
        return false;
    }

}
