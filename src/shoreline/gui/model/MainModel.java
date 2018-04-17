package shoreline.gui.model;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.BorderPane;
import shoreline.bll.LogicManager;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.GUIException;

/**
 *
 * @author
 */
public class MainModel {

    private BorderPane borderPane;
    private LogicManager logic;

    public MainModel() throws GUIException {
        try {
            this.logic = new LogicManager();
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    public boolean isDarkTheme() throws GUIException {
        try {
            return logic.getProperty("darkTheme").equals("1");
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }

    public void setProperty(String key, String string) throws GUIException {
        try {
            logic.setProperty(key, string);
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }
    
    public String getProperty(String key) throws GUIException {
        try {
            return logic.getProperty(key);
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }
    

    public void toggleDarkTheme() throws GUIException {
        try {
            String newValue = logic.getProperty("darkTheme").equals("1") ? "0" : "1";
            logic.setProperty("darkTheme", newValue);
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }

}
