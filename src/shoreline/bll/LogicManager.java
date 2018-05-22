package shoreline.bll;

import shoreline.dal.DataManager;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.DALException;

/**
 * Holds a reference to all classes containing logic
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
        try {
            this.dataManager = new DataManager();
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
        
        this.userLogic = new UserLogic(this);
        this.configLogic = new ConfigLogic(this);
        this.logLogic = new LogLogic(this);
        this.batchLogic = new BatchLogic(this);
        this.taskLogic = new TaskLogic(this);
    }

    /**
     * @return Reference to DataManager
     */
    public DataManager getDataManager() {
        return dataManager;
    }

    /**
     * @return Reference to TaskLogic
     */
    public TaskLogic getTaskLogic() {
        return taskLogic;
    }

    /**
     * @return Reference to BatchLogicc
     */
    public BatchLogic getBatchLogic() {
        return batchLogic;
    }

    /**
     * @return Reference to LogLogic
     */
    public LogLogic getLogLogic() {
        return logLogic;
    }

    /**
     * @return Reference to ConfigLogic
     */
    public ConfigLogic getConfigLogic() {
        return configLogic;
    }

    /**
     * @return Reference to UserLogic
     */
    public UserLogic getUserLogic() {
        return userLogic;
    }

}
