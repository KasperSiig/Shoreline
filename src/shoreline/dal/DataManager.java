package shoreline.dal;

import shoreline.dal.DAO.ConfigDAO;
import shoreline.dal.DAO.LoggingDAO;
import shoreline.dal.DAO.UserDAO;
import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javafx.scene.control.Alert;
import org.json.JSONObject;
import shoreline.be.Config;
import shoreline.be.LogItem;
import shoreline.be.User;
import shoreline.dal.DAO.PropertiesDAO;
import shoreline.dal.DAO.TemplateDAO;
import shoreline.dal.ObjectPool.ConnectionPool;
import shoreline.dal.TitleStrats.CSVTitleStrat;
import shoreline.dal.TitleStrats.TitleImpl;
import shoreline.dal.TitleStrats.XLSXTitleStrat;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class DataManager {

    private ConnectionPool conPool;
    private UserDAO userDAO;
    private LoggingDAO logDAO;
    private ConfigDAO cfgDAO;
    private TemplateDAO templateDAO;
    private PropertiesDAO propertiesDAO;

    /**
     * Constructor for DataManager
     * @throws DALException 
     */
    public DataManager() throws DALException {
        this.propertiesDAO = new PropertiesDAO();
        this.conPool = new ConnectionPool(this);
        this.userDAO = new UserDAO();
        this.logDAO = new LoggingDAO();
        this.cfgDAO = new ConfigDAO();
        this.templateDAO = new TemplateDAO();
        
    }

    /**
     * @param username Username to get password from
     * @return Hashed password
     * @throws DALException 
     */
    public String getPass(String username) throws DALException {
        Connection con = conPool.checkOut();
        String pass = userDAO.getPass(username, con);
        conPool.checkIn(con);
        return pass;
    }

    /**
     * Create user in database
     * 
     * @param user User to create
     * @param password Password from user
     * @return User
     * @throws DALException 
     */
    public User createUser(User user, String password) throws DALException {
        Connection con = conPool.checkOut();
        User userRtn = userDAO.createUser(user, password, con);
        conPool.checkIn(con);
        return userRtn;
    }

    /**
     * @param userName Username of the user to get
     * @param password Password of the user to get
     * @return User
     * @throws DALException 
     */
    public User getUser(String userName, String password) throws DALException {
        Connection con = conPool.checkOut();
        User user = userDAO.getUser(userName, password, con);
        conPool.checkIn(con);
        return user;
    }

    /**
     * Adds log to database
     * 
     * @param user User associated with the log
     * @param type Type of log
     * @param message Message in log
     * @throws DALException 
     */
    public void addLog(User user, Alert.AlertType type, String message) throws DALException {
        Connection con = conPool.checkOut();
        logDAO.addLog(user, type, message, con);
        conPool.checkIn(con);
    }

    /**
     * @return Existing logs in database
     * @throws DALException 
     */
    public List<LogItem> getExistingLogs() throws DALException {
        Connection con = conPool.checkOut();
        List<LogItem> items = logDAO.getExistingLogs(con);
        conPool.checkIn(con);
        return items;
    }

    /**
     * @return New logs in database
     * @throws DALException 
     */
    public List<LogItem> getNewLogs() throws DALException {
        Connection con = conPool.checkOut();
        List<LogItem> items = logDAO.getNewLogs(con);
        conPool.checkIn(con);
        return items;
    }

    /**
     * @return All configs in database
     * @throws DALException 
     */
    public List<Config> getAllConfigs() throws DALException {
        Connection con = conPool.checkOut();
        List<Config> configs = cfgDAO.getAllConfigs(con);
        conPool.checkIn(con);
        return configs;
    }
    
    /**
     * Save config in database
     * 
     * @param config Config to be saved
     * @throws DALException 
     */
    public void saveConfig(Config config) throws DALException {
        Connection con = conPool.checkOut();
        cfgDAO.saveConfig(config, con);
        conPool.checkIn(con);
    }

    /**
     * Implementation of TitleStrats
     * 
     * @param file File to get titles from
     * @return HashMap containing titles and indexes
     * @throws DALException 
     */
    public HashMap<String, Integer> getTitles(File file) throws DALException {
        String extension = "";

        int i = file.getAbsolutePath().lastIndexOf('.');
        if (i > 0) {
            extension = file.getAbsolutePath().substring(i + 1);
        }
        TitleImpl impl;
        switch (extension) {
            case "xlsx":
                impl = new TitleImpl(new XLSXTitleStrat());
                break;
            case "csv":
                impl = new TitleImpl(new CSVTitleStrat());
                break;
            default:
                throw new IllegalArgumentException();
        }
        return impl.getTitles(file);
    }
    
    public void saveTemplate(JSONObject jsonObject) throws DALException {
        Connection con = conPool.checkOut();
        templateDAO.save(jsonObject, con);
        conPool.checkIn(con);
    }
    
    public JSONObject getTemplate() throws DALException {
        Connection con = conPool.checkOut();
        JSONObject object = templateDAO.getTemplate(con);
        conPool.checkIn(con);
        return object;
    }

    public void updateConfig(Config config) throws DALException {
        Connection con = conPool.checkOut();
        cfgDAO.updateConfig(config, con);
        conPool.checkIn(con);
    }
    
    public void deleteConfig(Config config) throws DALException {
        Connection con = conPool.checkOut();
        cfgDAO.deleteConfig(config, con);
        conPool.checkIn(con);
    }
    
    public void updateUser(User user, String password) throws DALException {
        Connection con = conPool.checkOut();
        userDAO.updateUser(user, password, con);
        conPool.checkIn(con);
    }
    
    public void updateUser(User user) throws DALException {
        Connection con = conPool.checkOut();
        userDAO.updateUser(user, con);
        conPool.checkIn(con);
    }
    
    public void deleteUser(User user) throws DALException {
        Connection con = conPool.checkOut();
        userDAO.deleteUser(user, con);
        conPool.checkIn(con);
    }
    
    public List<User> getAllUsers() throws DALException {
        List<User> users;
        Connection con = conPool.checkOut();
        users = userDAO.getAllUsers(con);
        conPool.checkIn(con);
        return users;
    }
    
    public String getProperty(String key) {
        return propertiesDAO.getProperty(key);
    }
    
    public void setProperty(String key, String value) throws DALException {
        propertiesDAO.setProperty(key, value);
    }
    
    public Properties getPropertiesFromFile(String filePath) {
        return propertiesDAO.getPropertiesFromFile(filePath);
    }
    
    public void savePropertiesFile(String filePath, HashMap<String, String> properties, boolean overwrite) throws DALException {
        propertiesDAO.savePropertiesFile(filePath, properties, overwrite);
    }

    public HashMap<String, File> getAllPropertyFiles() {
        return propertiesDAO.getAllPropertyFiles();
    }
    
    
}
