package shoreline.bll;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import shoreline.be.LogItem;
import shoreline.be.User;
import shoreline.dal.DataManager;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class LogLogic extends LogicClass {
    
    private ObservableList<LogItem> tempLog;
    private Timer logTimer;

    /**
     * Constructor for LogLogic
     *
     * @param dataManager Holds a reference to DataManager
     */
    public LogLogic(DataManager dataManager) {
        super(dataManager);
        this.tempLog = FXCollections.observableArrayList();
    }

    /**
     * Adds log to the database
     *
     * @param user User who performed an action
     * @param type Type of action
     * @param message Message to be written in log
     * @throws BLLException
     */
    public void add(User user, Alert.AlertType type, String message) throws BLLException {
        try {
            dataManager.addLog(user, type, message);
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }

    /**
     * Gets all logs from database
     *
     * @return
     * @throws BLLException
     */
    public List<LogItem> getAll() throws BLLException {
        try {
            return dataManager.getExistingLogs();
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }

    /**
     * Starts a timer, looking for new logs
     */
    public void startLogTimer() {
        logTimer = new Timer();
        logTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                tempLog.clear();
                try {
                    tempLog.addAll(getNew());
                } catch (BLLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        }, 5000, 5000);
    }

    /**
     * Gets new logs from database
     *
     * @return
     * @throws BLLException
     */
    public List<LogItem> getNew() throws BLLException {
        try {
            return dataManager.getNewLogs();
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }

    /**
     * @return List of temporary logs
     */
    public ObservableList<LogItem> getTempLog() {
        return tempLog;
    }

    /**
     * @return LogTimer
     */
    public Timer getLogTimer() {
        return logTimer;
    }
}
