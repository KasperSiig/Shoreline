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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import shoreline.exceptions.GUIException;
import shoreline.gui.MenuBarFactory;
import shoreline.gui.model.ModelManager;
import shoreline.statics.Window;

/**
 * FXML Controller for Login View
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class LoginWindowController implements Initializable, IController {

    /* Java Variables */
    private ModelManager model;

    /* JavaFX Variables*/
    @FXML
    private BorderPane bPane;
    @FXML
    private Label lblError;

    /* JFoenix Variables */
    @FXML
    private JFXTextField txtUserName;
    @FXML
    private JFXPasswordField txtPassword;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @Override
    public void postInit(ModelManager model) {
        this.model = model;
    }

    /**
     * Opens the window to create new user
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
     * Validates the information given in the TextFields and if login is valid
     * logs the user in. Otherwise it shows an error message.
     *
     * @param event
     */
    @FXML
    private void loginValidation(ActionEvent event) {
        try {
            if (model.getUserModel().validatePassword(txtUserName.getText(), txtPassword.getText())) {
                model.getUserModel().setUser(model.getUserModel().getUserOnLogin(txtUserName.getText(), txtPassword.getText()));
                Window.openView(model, model.getBorderPane(), Window.View.Mapping, "center");
                model.getLogModel().add(model.getUserModel().getUser().getId(), Alert.AlertType.INFORMATION, model.getUserModel().getUser().getfName() + " has logged in");
            } else {
                lblError.setText("there was a problem with the log in");
                txtUserName.requestFocus();
            }
        } catch (GUIException ex) {
            Window.openExceptionWindow("There was a problem validating your login", ex.getStackTrace());
        }

    }

}
