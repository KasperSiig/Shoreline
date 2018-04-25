package shoreline.dal;

import shoreline.exceptions.DALException;

/**
 *
 * @author
 */
public class DataManager {

    private PropertiesDAO pDAO;
    private UserDAO userDAO;

    public DataManager() throws DALException {
        this.pDAO = new PropertiesDAO();
        this.userDAO = new UserDAO();
    }
    
    public String getProperty(String key) throws DALException {
        return pDAO.getProperty(key);
    }
    
    public void setProperty(String key, String input) throws DALException {
        pDAO.setProperty(key, input);
    }

    public String getPass(String username) throws DALException {
        return userDAO.getPass(username);
    }

    public boolean createUser(String username, String password, String firstname, String lastname) throws DALException {
        return userDAO.createUser(username,password,firstname,lastname);
    }
    
}
