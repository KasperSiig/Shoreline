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
import shoreline.be.BEException;
import shoreline.be.Batch;
import shoreline.be.Config;
import shoreline.bll.LogicManager;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.GUIException;
import shoreline.gui.model.ModelManager;
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
    private JFXComboBox<?> comboConfig;
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
            Window.openView(model, bPane, Window.View.BatchTask, "right");
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
    private void handleCreateBatch(ActionEvent event) throws BLLException, BEException {
        LogicManager l = new LogicManager();
        List<File> files = l.getFilesInBatch(new Batch(new File("C:\\Users\\kaspe\\Desktop"), new File("C:\\Users\\kaspe\\Desktop"), null, new Config(null, "xlsx", null)));
        files.forEach((t) -> {
            System.out.println(t);
        });
    }

}
