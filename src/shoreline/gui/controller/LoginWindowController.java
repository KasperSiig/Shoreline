/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.gui.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import shoreline.be.LogItem;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.GUIException;
import shoreline.gui.model.MainModel;
import shoreline.statics.Window;

/**
 * FXML Controller class
 *
 * @author madst
 */
public class LoginWindowController implements Initializable, IController {

    MainModel model;
    @FXML
    private BorderPane bPane;
    @FXML
    private JFXTextField txtUserName;
    @FXML
    private JFXPasswordField txtPassword;
    @FXML
    private Label lblError;

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

    /**
     * sout's that the password was forgotten
     *
     * @param event
     */
    @FXML
    private void forgotPassword(MouseEvent event) {
        System.out.println("forgot you're password eh?");
    }

    /**
     * Opens the create new user window
     *
     * @param event
     */
    @FXML
    private void createNewUser(MouseEvent event) {
        try {
            Window.openView(model, model.getBorderPane(), Window.View.CreateUser, "center");
        } catch (GUIException ex) {
            Window.openExceptionWindow("Could not load the create user window", ex.getStackTrace());
        }
    }

    /**
     * Validates the information given in the textfields and if it OK it loads
     * the main window otherwise it set the label to be a text
     *
     * @param event
     */
    @FXML
    private void loginValidation(ActionEvent event) {
        try {
            if (model.validateLogin(txtUserName.getText(), txtPassword.getText())) {
                Window.openView(model, model.getBorderPane(), Window.View.Mapping, "center");
                model.addLog(1, "ERROR", "Hej Mads :-) DIN LUDER");
                model.addLog(1, "ERROR", "Hej Mads :-) DIN LUDER!!!");
                model.addLog(1, "ERROR", "Hej Mads :-) DIN LUDER@@@@@@");
            } else {
                lblError.setText("there was a problem with the log in");
            }
        } catch (GUIException ex) {
            Window.openExceptionWindow("Something went wrong with the login window", ex.getStackTrace());
        } catch (BLLException ex) {
            Logger.getLogger(LoginWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
