package shoreline.gui.controller;

import com.jfoenix.controls.JFXToggleButton;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import shoreline.exceptions.GUIException;
import shoreline.gui.MenuBarFactory;
import shoreline.gui.model.MainModel;
import shoreline.statics.Window;

/**
 *
 * @author
 */
public class RootWindowController implements Initializable, IController {
    
    private MainModel model;
    @FXML
    private BorderPane borderPane;
    @FXML
    private AnchorPane anchMenu;
    @FXML
    private JFXToggleButton tglTheme;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            model = new MainModel();
            if (model.getProperty("darkTheme").equals("1")) {
                tglTheme.fire();
                toggleTheme(new ActionEvent());
            }
            model.setBorderPane(borderPane);
            Window.openView(model, borderPane, Window.View.Main, "center", MenuBarFactory.MenuType.Default);
            
        } catch (GUIException ex) {
            Window.openExceptionWindow("Something went wrong...");
        }
    }    

    @Override
    public void postInit(MainModel model) {
        this.model = model;
        this.borderPane = model.getBorderPane();
    }

    @FXML
    private void toggleTheme(ActionEvent event) {
        try {
            if (model.isDarkTheme()) {
                borderPane.getStylesheets().clear();
                borderPane.getStylesheets().add("/shoreline/res/LightTheme.css");
                try {
                    model.toggleDarkTheme();
                } catch (GUIException ex) {
                    Logger.getLogger(RootWindowController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                borderPane.getStylesheets().clear();
                borderPane.getStylesheets().add("/shoreline/res/DarkTheme.css");
                try {
                    model.toggleDarkTheme();
                } catch (GUIException ex) {
                    Logger.getLogger(RootWindowController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (GUIException ex) {
            Window.openExceptionWindow("Something went wrong...");
        }
    }
    
}
