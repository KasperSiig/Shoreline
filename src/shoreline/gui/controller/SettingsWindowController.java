/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.gui.controller;

import com.jfoenix.controls.JFXButton;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
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

    @FXML
    private JFXButton jsonBtn;
    @FXML
    private BorderPane borderPane;
    @FXML
    private JFXButton logOutBtn;

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
    private void handleLogOut(ActionEvent event) {
        try {
            Window.openView(model, model.getBorderPane(), Window.View.Login, "center");
        } catch (GUIException ex) {
            Logger.getLogger(SettingsWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleOpenJSONSetting(ActionEvent event) {
        try {
            Window.openView(model, model.getBorderPane(), Window.View.JSONTemplate, "center");
        } catch (GUIException ex) {
            Logger.getLogger(SettingsWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
