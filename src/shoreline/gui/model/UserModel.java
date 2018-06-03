package shoreline.gui.model;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import shoreline.be.User;
import shoreline.bll.LogicManager;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.GUIException;

/**
 * Holds relevant information about Login-system, and creates connection between
 * GUI and BLL layer
 * 
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class UserModel {

    private LogicManager logic;

    private User user;

    public UserModel(LogicManager logic) {
        this.logic = logic;
    }

    /**
     * @return Currently logged in User
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets User to be logged in
     * 
     * @param user User that has logged in
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Hashes password and parses data through to BLL
     *
     * @param user User to create
     * @param password Password from user
     * @return Boolean whether the user was successfully created or not
     * @throws GUIException
     */
    public User create(User user, String password) throws GUIException {
        try {
            return logic.getUserLogic().createUser(user, hashString(password));
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }

    /**
     * Gets user, after password has been validated
     *
     * @param username Username entered
     * @param password Password entered
     * @return Boolean whether the user is valid
     * @throws GUIException
     */
    public User getUserOnLogin(String username, String password) throws GUIException {
        try {
            return logic.getUserLogic().getUser(username, hashString(password));
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }

    /**
     * Validates password
     * 
     * @param username Username entered
     * @param pass Password entered
     * @return true if the user has correct credentials
     * @throws GUIException 
     */
    public boolean validatePassword(String username, String pass) throws GUIException {
        try {
            return logic.getUserLogic().validateLogin(username, hashString(pass));
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }

    /**
     * Hashes a given string and returns the hashed string
     *
     * @param string String to be hashed
     * @return StringBuffer containing hashed string
     * @throws GUIException
     */
    private String hashString(String string) throws GUIException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(string.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {

                String hex = Integer.toHexString(0xff & hash[i]);

                if (hex.length() == 1) {
                    hexString.append('0');
                }

                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            throw new GUIException("Error hashing password", ex);
        }
    }
    
    public void updateUser(User user, String password) throws GUIException {
        try {
            logic.getUserLogic().updateUser(user, hashString(password));
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }
    
    public void updateUser(User user) throws GUIException {
        try {
            logic.getUserLogic().updateUser(user);
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }
    
    public void deleteUser(User user) throws GUIException {
        try {
            logic.getUserLogic().deleteUser(user);
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }
    
    public List<User> getAllUsers() throws GUIException {
        try {
            return logic.getUserLogic().getAllUsers();
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }
    
    
}
