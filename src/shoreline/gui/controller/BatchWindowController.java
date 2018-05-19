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
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import shoreline.exceptions.BEException;
import shoreline.be.Batch;
import shoreline.be.Config;
import shoreline.bll.LogicManager;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.GUIException;
import shoreline.gui.model.ModelManager;
import shoreline.statics.Styling;
import shoreline.statics.Window;

/**
 * FXML Controller class
 *
 * @author madst
 */
public class BatchWindowController implements Initializable, IController {

    @FXML
    private BorderPane bPane;
    @FXML
    private HBox hBoxImport;
    @FXML
    private TextField txtImportPath;
    @FXML
    private HBox hBoxTarget;
    @FXML
    private TextField txtTargetPath;
    @FXML
    private JFXTextField txtFileName;
    @FXML
    private JFXComboBox<Config> comboConfig;
    @FXML
    private BorderPane bPaneSplit;

    private ModelManager model;
    private File targetFolder;
    private File importFolder;

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
        try {
            Window.openView(model, bPaneSplit, Window.View.BatchTask, "center");
            

            comboConfig.setItems(model.getConfigModel().getConfigList());
            
            
        } catch (GUIException ex) {
            Logger.getLogger(BatchWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleTargetFolderBtn(ActionEvent event) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        File file = dirChooser.showDialog(null);

        if (file != null) {
            targetFolder = file;
            txtTargetPath.setText(targetFolder.getAbsolutePath());
        }
    }

    @FXML
    private void handleImportFolderBtn(ActionEvent event) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        File file = dirChooser.showDialog(null);

        if (file != null) {
            importFolder = file;
            txtImportPath.setText(importFolder.getAbsolutePath());
        }
    }

    @FXML
    private void handleCreateBatch(ActionEvent event) {
        if (checkRequired()) {
            return;
        }

        try {
            Batch tempBacth = new Batch(importFolder, targetFolder, txtFileName.getText(), comboConfig.getSelectionModel().getSelectedItem());
            model.getBatchModel().addToBatches(tempBacth);
            Window.openSnack("New bacth " + tempBacth.getName() + " was created", bPane, "blue");
        } catch (BEException ex) {
            Logger.getLogger(BatchWindowController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GUIException ex) {
            Logger.getLogger(BatchWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    /**
     * Checks if there is an input file. Checks if the JSONmap is empty. Checks
     * if there is a file name. Checks if there is a target path.
     *
     * @return
     */
    private boolean checkRequired() {
        boolean hasFailed = false;
        if (importFolder == null) {
            // Window.openSnack("Please choose an input file", bPane, "red");
            Styling.redOutline(hBoxImport);
            hasFailed = true;
        } else {
            Styling.clearRedOutline(hBoxImport);
        }
        if (txtFileName.getText().equals("")) {
            Styling.redOutline(txtFileName);
            //Window.openSnack("Please enter a file name", bPane, "red");
            hasFailed = true;
        } else {
            Styling.clearRedOutline(txtFileName);
        }
        if (importFolder == null) {
            Styling.redOutline(hBoxTarget);
            //Window.openSnack("Please choose a target folder", bPane, "red");
            hasFailed = true;
        } else {
            Styling.clearRedOutline(hBoxTarget);
        }
        if (comboConfig.getSelectionModel().getSelectedItem() == null) {
            Styling.redOutline(comboConfig);
            //Window.openSnack("Please select a config", bPane, "red");
            hasFailed = true;
        } else {
            Styling.clearRedOutline(comboConfig);
        }
        if (hasFailed) {
            Window.openSnack("Could not create task. Missing input is highlighted.", bPane, "red", 4000);
        }
        return hasFailed;
    }

}
