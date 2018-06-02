package shoreline.gui.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
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
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        try {
            model.getConfigModel().deleteConfig(config);
            curVBox.getChildren().remove(this);
        } catch (GUIException ex) {
            Logger.getLogger(ConfigView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void handleEdit(ActionEvent event) {
        try {
            ConfigWindowController cwc = (ConfigWindowController) Window.openView(model, model.getBorderPane(), Window.View.Config, "center");
            cwc.setInfo(config);
        } catch (GUIException ex) {
            Logger.getLogger(ConfigView.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
