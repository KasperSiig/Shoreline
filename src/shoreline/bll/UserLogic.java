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

    /**
     * Constructor for UserLogic
     *
     * @param logicManager Reference back to the LogicManager
     */
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
     * @param user User to create
     * @param password Password
     * @return Whether the user was created or not
     * @throws BLLException 
     */
    public User createUser(User user, String password) throws BLLException {
        try {
            return logicManager.getDataManager().createUser(user, password);
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
