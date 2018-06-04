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
    private String userDir = System.getProperty("user.dir");
    private DataManager dataManager;

    public DataBaseConnector(DataManager dataManager) throws DALException {
        this.dataManager = dataManager;
        int portNumber = 0;
        String portNumberString = dataManager.getProperty("portNumber");
        if (portNumberString.matches("[0-9]+")) {
            portNumber = Integer.parseInt(portNumberString);
        }
        dataSource = new SQLServerDataSource();
        dataSource.setServerName(dataManager.getProperty("serverName"));
        dataSource.setPortNumber(portNumber);
        dataSource.setDatabaseName(dataManager.getProperty("databaseName"));
        dataSource.setUser(dataManager.getProperty("user"));
        dataSource.setPassword(dataManager.getProperty("password"));

    }

    public Connection getConnection() throws DALException {
        try {
            return dataSource.getConnection();
        } catch (SQLServerException ex) {
            throw new DALException("Could not retrieve Connection", ex);
        }
    }

}
