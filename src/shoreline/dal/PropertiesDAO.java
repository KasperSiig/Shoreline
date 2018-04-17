package shoreline.dal;

import java.io.*;
import java.util.Properties;
import shoreline.exceptions.DALException;

/**
 *
 * @author 
 */
public class PropertiesDAO
{

    private Properties properties;

    public PropertiesDAO()
    {
    }

    public void setProperty(String key, String input) throws DALException
    {
        // Have to call getProperty on a valid property, or else shit just breaks...
        getProperty("darkTheme");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("config.properties")))
        {
            
            properties.setProperty(key, input);
            properties.store(bw, input);
        }
        catch (IOException ex)
        {
            throw new DALException(ex.getLocalizedMessage(), ex);
        }
    }

    public String getProperty(String key) throws DALException
    {
        if (properties != null)
            return properties.getProperty(key);

        try (BufferedReader br = new BufferedReader(new FileReader("config.properties")))
        {
            properties = new Properties();
            properties.load(br);
            
            return properties.getProperty(key);
        }
        catch (IOException ex)
        {
            throw new DALException(ex.getLocalizedMessage(), ex);
        }
    }

}
