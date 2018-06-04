package shoreline.dal.DAO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    public void savePropertiesFile(String filePath, HashMap<String, String> properties, boolean overwrite) throws DALException {
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
            properties.forEach((String key, String value) -> {
                prop.setProperty(key, value);
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
    
}
