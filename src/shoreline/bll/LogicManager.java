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
    private TemplateLogic templateLogic;
    private PropertiesLogic propertiesLogic;

    private DataManager dataManager;

    public LogicManager() throws BLLException {
        try {
            this.dataManager = new DataManager();
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
        
        this.userLogic = new UserLogic(dataManager);
        this.configLogic = new ConfigLogic(dataManager);
        this.logLogic = new LogLogic(dataManager);
        this.taskLogic = new TaskLogic(dataManager);
        this.batchLogic = new BatchLogic(dataManager, taskLogic);
        this.templateLogic = new TemplateLogic(dataManager);
        this.propertiesLogic = new PropertiesLogic(dataManager);
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

    /**
     * @return Reference to TemplateLogic
     */
    public TemplateLogic getTemplateLogic() {
        return templateLogic;
    }

    public PropertiesLogic getPropertiesLogic() {
        return propertiesLogic;
    }
}
