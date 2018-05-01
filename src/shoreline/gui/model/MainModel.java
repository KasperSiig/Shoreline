package shoreline.gui.model;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import shoreline.be.Config;
import shoreline.be.ConvTask;
import shoreline.be.logItem;
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
    private ObservableList<logItem> logList;
    private List<ConvTask> taskList;
    private ObservableList<Config> configList;
    
    public MainModel() throws GUIException {
        taskList = new ArrayList();
        configList = FXCollections.observableArrayList();
        this.logList = FXCollections.observableArrayList();
        this.templateList = FXCollections.observableArrayList("siteName", "assetSerialNumber", "type", "externalWorkOrderId", "systemStatus", "userStatus", "name", "priority", "latestFinishDate", "earliestStartDate", "latestStartDate", "estimatedTime");
        try {
            this.logic = new LogicManager();
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
        temp();
    }

    /**
     * Returns the borderpane saved in the model
     * 
     * @return 
     */
    public BorderPane getBorderPane() {
        return borderPane;
    }

    /**
     * Sets the borderpane in model
     * 
     * @param borderPane 
     */
    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    /**
     * Writes a property to the config.property file
     * 
     * @param key
     * @param string
     * @throws GUIException 
     */
    public void setProperty(String key, String string) throws GUIException {
        try {
            logic.setProperty(key, string);
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }
    
    /**
     * Gets a property from the config.property
     * 
     * @param key
     * @return
     * @throws GUIException 
     */
    public String getProperty(String key) throws GUIException {
        try {
            return logic.getProperty(key);
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }
    /**
     * Hashes password and parse data through to BLL
     * 
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
    
    /**
     * Hashes a given string and returns the hashed string
     * 
     * @param pass
     * @return
     * @throws GUIException 
     */
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
    
    /**
     * Return a hashmap of the titles from a file
     * 
     * @param file
     * @return
     * @throws GUIException 
     */
    public HashMap<String, Integer> getTitles(File file) throws GUIException {
        try {
            return logic.getTitles(file);
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }

    /**
     * Return the observablelist of the template
     * 
     * @return 
     */
    public ObservableList<String> getTemplateList() {
        return templateList;
    }
    
    /**
     * Adds a task to the list of tasks
     * 
     * @param task 
     */
    public void addToTaskList(ConvTask task) {
        taskList.add(task);
    }
    
    /**
     * Removes a task from the list of tasks
     * 
     * @param task 
     */
    private void removeTaskFromList(ConvTask task) {
        taskList.remove(task);
    }
    
    /**
     * returns the list of tasks
     * 
     * @return 
     */
    public List<ConvTask> getTaskList() {
        return taskList;
    }
    
    /**
     * Starts a task
     * 
     * @param task 
     */
    public void startTask(ConvTask task) {
        logic.startTask(task);
    }
    
    /**
     * Adds a callable to a task
     * 
     * @param task
     * @throws GUIException 
     */
    public void addCallableToTask(ConvTask task) throws GUIException {
        try {
            logic.addCallableToTask(task);
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }

    /**
     * Returns the observable list of logitems
     * 
     * @return 
     */
    public ObservableList<logItem> getLogList() {
        return logList;
    }

    /**
     * Adds a logitem to the list of logitems
     * 
     * @param item 
     */
    public void addToLogList(logItem item) {
   
        logList.add(item);
    }

    /**
     * Returns the config list
     * 
     * @return 
     */
    public ObservableList<Config> getConfigList() {
        return configList;
    }

    /**
     * adds a hashmap to the config list
     * 
     * @param config 
     */
    public void addToConfigList(Config config) {
        this.configList.add(config);
    }
    
    
    /**
     * 
     * To be removed
     * 
     */
    private void temp() {
        addToLogList(new logItem(Alert.AlertType.ERROR, "some error happend", "bo", new Date(2018, 4, 30)));
        addToLogList(new logItem(Alert.AlertType.WARNING, "some warning happend", "ib", new Date(2018, 4, 30)));
        addToLogList(new logItem(Alert.AlertType.CONFIRMATION, "Some confirmation happend", "dorte", new Date(2018, 4, 30)));
        addToLogList(new logItem(Alert.AlertType.INFORMATION, "Some information happend", "Elisabeth", new Date(2018, 4, 30)));
        addToLogList(new logItem(Alert.AlertType.NONE, "nothing happend", "Carl", new Date(2018, 4, 30)));
        addToLogList(new logItem(Alert.AlertType.NONE, "nothing happend", "Carl", new Date(2018, 4, 30)));
        addToLogList(new logItem(Alert.AlertType.NONE, "nothing happend", "Carl", new Date(2018, 4, 30)));
        addToLogList(new logItem(Alert.AlertType.NONE, "nothing happend", "Carl", new Date(2018, 4, 30)));
        addToLogList(new logItem(Alert.AlertType.NONE, "nothing happend", "Carl", new Date(2018, 4, 30)));
        addToLogList(new logItem(Alert.AlertType.NONE, "nothing happend", "Carl", new Date(2018, 4, 30)));
        addToLogList(new logItem(Alert.AlertType.NONE, "nothing happend", "Carl", new Date(2018, 4, 30)));
        addToLogList(new logItem(Alert.AlertType.NONE, "nothing happend", "Carl", new Date(2018, 4, 30)));
        addToLogList(new logItem(Alert.AlertType.NONE, "nothing happend", "Carl", new Date(2018, 4, 30)));
        addToLogList(new logItem(Alert.AlertType.NONE, "nothing happend", "Carl", new Date(2018, 4, 30)));
    }
}
