package shoreline.gui.controller;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import shoreline.exceptions.GUIException;
import shoreline.gui.MenuBarFactory;
import shoreline.gui.model.ModelManager;
import shoreline.statics.Window;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class RootWindowController implements Initializable, IController {

    private ModelManager model;
    @FXML
    private BorderPane borderPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            model = new ModelManager();
            model.setBorderPane(borderPane);
            Window.openView(model, borderPane, Window.View.Login, "center");
        } catch (GUIException ex) {
            Window.openExceptionWindow(ex.getMessage());
        }

    }

    @Override
    public void postInit(ModelManager model) {
        this.model = model;
        this.borderPane = model.getBorderPane();

    }

    /**
     * Mouse event that opens the Shoreline webpage
     *
     * @param event
     */
    private void openShorlineLInk(MouseEvent event) {
        try {
            Desktop.getDesktop().browse(new URI("http://www.shoreline.no/home"));
        } catch (URISyntaxException | IOException ex) {
            Window.openExceptionWindow("There was a problem loading the webpage", ex.getStackTrace());
        }
    }

}
