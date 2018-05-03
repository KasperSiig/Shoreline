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
import javafx.scene.layout.BorderPane;
import shoreline.be.Config;
import shoreline.be.ConvTask;
import shoreline.be.LogItem;
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
    private ObservableList<LogItem> logList;
    private ObservableList<ConvTask> taskList;
    private ObservableList<Config> configList;

    public MainModel() throws GUIException {
        try {
            this.logic = new LogicManager();
            this.taskList = FXCollections.observableArrayList();
            this.logList = FXCollections.observableArrayList(getAllLogs());
            this.templateList = FXCollections.observableArrayList("siteName", "assetSerialNumber", "type", "externalWorkOrderId", "systemStatus", "userStatus", "name", "priority", "latestFinishDate", "earliestStartDate", "latestStartDate", "estimatedTime");
            configList = FXCollections.observableArrayList(getAllConfigs());
            getLatestLog();
            logicLogListener();
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
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
    public boolean createUser(String username, String password, String firstname, String lastname) throws GUIException {
        StringBuffer hexString = hashString(password);
        password = hexString.toString();
        try {
            return logic.createUser(username, password, firstname, lastname);
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }

    public boolean validateLogin(String username, String pass) throws GUIException {
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
        for (ConvTask convTask : taskList) {
            System.out.println(convTask.getMapper());
        }
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
    public ObservableList<ConvTask> getTaskList() {
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
    public ObservableList<LogItem> getLogList() {
        return logList;
    }

    private void getLatestLog() throws GUIException {
        logic.logTimer();
    }
    
    
    private void addToLogList(LogItem item) {
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
        try {
            this.configList.add(config);
            saveConfig(config.getName(), config.getExtension(), config.getMap());
        } catch (BLLException ex) {
            Logger.getLogger(MainModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addLog(int userId, String type, String message) throws BLLException {
        logic.addLog(userId, type, message);
    }

    public List<LogItem> getAllLogs() throws BLLException {
        return logic.getAllLogs();
    }

    public List<LogItem> getNewLogs() throws BLLException {
        return logic.getNewLogs();
    }

    public List<Config> getAllConfigs() throws BLLException {
        return logic.getAllConfigs();
    }

    public void saveConfig(String name, String extension, HashMap map) throws BLLException {
        logic.saveConfig(name, extension, map);
    }

    public Timer getTimer() {
        return logic.getTimer();
    }
    
    private void logicLogListener() {
        logic.getTempLog().addListener(new ListChangeListener<LogItem>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends LogItem> c) {
                c.next();
                if (c.wasAdded() || c.wasRemoved() || c.wasReplaced() || c.wasUpdated()) {
                    logList.addAll(logic.getTempLog());
                    System.out.println("logic list change");
                }
            }
        });
    }
    
    
    
}
