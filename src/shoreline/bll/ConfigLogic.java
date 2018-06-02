package shoreline.bll;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
     * @param dataManager Holds a reference to DataManager
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

    public void updateConfig(Config config) throws BLLException {
        try {
            dataManager.updateConfig(config);
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }
    
    public void deleteConfig(Config config) throws BLLException {
        try {
            dataManager.deleteConfig(config);
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }
    
}
