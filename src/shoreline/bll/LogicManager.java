package shoreline.bll;

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
