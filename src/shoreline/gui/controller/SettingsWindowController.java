package shoreline.gui.controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import shoreline.exceptions.GUIException;
import shoreline.gui.model.ModelManager;
import shoreline.statics.Window;

/**
 * FXML Controller class
 *
 * @author madst
 */
public class SettingsWindowController implements Initializable, IController {

    private ModelManager model;

    @FXML
    private JFXButton jsonBtn;
    @FXML
    private BorderPane borderPane;
    @FXML
    private JFXButton logOutBtn;
    @FXML
    private JFXButton bgnConfig;
    @FXML
    private JFXButton bgnUsers;

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
    }

    @FXML
    private void handleLogOut(ActionEvent event) {
        try {
            Window.openView(model, model.getBorderPane(), Window.View.Login, "center");
        } catch (GUIException ex) {
            Logger.getLogger(SettingsWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleOpenJSONSetting(ActionEvent event) {
        try {
            Window.openView(model, model.getBorderPane(), Window.View.JSONTemplateSetting, "center");
        } catch (GUIException ex) {
            Logger.getLogger(SettingsWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleOpenConfigSetting(ActionEvent event) {
        try {
            Window.openView(model, model.getBorderPane(), Window.View.ConfigSetting, "center");
        } catch (GUIException ex) {
            Logger.getLogger(SettingsWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleOpenUsersSetting(ActionEvent event) {
        try {
            Window.openView(model, model.getBorderPane(), Window.View.UsersSetting, "center");
        } catch (GUIException ex) {
            Logger.getLogger(SettingsWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
