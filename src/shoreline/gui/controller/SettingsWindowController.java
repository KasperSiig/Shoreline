package shoreline.gui.controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import shoreline.be.Config;
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
    @FXML
    private JFXButton btnDatabase;
    @FXML
    private FlowPane flowPane;
    @FXML
    private AnchorPane apJSON;
    @FXML
    private AnchorPane apUsers;
    @FXML
    private Pane paneWarning;

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
        checkConfigs();
        if (model.getUserModel().getUser().getUserLevel() != 1) {
            flowPane.getChildren().remove(apUsers);
            flowPane.getChildren().remove(apJSON);
        }
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
            Window.openView(model, borderPane, Window.View.JSONTemplateSetting, "center");
        } catch (GUIException ex) {
            Logger.getLogger(SettingsWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleOpenConfigSetting(ActionEvent event) {
        try {
            Window.openView(model, borderPane, Window.View.ConfigSetting, "center");
        } catch (GUIException ex) {
            Logger.getLogger(SettingsWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleOpenUsersSetting(ActionEvent event) {
        try {
            Window.openView(model, borderPane, Window.View.UsersSetting, "center");
        } catch (GUIException ex) {
            Logger.getLogger(SettingsWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleOpenDatabaseSetting(ActionEvent event) {
        try {
            Window.openView(model, borderPane, Window.View.DatabaseSetting, "center");
        } catch (GUIException ex) {
            Logger.getLogger(SettingsWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void checkConfigs() {
        try {
            List<Config> configs = model.getConfigModel().getAllConfigs();
            configs.forEach((config) -> {
                if (!config.isValid()) {
                    paneWarning.setVisible(true);
                }
            });
        } catch (GUIException ex) {
            Logger.getLogger(SettingsWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
