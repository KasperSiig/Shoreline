/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.bll;

import java.util.HashMap;
import java.util.List;
import shoreline.be.Config;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kasper Siig
 */
public class ConfigLogic {
    private LogicManager logicManager;

    public ConfigLogic(LogicManager logicManager) {
        this.logicManager = logicManager;
    }
    
    public List<Config> getAllConfigs() throws BLLException {
        try {
            return logicManager.getDataManager().getAllConfigs();
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }

    public void saveConfig(String name, String extension, HashMap map) throws BLLException {
        try {
            logicManager.getDataManager().saveConfig(name, extension, map);
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }
}
