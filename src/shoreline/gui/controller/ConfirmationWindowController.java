/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.gui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import shoreline.be.Config;
import shoreline.exceptions.GUIException;
import shoreline.gui.model.MainModel;
import shoreline.statics.Window;

/**
 * FXML Controller class
 *
 * @author madst
 */
public class ConfirmationWindowController implements Initializable, IController {

    MainModel model;
    HashMap map;
    Stage stage;
    private boolean confirmation = false;

    @FXML
    private JFXTextField txtInput;
    @FXML
    private Label lblInfo;
    @FXML
    private JFXButton btnYes;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @Override
    public void postInit(MainModel model) {
        this.model = model;
    }

    public void setInfo(String msg, HashMap map) {
        this.map = map;
        lblInfo.setText(msg);
        if (map != null) {
            txtInput.requestFocus();
        } else {
            btnYes.requestFocus();
        }
    }

    @FXML
    private void handleYes(ActionEvent event) {
        if (map != null) {
            if (txtInput.getText().isEmpty()) {
                Window.openExceptionWindow("Please enter config name");
            } else {
                String name = txtInput.getText();
                for (Config config : model.getConfigList()) {
                    if (config.getName().equals(name)) {
                        Window.openExceptionWindow("The name aleardy exists");
                        return;
                    }
                }
                Config config = new Config(name, "xlsx", map);
                model.addToConfigList(config);
                try {
                    model.addLog(model.getUser().getId(), Alert.AlertType.INFORMATION, model.getUser().getfName() + " has created aconfig" + config.getName());
                } catch (GUIException ex) {
                    Window.openExceptionWindow("There was a problem adding a log", ex.getStackTrace());
                }
            }
        } else {
            txtInput.setDisable(true);
            confirmation = true;
            stage = (Stage) lblInfo.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void handleNo(ActionEvent event) {
        stage = (Stage) lblInfo.getScene().getWindow();
        stage.close();
    }

    public boolean getConfirmation() {
        return confirmation;
    }
}
