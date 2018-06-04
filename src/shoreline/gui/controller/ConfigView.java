package shoreline.gui.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import shoreline.be.Config;
import shoreline.exceptions.GUIException;
import shoreline.gui.model.ModelManager;
import shoreline.statics.Window;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class ConfigView extends BorderPane {

    private ModelManager model;
    private Config config;
    private VBox curVBox;

    @FXML
    private Label lblName;
    @FXML
    private Label lblExt;
    @FXML
    private AnchorPane aPane;

    public ConfigView(Config config) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/shoreline/gui/view/ConfigView.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
            this.config = config;
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public void postInit(ModelManager model) {
        this.model = model;

    }

    /**
     * Sets the visual info in the BatchView
     *
     * @param config
     * @param vbox
     */
    public void setInfo(Config config, VBox vbox) {
        lblName.setText("Name: " + config.getName());
        lblExt.setText("Extension: " + config.getExtension().toUpperCase());
        this.curVBox = vbox;
        if (!config.isValid()) {
            aPane.setStyle("-fx-border-width: 0 0 0 6px; -fx-border-color: red; -fx-border-height: 10px; -fx-padding: 0 0 0 4px;");
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        try {
            if (openConfirmWindow("Are you sure you want to delete this config?")) {
                model.getConfigModel().deleteConfig(config);
                curVBox.getChildren().remove(this);
            }
        } catch (GUIException ex) {
            Logger.getLogger(ConfigView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleEdit(ActionEvent event) {
        try {
            SingleTaskWindowController stwc = (SingleTaskWindowController) Window.openView(model, model.getBorderPane(), Window.View.SingleTask, "center");
            stwc.setTabSelected(2, config);
        } catch (GUIException ex) {
            Logger.getLogger(ConfigView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean openConfirmWindow(String message) {
        boolean yes = false;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(Window.View.Confirm.getView()));
            Parent root = fxmlLoader.load();

            ConfirmationWindowController cwc = fxmlLoader.getController();
            cwc.postInit(model);
            cwc.setInfo(message);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Confirmation");
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.showAndWait();
            yes = cwc.getConfirmation();
        } catch (IOException ex) {
            Window.openExceptionWindow("Couldn't open confirmation window.");
        }
        return yes;
    }

}
