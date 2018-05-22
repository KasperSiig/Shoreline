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
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import shoreline.be.User;
import shoreline.exceptions.GUIException;
import shoreline.gui.model.ModelManager;
import shoreline.statics.Window;

/**
 * FXML Controller class
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class CreateUserWindowController implements Initializable, IController {

    private ModelManager model;

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

    /**
     * Creates a new user login
     *
     * @param event
     */
    @FXML
    private void onCreateAction(ActionEvent event) throws InterruptedException {
        if (checkEmptyFields()) {
            try {
                User user = new User(txtLastname.getText(), txtFirstname.getText(), txtUsername.getText(), 0);
                user = model.getUserModel().create(user, txtPassword.getText());
                Window.openView(model, model.getBorderPane(), Window.View.Login, "center");
                Window.openSnack("User " + txtUsername.getText() + " was created", model.getBorderPane(), "blue");
                model.getLogModel().add(user, Alert.AlertType.INFORMATION, "A new user was created with ID: " + user.getId());
            } catch (GUIException ex) {
                Window.openExceptionWindow("Error creating user", ex.getStackTrace());
            }
        }

    }

    /**
     * Returns to the login screen.
     *
     * @param event
     */
    @FXML
    private void onCancelAction(ActionEvent event) {
        try {
            Window.openView(model, model.getBorderPane(), Window.View.Login, "center");
        } catch (GUIException ex) {
            Window.openExceptionWindow("Could not load Login screen", ex.getStackTrace());
        }
    }

    /**
     * Loads data after initialization
     *
     * @param model
     */
    @Override
    public void postInit(ModelManager model) {
        this.model = model;
    }

    /**
     * Checks if any fields are empty. If there is any it returns false, else,
     * it returns true.
     *
     * @return
     */
    private boolean checkEmptyFields() {
        if (txtFirstname.getText().isEmpty()) {
            lblError.setText("Please enter a firstname!");
            txtFirstname.requestFocus();
            return false;
        } else if (txtLastname.getText().isEmpty()) {
            lblError.setText("Please enter a lastname!");
            txtLastname.requestFocus();
            return false;
        } else if (txtUsername.getText().isEmpty()) {
            lblError.setText("Please enter a username!");
            txtUsername.requestFocus();
            return false;
        } else if (txtPassword.getText().isEmpty()) {
            lblError.setText("Please enter a password!");
            txtPassword.requestFocus();
            return false;
        } else {
            return true;
        }
    }
}
