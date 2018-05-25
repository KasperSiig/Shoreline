package shoreline.dal;


import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class DataBaseConnector {

    private SQLServerDataSource dataSource;

    public DataBaseConnector() throws DALException {

        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("config.properties"));
            
            dataSource = new SQLServerDataSource();
            dataSource.setServerName(properties.getProperty("serverName"));
            dataSource.setPortNumber(Integer.parseInt(properties.getProperty("portNumber")));
            dataSource.setDatabaseName(properties.getProperty("databaseName"));
            dataSource.setUser(properties.getProperty("user"));
            dataSource.setPassword(properties.getProperty("password"));
        } catch (FileNotFoundException ex) {
            throw new DALException("Error getting properties", ex);
        } catch (IOException ex) {
            throw new DALException("Error getting properties", ex);
        }

    }

    public Connection getConnection() throws SQLServerException {
        return dataSource.getConnection();
    }

}
