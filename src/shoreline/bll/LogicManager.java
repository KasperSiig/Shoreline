package shoreline.bll;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import shoreline.be.Batch;
import shoreline.be.Config;
import shoreline.be.ConvTask;
import shoreline.be.LogItem;
import shoreline.be.User;
import shoreline.dal.DataManager;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class LogicManager {
    
    private TaskLogic taskLogic;
    private BatchLogic batchLogic;
    private LogLogic logLogic;
    private ConfigLogic configLogic;
    private UserLogic userLogic;

    private DataManager dataManager;

    public LogicManager() throws BLLException {
        this.userLogic = new UserLogic(this);
        this.configLogic = new ConfigLogic(this);
        this.logLogic = new LogLogic(this);
        this.batchLogic = new BatchLogic(this);
        this.taskLogic = new TaskLogic(this);
        try {
            this.dataManager = new DataManager();
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }

    public DataManager getDataManager() {
        return dataManager;
    }
    
    public TaskLogic getTaskLogic() {
        return taskLogic;
    }

    public BatchLogic getBatchLogic() {
        return batchLogic;
    }

    public LogLogic getLogLogic() {
        return logLogic;
    }

    public ConfigLogic getConfigLogic() {
        return configLogic;
    }

    public UserLogic getUserLogic() {
        return userLogic;
    }

}
