/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.bll;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import shoreline.be.Config;
import shoreline.be.ConvTask;
import shoreline.be.LogItem;
import shoreline.be.User;
import shoreline.dal.DataManager;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kasper Siig
 */
public class LogicManager {

    private ObservableList<LogItem> tempLog = FXCollections.observableArrayList();
    private Timer t;

    private DataManager dm;

    public LogicManager() throws BLLException {
        try {
            this.dm = new DataManager();
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }

    public String getProperty(String key) throws BLLException {
        try {
            return dm.getProperty(key);
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }

    public void setProperty(String key, String input) throws BLLException {
        try {
            dm.setProperty(key, input);
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }

    public boolean validateLogin(String username, String pass) throws BLLException {
        try {
            String hashPass = dm.getPass(username);
            if (hashPass == null) {
                return false;
            }
            return hashPass.equals(pass);
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }

    public int createUser(String username, String password, String firstname, String lastname) throws BLLException {
        try {
            return dm.createUser(username, password, firstname, lastname);
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }
    
    public User getUser(String username, String password) throws BLLException {
        try {
            return dm.getUser(username, password);
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }
    

    public HashMap<String, Integer> getTitles(File file) throws BLLException {
        try {
            return dm.getTitles(file);
        } catch (DALException ex) {
            throw new BLLException("File format not supported.", ex);
        }
    }

    public void startTask(ConvTask task) {
        ThreadPool threadPool = ThreadPool.getInstance();
        System.out.println(threadPool);
        threadPool.startTask(task);
    }

    public void addCallableToTask(ConvTask task) throws BLLException {
        try {
            dm.addCallableToTask(task);
        } catch (DALException ex) {
            throw new BLLException("File format not supported.", ex);
        }
    }

    public void addLog(int userId, Alert.AlertType type, String message) throws BLLException {
        try {
            dm.addLog(userId, type, message);
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }

    public List<LogItem> getAllLogs() throws BLLException {
        try {
            return dm.getAllLogs();
        } catch (Exception ex) {
            throw new BLLException(ex);
        }
    }

    public List<LogItem> getNewLogs() throws BLLException {
        try {
            return dm.getNewLogs();
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }

    public List<Config> getAllConfigs() throws BLLException {
        try {
            return dm.getAllConfigs();
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }

    public void saveConfig(String name, String extension, HashMap map) throws BLLException {
        try {
            dm.saveConfig(name, extension, map);
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }

    public void logTimer() {
        t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    tempLog.clear();
                    tempLog.addAll(getNewLogs());
                    System.out.println("Fetched log");
                } catch (BLLException ex) {
                    Logger.getLogger(LogicManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }, 0, 5000);
    }

    public ObservableList<LogItem> getTempLog() {
        return tempLog;
    }
    
    public Timer getTimer() {
        if (t != null) {
            return t;
        } else {
            return null;
        }
    }
    
}
