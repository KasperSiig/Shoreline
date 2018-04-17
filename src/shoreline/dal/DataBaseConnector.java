/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.dal;


import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

/**
 *
 * @author Kasper, Mads, Tina & Kenneth
 */
public class DataBaseConnector {

    private SQLServerDataSource dataSource;

    public DataBaseConnector() throws FileNotFoundException, IOException {

        Properties properties = new Properties();
        properties.load(new FileInputStream("config.properties"));

        dataSource = new SQLServerDataSource();
        dataSource.setServerName(properties.getProperty("serverName"));
        dataSource.setPortNumber(Integer.parseInt(properties.getProperty("portNumber")));
        dataSource.setDatabaseName(properties.getProperty("databaseName"));
        dataSource.setUser(properties.getProperty("user"));
        dataSource.setPassword(properties.getProperty("password"));

    }

    public Connection getConnection() throws SQLServerException {
        return dataSource.getConnection();
    }

}
