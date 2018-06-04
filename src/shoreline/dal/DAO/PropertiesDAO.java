package shoreline.dal.DAO;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import shoreline.bll.ThreadPool;
import shoreline.exceptions.DALException;

/**
 *
 * @author kaspe
 */
public class PropertiesDAO {

    private String userDir = System.getProperty("user.dir");
    private File configFile;
    private Properties properties;

    public PropertiesDAO() {
        configFile = new File(userDir + "\\configs\\config.properties");
        try (BufferedReader br = new BufferedReader(new FileReader(configFile.getAbsolutePath()))) {
            properties = new Properties();
            properties.load(br);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PropertiesDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PropertiesDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setProperty(String key, String value) throws DALException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(configFile.getAbsolutePath()))) {
            properties.setProperty(key, value);
            properties.store(bw, "Credentials For Database");
        } catch (IOException ex) {
            throw new DALException("Could not set property", ex);
        }
    }

    public String getProperty(String key, Properties properties) {
        return properties.containsKey(key) ? properties.getProperty(key) : "";
    }

    public String getProperty(String key) {
        return properties.containsKey(key) ? properties.getProperty(key) : "";
    }

    public Properties getPropertiesFromFile(String filePath) {
        Properties localProperties = new Properties();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            localProperties = new Properties();
            localProperties.load(br);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PropertiesDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PropertiesDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return localProperties;
    }

    public void savePropertiesFile(String filePath, Properties properties, boolean overwrite) throws DALException {
        Properties localProperties = new Properties();
        File file = new File(filePath);
        try {
            file.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(PropertiesDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!overwrite) {
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                localProperties = new Properties();
                localProperties.load(br);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PropertiesDAO.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(PropertiesDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        final Properties prop = localProperties;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            properties.forEach((key, value) -> {
                prop.setProperty((String) key, (String) value);
            });
            prop.store(bw, "Credentials For Database");
        } catch (IOException ex) {
            throw new DALException("Could not save property file", ex);
        }
    }

    public HashMap<String, File> getAllPropertyFiles() {
        HashMap<String, File> configs = new HashMap();
        File directory = new File(userDir + "\\configs\\");
        for (File file : directory.listFiles()) {
            if (file.getName().equals("config.properties")) {
                configs.put("Current", file);
            } else {
                int index = file.getName().lastIndexOf(".properties");
                configs.put(file.getName().substring(0, index), file);
            }
        }
        return configs;
    }

    public boolean validateConnection(Properties properties) throws DALException {
        boolean valid = false;
        ThreadPool tp = ThreadPool.getInstance();
        Callable<Boolean> callable = () -> {
            SQLServerDataSource dataSource = new SQLServerDataSource();
            int portNumber = 0;
            String portNumberString = getProperty("portNumber", properties);
            if (portNumberString.matches("[0-9]+")) {
                portNumber = Integer.parseInt(portNumberString);
            }
            dataSource = new SQLServerDataSource();
            dataSource.setServerName(getProperty("serverName", properties));
            dataSource.setPortNumber(portNumber);
            dataSource.setDatabaseName(getProperty("databaseName", properties));
            dataSource.setUser(getProperty("user", properties));
            dataSource.setPassword(getProperty("password", properties));
            try {
                dataSource.getConnection();
                return true;
            } catch (SQLServerException ex) {
                return false;
            }
        };
        Future future = tp.addCallableToPool(callable);
        try {
            return (boolean) future.get(3, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            return false;
        }
    }

}
