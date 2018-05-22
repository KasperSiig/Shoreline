package shoreline.gui.model;

import java.util.List;
import java.util.Timer;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import shoreline.be.LogItem;
import shoreline.be.User;
import shoreline.bll.LogicManager;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.GUIException;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class LogModel {

    private BorderPane borderPane;
    private LogicManager logic;

    private ObservableList<LogItem> logList;

    public LogModel(BorderPane borderPane, LogicManager logic) throws GUIException {
        this.borderPane = borderPane;
        this.logic = logic;

        this.logList = FXCollections.observableArrayList(getAll());
    }

    /**
     * @return ObservableList containing LogItems
     */
    public ObservableList<LogItem> getList() {
        return logList;
    }

    /**
     * Adds log to database
     *
     * @param userId The user who made an action
     * @param type Type of log to be logged
     * @param message Log message
     * @throws GUIException
     */
    public void add(User user, Alert.AlertType type, String message) throws GUIException {
        try {
            logic.getLogLogic().addLog(user, type, message);
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }

    /**
     * Gets all logs from database
     *
     * @return
     * @throws GUIException
     */
    public List<LogItem> getAll() throws GUIException {
        try {
            startTimer();
            return logic.getLogLogic().getAllLogs();
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }

    /**
     * Starts timer to get new logs
     *
     * @throws GUIException
     */
    private void startTimer() throws GUIException {
        logic.getLogLogic().startLogTimer();
        addLogListener();
    }

    /**
     * @return New LogItems
     * @throws BLLException
     */
    public List<LogItem> getNew() throws BLLException {
        return logic.getLogLogic().getNewLogs();
    }

    /**
     * Gets the timer from logic layer
     *
     * @return
     */
    public Timer getTimer() {
        return logic.getLogLogic().getLogTimer();
    }

    /**
     * Adds listener to Log List in BLL
     */
    private void addLogListener() {
        logic.getLogLogic().getTempLog().addListener((ListChangeListener.Change<? extends LogItem> c) -> {
            c.next();
            if (c.wasAdded() || c.wasRemoved() || c.wasReplaced() || c.wasUpdated()) {
                logList.addAll(logic.getLogLogic().getTempLog());
            }
        });
    }
}
