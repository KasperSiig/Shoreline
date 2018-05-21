package shoreline.bll;

import java.util.HashMap;
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
     * Saves config in database
     * 
     * @param name Name of config
     * @param extension Filetype that config is used for
     * @param map 
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
