package shoreline.bll;

import shoreline.be.User;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class UserLogic {
    
    private LogicManager logicManager;

    public UserLogic(LogicManager logicManager) {
        this.logicManager = logicManager;
    }
    
    /**
     * Validates password on login
     * 
     * @param username Username to retrieve password of
     * @param pass Password to validate
     * @return Whether login is valid or not
     * @throws BLLException 
     */
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

    /**
     * Creates new user
     * 
     * @param username Username
     * @param password Password
     * @param firstname First Name
     * @param lastname Last Name
     * @return Whether the user was created or not
     * @throws BLLException 
     */
    public int createUser(String username, String password, String firstname, String lastname) throws BLLException {
        try {
            return logicManager.getDataManager().createUser(username, password, firstname, lastname);
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }

    /**
     * Gets the user from given username and password
     * 
     * @param username Username from login
     * @param password Password from login
     * @return User
     * @throws BLLException 
     */
    public User getUser(String username, String password) throws BLLException {
        try {
            return logicManager.getDataManager().getUser(username, password);
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }
}
