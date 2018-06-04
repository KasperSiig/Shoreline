/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.bll;

import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import shoreline.dal.DataManager;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.DALException;

/**
 *
 * @author kaspe
 */
public class PropertiesLogic extends LogicClass {

    public PropertiesLogic(DataManager dataManager) {
        super(dataManager);
    }
    
    public void setProperty(String key, String value) throws BLLException {
        try {
            dataManager.setProperty(key, value);
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }
    
    public String getProperty(String key) {
        return dataManager.getProperty(key);
    }

    public Properties getPropertiesFromFile(String filePath) {
        return dataManager.getPropertiesFromFile(filePath);
    }
    
    public void savePropertiesFile(String filePath, HashMap<String, String> properties, boolean overwrite) throws BLLException {
        try {
            dataManager.savePropertiesFile(filePath, properties, overwrite);
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }
    
    public boolean isConfigEmpty() throws BLLException {
        try {
            return dataManager.isConfigEmpty();
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }
    
}