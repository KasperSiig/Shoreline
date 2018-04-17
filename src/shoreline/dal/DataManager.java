package shoreline.dal;

import shoreline.exceptions.DALException;

/**
 *
 * @author
 */
public class DataManager {

    private PropertiesDAO pDao;

    public DataManager() throws DALException {
        this.pDao = new PropertiesDAO();
    }
    
    public String getProperty(String key) throws DALException {
        return pDao.getProperty(key);
    }
    
    public void setProperty(String key, String input) throws DALException {
        pDao.setProperty(key, input);
    }
    
}
