/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.gui.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import shoreline.exceptions.GUIException;
import shoreline.gui.model.MainModel;
import shoreline.statics.Window;

/**
 * FXML Controller class
 *
 * @author kenne
 */
public class CreateUserWindowController implements Initializable, IController {

    MainModel model;

    @FXML
    private JFXTextField txtFirstname;
    @FXML
    private JFXTextField txtLastname;
    @FXML
    private JFXTextField txtUsername;
    @FXML
    private JFXPasswordField txtPassword;
    @FXML
    private Label lblError;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void onCreateAction(ActionEvent event) {
        if (txtFirstname.getText().isEmpty()) {
            lblError.setText("Please enter a firstname!");
            txtFirstname.requestFocus();
        } else if (txtLastname.getText().isEmpty()) {
            lblError.setText("Please enter a lastname!");
            txtLastname.requestFocus();
        } else if (txtUsername.getText().isEmpty()) {
            lblError.setText("Please enter a username!");
            txtUsername.requestFocus();
        } else if (txtPassword.getText().isEmpty()) {
            lblError.setText("Please enter a password!");
            txtPassword.requestFocus();
        } else {
            try {
                model.createUser(txtUsername.getText(), txtPassword.getText(), txtFirstname.getText(), txtLastname.getText());
                Window.openView(model, model.getBorderPane(), Window.View.Login, "center");

            } catch (GUIException ex) {
                Window.openExceptionWindow("Error creating user", ex.getStackTrace());
            }
        }

    }

    @FXML
    private void onCancelAction(ActionEvent event) {
        try {
            Window.openView(model, model.getBorderPane(), Window.View.Login, "center");
        } catch (GUIException ex) {
            Window.openExceptionWindow("Could not load Login screen", ex.getStackTrace());
        }
    }

    @Override
    public void postInit(MainModel model) {
        this.model = model;
    }

}
