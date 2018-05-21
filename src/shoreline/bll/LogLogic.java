/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.bll;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import shoreline.be.LogItem;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kasper Siig
 */
public class LogLogic {
    private LogicManager logicManager;
    
    private ObservableList<LogItem> tempLog = FXCollections.observableArrayList();
    private Timer timer;

    public LogLogic(LogicManager logicManager) {
        this.logicManager = logicManager;
    }
    
    public void addLog(int userId, Alert.AlertType type, String message) throws BLLException {
        try {
            logicManager.getDataManager().addLog(userId, type, message);
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }

    public List<LogItem> getAllLogs() throws BLLException {
        try {
            return logicManager.getDataManager().getAllLogs();
        } catch (Exception ex) {
            throw new BLLException(ex);
        }
    }

    public List<LogItem> getNewLogs() throws BLLException {
        try {
            return logicManager.getDataManager().getNewLogs();
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }
    
    public void logTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                tempLog.clear();
                try {
                    tempLog.addAll(getNewLogs());
                } catch (BLLException ex) {
                    Logger.getLogger(LogicManager.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }, 5000, 5000);
    }
    
    public ObservableList<LogItem> getTempLog() {
        return tempLog;
    }

    public Timer getTimer() {
        return timer;
    }
}
