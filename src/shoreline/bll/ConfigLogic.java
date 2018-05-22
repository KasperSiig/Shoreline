package shoreline.bll;

import java.util.List;
import shoreline.be.Config;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class ConfigLogic {
    private LogicManager logicManager;

    /**
     * Constructor for ConfigLogic
     * 
     * @param logicManager Reference back to the LogicManager
     */
    public ConfigLogic(LogicManager logicManager) {
        this.logicManager = logicManager;
    }
    
    /**
     * @return List of all configs in database
     * @throws BLLException 
     */
    public List<Config> getAllConfigs() throws BLLException {
        try {
            return logicManager.getDataManager().getAllConfigs();
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }

    /**
     * Saves Config in database
     * 
     * @param config Config to be saved
     * @throws BLLException 
     */
    public void saveConfig(Config config) throws BLLException {
        try {
            logicManager.getDataManager().saveConfig(config);
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }
}
