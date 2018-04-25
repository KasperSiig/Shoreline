package shoreline.dal;

import shoreline.exceptions.DALException;

/**
 *
 * @author
 */
public class DataManager {

    private PropertiesDAO pDAO;
    private LoginDAO loginDAO;

    public DataManager() throws DALException {
        this.pDAO = new PropertiesDAO();
    }
    
    public String getProperty(String key) throws DALException {
        return pDAO.getProperty(key);
    }
    
    public void setProperty(String key, String input) throws DALException {
        pDAO.setProperty(key, input);
    }

    public String getPass(String username) throws DALException {
        return loginDAO.getPass(username);
    }
    
}
