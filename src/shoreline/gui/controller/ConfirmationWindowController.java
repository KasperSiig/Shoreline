/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.gui.controller;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import shoreline.be.Config;
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

    @FXML
    private JFXTextField txtInput;
    @FXML
    private Label lblInfo;

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
    }

    @FXML
    private void handleYes(ActionEvent event) {
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
            stage = (Stage) lblInfo.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void handleNo(ActionEvent event) {
        stage = (Stage) lblInfo.getScene().getWindow();
        stage.close();
    }
}
