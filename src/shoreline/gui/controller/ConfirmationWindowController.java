/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.gui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import shoreline.be.Config;
import shoreline.exceptions.GUIException;
import shoreline.gui.model.ModelManager;
import shoreline.statics.Window;

/**
 * FXML Controller class
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class ConfirmationWindowController implements Initializable, IController {

    private ModelManager model;
    private HashMap map;
    private Stage stage;
    private File file;
    private boolean confirmation = false;
    private boolean txtField = false;

    @FXML
    private JFXTextField txtInput;
    @FXML
    private Label lblInfo;
    @FXML
    private JFXButton btnYes;
    @FXML
    private BorderPane bPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    /**
     * Runs before the rest of the class
     *
     * @param model
     */
    @Override
    public void postInit(ModelManager model) {
        this.model = model;
    }

    /**
     * Sets the label information. if this window are used to make configs it
     * gets the data it needs to make them.
     *
     * @param msg
     * @param map
     * @param file
     * @param txtField
     */
    public void setInfo(String msg, HashMap map, File file, boolean txtField) {
        this.map = map;
        this.file = file;
        this.txtField = txtField;
        lblInfo.setText(msg);
    }

    /**
     * Sets the label information if the window is used as confirmation it
     * disables the textfield.
     *
     * @param msg
     * @param txtField
     */
    public void setInfo(String msg, boolean txtField) {
        lblInfo.setText(msg);
        btnYes.requestFocus();
    }

    /**
     * Handles the yes button checks if it needs the textfield if it does it
     * asks for a name and creates a config and sets a boolean to true otherwise
     * it just sets the boolean, then closes the window.
     *
     * @param event
     */
    @FXML
    private void handleYes(ActionEvent event) {
        if (txtField) {
            if (txtInput.getText().isEmpty()) {
                Window.openSnack("Please enter config name", bPane, "red");
            } else {
                String name = txtInput.getText();
                for (Config config : model.getConfigModel().getConfigList()) {
                    if (config.getName().equals(name)) {
                        Window.openSnack("The name already exists", bPane, "red");
                        return;
                    }
                }
            }
        } else {
            confirmation = true;
            stage = (Stage) lblInfo.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * Handles the no button sets the boolean to false closes the window.
     *
     *
     * @param event
     */
    @FXML
    private void handleNo(ActionEvent event) {
        confirmation = false;
        stage = (Stage) lblInfo.getScene().getWindow();
        stage.close();
    }

    /**
     * returns the confirmation boolean
     *
     * @return Confirmation
     */
    public boolean getConfirmation() {
        return confirmation;
    }
}
