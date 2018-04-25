package shoreline.gui.model;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    
    public boolean createUser(String username, String password, String firstname, String lastname) throws GUIException{
        StringBuffer hexString = hashString(password);
        password = hexString.toString();
        try {
            return logic.createUser(username,password,firstname,lastname);
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }
    
    public boolean validateLogin(String username, String pass) throws GUIException{

        try {
            StringBuffer hexString = hashString(pass);
            return logic.validateLogin(username, hexString.toString());
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }
    
    private StringBuffer hashString(String pass) throws GUIException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(pass.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                
                String hex = Integer.toHexString(0xff & hash[i]);
                
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                
                hexString.append(hex);
                
            }
            return hexString;
        } catch (NoSuchAlgorithmException ex) {
            throw new GUIException(ex);
        } catch (UnsupportedEncodingException ex) {
            throw new GUIException(ex);
        }
    }
}
