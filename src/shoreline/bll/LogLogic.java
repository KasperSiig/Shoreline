package shoreline.bll;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import shoreline.be.LogItem;
import shoreline.be.User;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class LogLogic {

    private LogicManager logicManager;

    private ObservableList<LogItem> tempLog;
    private Timer logTimer;

    /**
     * Constructor for LogLogic
     *
     * @param logicManager Reference back to the LogicManager
     */
    public LogLogic(LogicManager logicManager) {
        this.tempLog = FXCollections.observableArrayList();
        this.logicManager = logicManager;
    }

    /**
     * Adds log to the database
     *
     * @param user User who performed an action
     * @param type Type of action
     * @param message Message to be written in log
     * @throws BLLException
     */
    public void addLog(User user, Alert.AlertType type, String message) throws BLLException {
        try {
            logicManager.getDataManager().addLog(user, type, message);
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
            return logicManager.getDataManager().getExistingLogs();
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
                    tempLog.addAll(getNewLogs());
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
    public List<LogItem> getNewLogs() throws BLLException {
        try {
            return logicManager.getDataManager().getNewLogs();
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
