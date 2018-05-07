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
    File file;
    private boolean confirmation = false;
    private boolean txtField = false;

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

    public void setInfo(String msg, HashMap map, File file, boolean txtField) {
        this.map = map;
        this.file = file;
        this.txtField = txtField;
        lblInfo.setText(msg);
        if (txtField) {
            txtInput.requestFocus();
        } else {
            txtInput.setDisable(true);
            btnYes.requestFocus();
        }
    }

    @FXML
    private void handleYes(ActionEvent event) {
        if (txtField) {
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
                if (file != null) {
                    String extension = "";

                    int i = file.getAbsolutePath().lastIndexOf('.');
                    if (i > 0) {
                        extension = file.getAbsolutePath().substring(i + 1);
                    }
                    Config config = new Config(name, extension, map);
                    model.addToConfigList(config);
                }
            }
        } else {
            confirmation = true;
            stage = (Stage) lblInfo.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void handleNo(ActionEvent event) {
        confirmation = false;
        stage = (Stage) lblInfo.getScene().getWindow();
        stage.close();
    }

    public boolean getConfirmation() {
        return confirmation;
    }
}
