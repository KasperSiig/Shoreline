package shoreline.dal.DAO;

import java.io.*;
import java.util.Properties;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class PropertiesDAO {

    private Properties properties;

    /**
     * Gets property from config.properties file
     * 
     * @param key Property to fetch
     * @return value from given key
     * @throws DALException 
     */
    public String getProperty(String key) throws DALException {
        if (properties != null) {
            return properties.getProperty(key);
        }

        try (BufferedReader br = new BufferedReader(new FileReader("config.properties"))) {
            properties = new Properties();
            properties.load(br);

            return properties.getProperty(key);
        } catch (IOException ex) {
            throw new DALException(ex.getLocalizedMessage(), ex);
        }
    }

}
