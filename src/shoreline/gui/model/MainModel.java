package shoreline.gui.model;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import shoreline.be.Config;
import shoreline.be.ConvTask;
import shoreline.be.User;
import shoreline.be.LogItem;
import shoreline.bll.LogicManager;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.GUIException;
import shoreline.statics.Window;

/**
 * Collects all information about User Interface, and contains UI Logic
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class MainModel {

    private BorderPane borderPane;

    private LogicManager logic;

    // Observable Lists
    private ObservableList<String> templateList;
    private ObservableList<LogItem> logList;
    private ObservableList<ConvTask> pendingTasks;
    private ObservableList<ConvTask> finishedTasks;
    private ObservableList<ConvTask> canceledTasks;
    private ObservableList<Config> configList;

    /**
     * Constructor for MainModel
     *
     * @throws GUIException
     */

    User user;


    public MainModel() throws GUIException {
        try {
            
            this.logic = new LogicManager();
            this.pendingTasks = FXCollections.observableArrayList();
            this.finishedTasks = FXCollections.observableArrayList();
            this.canceledTasks = FXCollections.observableArrayList();
            this.logList = FXCollections.observableArrayList(getAllLogs());
            this.templateList = FXCollections.observableArrayList("siteName", "assetSerialNumber", 
                    "type", "externalWorkOrderId", "systemStatus", "userStatus", "name", "priority", 
                    "latestFinishDate", "earliestStartDate", "latestStartDate", "estimatedTime");
            this.configList = FXCollections.observableArrayList(getAllConfigs());
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return BorderPane saved in MainModel
     */
    public BorderPane getBorderPane() {
        return borderPane;
    }

    /**
     * Sets the BorderPane in model
     *
     * @param borderPane BorderPane to be saved
     */
    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    /**
     * Writes a property to the config.properties file
     *
     * @param key Key for the property
     * @param string Value for the property
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
     * Gets a property from the config.properties
     *
     * @param key Key from the property
     * @return Property based on key given
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
     * Hashes password and parses data through to BLL
     *
     * @param username Username entered
     * @param password Password entered
     * @param firstname First Name entered
     * @param lastname Last Name entered
     * @return Boolean whether the user was successfully created or not
     * @throws GUIException
     */
    public int createUser(String username, String password, String firstname, String lastname) throws GUIException {
        try {
            return logic.createUser(username, hashString(password), firstname, lastname);
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }

    /**
     * Validates if the password is correct.
     *
     * @param username Username entered
     * @param pass Password entered
     * @return Boolean whether the user is valid
     * @throws GUIException
     */

    public User getUser(String username, String password) throws GUIException {
        try {
            return logic.getUser(username, hashString(password).toString());
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }


    public boolean validateLogin(String username, String pass) throws GUIException {
        try {
            return logic.validateLogin(username, hashString(pass));
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }

    /**
     * Hashes a given string and returns the hashed string
     *
     * @param string String to be hashed
     * @return StringBuffer containing hashed string
     * @throws GUIException
     */
    private String hashString(String string) throws GUIException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(string.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {

                String hex = Integer.toHexString(0xff & hash[i]);

                if (hex.length() == 1) {
                    hexString.append('0');
                }

                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            throw new GUIException(ex);
        }
    }

    /**
     * @param file File to get titles from
     * @return Titles from a given file
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
     * @return Gets the titles from the given template
     */
    public ObservableList<String> getTemplateList() {
        return templateList;
    }

    /**
     * Adds a task to the list of tasks
     *
     * @param task Task to be added
     */
    public void addToPendingTasks(ConvTask task) {
        pendingTasks.add(task);
    }
    
    /**
     * Removes task from List of pending tasks
     * 
     * @param task Task to be removed
     */
    public void removeFromPendingTasks(ConvTask task) {
        pendingTasks.remove(task);
    }

    /**
     * @return ObersableList containing ConvTasks
     */
//    public ObservableList<ConvTask> getTaskList() {
//        return pendingTasks;
//    }

    public ObservableList<ConvTask> getPendingTasks() {
        return pendingTasks;
    }

    /**
     * Adds a task to the list of tasks
     *
     * @param task Task to be added
     */
    public void addToFinishedTasks(ConvTask task) {
        finishedTasks.add(task);
        System.out.println("shoreline.gui.model.MainModel.addToFinishedTasks()");
        System.out.println("finishedTasks = " + finishedTasks.size() + "\n");
    }
    
    /**
     * Removes task from List of pending tasks
     * 
     * @param task Task to be removed
     */
    public void removeFromFinishedTasks(ConvTask task) {
        finishedTasks.remove(task);
    }

    /**
     * @return ObersableList containing ConvTasks
     */
    public ObservableList<ConvTask> getFinishedTasks() {
        return finishedTasks;
    }

    /**
     * Adds a task to the list of tasks
     *
     * @param task Task to be added
     */
    public void addToCanceledTasks(ConvTask task) {
        canceledTasks.add(task);
    }
    
    /**
     * Removes task from List of canceled tasks
     * 
     * @param task Task to be removed
     */
    public void removeFromCanceledTasks(ConvTask task) {
        canceledTasks.remove(task);
    }

    /**
     * @return ObersableList containing ConvTasks
     */
    public ObservableList<ConvTask> getCanceledTasks() {
        return canceledTasks;
    }

    /**
     * Starts a task in ThreadPool
     *
     * @param task Task to be converted
     */
    public void startTask(ConvTask task) {
        logic.startTask(task);
    }

    /**
     * Adds the conversion task to the ConvTask, making it ready for conversion
     *
     * @param task Task to get ready
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
     * @return ObservableList containing LogItems
     */
    public ObservableList<LogItem> getLogList() {
        return logList;
    }

    /**
     * @return List of configurations
     */
    public ObservableList<Config> getConfigList() {
        return configList;
    }

    /**
     * Adds a configuration to the configuration list
     *
     * @param config Configuration to be added
     */
    public void addToConfigList(Config config) {
        try {
            this.configList.add(config);
            saveConfig(config.getName(), config.getExtension(), config.getMap());
        } catch (BLLException ex) {
            Logger.getLogger(MainModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /**
     * Adds log to database
     * 
     * @param userId The user who made an action
     * @param type Type of log to be logged
     * @param message Log message
     * @throws GUIException
     */
    public void addLog(int userId, Alert.AlertType type, String message) throws GUIException {
        try {
            logic.addLog(userId, type, message);
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }

    public List<LogItem> getAllLogs() throws GUIException {
        try {
            startLogTimer();
            return logic.getAllLogs();
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }
    
    /**
     * Starts timer to get new logs
     *
     * @throws GUIException
     */
    private void startLogTimer() throws GUIException {
        logic.logTimer();
        logicLogListener();
    }

    /**
     * @return New LogItems
     * @throws BLLException 
     */
    public List<LogItem> getNewLogs() throws BLLException {
        return logic.getNewLogs();
    }

    /**
     * @return List of all Configurations
     * @throws BLLException 
     */
    public List<Config> getAllConfigs() throws BLLException {
        return logic.getAllConfigs();
    }

    /**
     * Save new configruation
     * 
     * @param name Name of configuration
     * @param extension Extension of the file, fit to config
     * @param map Mapping of the configuration
     * @throws BLLException 
     */
    public void saveConfig(String name, String extension, HashMap map) throws BLLException {
        logic.saveConfig(name, extension, map);
    }

    /**
     * Gets the timer from logic layer
     * @return 
     */
    public Timer getTimer() {
        return logic.getTimer();
    }


    /**
     * Adds listener to Log List in BLL
     */
    private void logicLogListener() {
        logic.getTempLog().addListener((ListChangeListener.Change<? extends LogItem> c) -> {
            c.next();
            if (c.wasAdded() || c.wasRemoved() || c.wasReplaced() || c.wasUpdated()) {
                logList.addAll(logic.getTempLog());
            }
        });
    }
}
