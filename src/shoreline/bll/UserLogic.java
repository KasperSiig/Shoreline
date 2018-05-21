/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.bll;

import shoreline.be.User;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kasper Siig
 */
public class UserLogic {
    
    private LogicManager logicManager;

    public UserLogic(LogicManager logicManager) {
        this.logicManager = logicManager;
    }
    
    public boolean validateLogin(String username, String pass) throws BLLException {
        try {
            String hashPass = logicManager.getDataManager().getPass(username);
            if (hashPass == null) {
                return false;
            }
            return hashPass.equals(pass);
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }

    public int createUser(String username, String password, String firstname, String lastname) throws BLLException {
        try {
            return logicManager.getDataManager().createUser(username, password, firstname, lastname);
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }

    public User getUser(String username, String password) throws BLLException {
        try {
            return logicManager.getDataManager().getUser(username, password);
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }
}
