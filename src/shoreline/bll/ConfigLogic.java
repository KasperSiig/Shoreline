package shoreline.bll;

import java.util.List;
import shoreline.be.Config;
import shoreline.dal.DataManager;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class ConfigLogic extends LogicClass {

    /**
     * Constructor for ConfigLogic
     * 
     * @param logicManager Reference back to the LogicManager
     */
    public ConfigLogic(DataManager dataManager) {
        super(dataManager);
    }
    
    /**
     * @return List of all configs in database
     * @throws BLLException 
     */
    public List<Config> getAllConfigs() throws BLLException {
        try {
            return dataManager.getAllConfigs();
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
            dataManager.saveConfig(config);
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }
}
