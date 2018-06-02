/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.gui.controller;

import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import shoreline.exceptions.GUIException;
import shoreline.gui.model.ModelManager;
import shoreline.statics.Window;

/**
 * FXML Controller class
 *
 * @author madst
 */
public class SettingsWindowController implements Initializable, IController {

    private ModelManager model;
    private File JSONFile;

    @FXML
    private BorderPane bPane;
    @FXML
    private VBox SettingsVNox;
    @FXML
    private TextField txtJSONPath;
    @FXML
    private JFXTextField txtIndentFactor;

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
    }

    @FXML
    private void handleJSONBtn(ActionEvent event) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        File temp = dirChooser.showDialog(null);
        if (temp != null) {
            JSONFile = temp;
        }
    }

    @FXML
    private void handleLogOut(ActionEvent event) {
        try {
            Window.openView(model, model.getBorderPane(), Window.View.Login, "center");
        } catch (GUIException ex) {
            Logger.getLogger(SettingsWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
