package shoreline.gui.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import shoreline.bll.ThreadPool;
import shoreline.exceptions.GUIException;
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
        onCloseRequest();
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
                Window.openView(model, model.getBorderPane(), Window.View.Config, "center");
            } else {
                lblError.setText("Incorrect credentials. Please try again.");
                txtUserName.requestFocus();
            }
        } catch (GUIException ex) {
            Window.openExceptionWindow("There was a problem validating your login", ex.getStackTrace());
        }

    }

    /**
     * Makes a onCloseRequest event closes the thread pool and stops the timer.
     *
     */
    private void onCloseRequest() {
        Platform.runLater(() -> {
            model.getBorderPane().getScene().getWindow().setOnCloseRequest((event) -> {
                ThreadPool tPool = ThreadPool.getInstance();
                tPool.closeThreadPool();
                model.getLogModel().getTimer().cancel();
            });
        });
    }

}
