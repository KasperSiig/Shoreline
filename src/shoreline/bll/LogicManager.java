/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.bll;

import java.util.logging.Level;
import java.util.logging.Logger;
import shoreline.dal.DataManager;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kasper Siig
 */
public class LogicManager {

    private DataManager dm;

    public LogicManager() throws BLLException {
        try {
            this.dm = new DataManager();
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }
    
    public String getProperty(String key) throws BLLException {
        try {
            return dm.getProperty(key);
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }
    
    public void setProperty(String key, String input) throws BLLException {
        try {
            dm.setProperty(key, input);
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }

    public boolean validateLogin(String username, String pass) throws BLLException {
        try {
            String hashPass = dm.getPass(username);
            if (hashPass == null) {
                return false;
            }
            return hashPass.equals(pass);
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }

    public boolean createUser(String username, String password, String firstname, String lastname) throws BLLException {
        try {
            return dm.createUser(username,password,firstname,lastname);
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }
    
}
