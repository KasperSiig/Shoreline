package shoreline.gui.model;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private ObservableList<String> templateList;

    public MainModel() throws GUIException {
        this.templateList = FXCollections.observableArrayList("siteName", "assetSerialNumber", "type", "externalWorkOrderId", "systemStatus", "userStatus", "name", "priority", "latestFinishDate", "earliestStartDate", "latestStartDate", "estimatedTime");
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
    /**
     * Hashes password and parse data through to BLL
     * @param username
     * @param password
     * @param firstname
     * @param lastname
     * @return
     * @throws GUIException 
     */
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
    
    public HashMap<String, Integer> getTitles(File file) throws GUIException {
        try {
            return logic.getTitles(file);
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }

    public ObservableList<String> getTemplateList() {
        return templateList;
    }
    
}
