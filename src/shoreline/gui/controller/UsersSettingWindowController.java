/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.gui.controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import shoreline.be.User;
import shoreline.exceptions.GUIException;
import shoreline.gui.model.ModelManager;
import shoreline.statics.Window;

/**
 * FXML Controller class
 *
 * @author kaspe
 */
public class UsersSettingWindowController implements Initializable, IController {

    @FXML
    private BorderPane borderPane;
    @FXML
    private JFXButton btnBack;

    private ModelManager model;
    @FXML
    private VBox vBoxUsers;

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
        List<User> users = new ArrayList();
        try {
            users = model.getUserModel().getAllUsers();
        } catch (GUIException ex) {
            Logger.getLogger(UsersSettingWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (User user : users) {
            UserView userView = new UserView(user);
            userView.postInit(model);
            userView.setInfo(user, vBoxUsers);
            vBoxUsers.getChildren().add(userView);
        }

    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            SingleTaskWindowController stwc = (SingleTaskWindowController) Window.openView(model, model.getBorderPane(), Window.View.SingleTask, "center");
            stwc.setTabSelected(3);
        } catch (GUIException ex) {
            Window.openExceptionWindow(ex.getMessage());
        }
    }

}
