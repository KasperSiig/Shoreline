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
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            model = new MainModel();
            model.setBorderPane(borderPane);
            Window.openView(model, borderPane, Window.View.CreateUser, "center", MenuBarFactory.MenuType.Default);
            
        } catch (GUIException ex) {
            Window.openExceptionWindow("Something went wrong...");
        }
    }    

    @Override
    public void postInit(MainModel model) {
        this.model = model;
        this.borderPane = model.getBorderPane();
    }
    
}
