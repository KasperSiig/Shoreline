/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.gui.model;

import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import shoreline.bll.LogicManager;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.GUIException;

/**
 *
 * @author kaspe
 */
public class PropertiesModel {
    private LogicManager logic;

    public PropertiesModel(LogicManager logic) {
        this.logic = logic;
    }
    
    public void setProperty(String key, String value) throws GUIException {
        try {
            logic.getPropertiesLogic().setProperty(key, value);
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }
    
    public String getProperty(String key) {
        return logic.getPropertiesLogic().getProperty(key);
    }
    
    public Properties getPropertiesFromFile(String filePath) {
        return logic.getPropertiesLogic().getPropertiesFromFile(filePath);
    }
    
    public void savePropertiesFile(String filePath, HashMap<String, String> properties, boolean overwrite) throws GUIException {
        try {
            logic.getPropertiesLogic().savePropertiesFile(filePath, properties, overwrite);
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }
    
    public boolean isConfigEmpty() throws GUIException {
        try {
            return logic.getPropertiesLogic().isConfigEmpty();
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }
    
}
