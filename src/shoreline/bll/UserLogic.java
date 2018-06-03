package shoreline.bll;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import shoreline.be.User;
import shoreline.dal.DataManager;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class UserLogic extends LogicClass {

    /**
     * Constructor for UserLogic
     *
     * @param dataManager Holds a reference to DataManager
     */
    public UserLogic(DataManager dataManager) {
        super(dataManager);
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
            String hashPass = dataManager.getPass(username);
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
            return dataManager.createUser(user, password);
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
            return dataManager.getUser(username, password);
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }
    
    public void updateUser(User user, String password) throws BLLException {
        try {
            dataManager.updateUser(user, password);
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }
    
    public void updateUser(User user) throws BLLException {
        try {
            dataManager.updateUser(user);
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }
    
    public void deleteUser(User user) throws BLLException {
        try {
            dataManager.deleteUser(user);
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }
    
    public List<User> getAllUsers() throws BLLException {
        try {
            return dataManager.getAllUsers();
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }
    
    
}
