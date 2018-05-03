package shoreline.dal;

import shoreline.dal.DAO.ConfigDAO;
import shoreline.dal.DAO.PropertiesDAO;
import shoreline.dal.DAO.LoggingDAO;
import shoreline.dal.DAO.UserDAO;
import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import javafx.scene.control.Alert;
import shoreline.be.Config;
import shoreline.be.ConvTask;
import shoreline.be.LogItem;
import shoreline.be.User;
import shoreline.dal.ConvStrats.ConvImpl;
import shoreline.dal.ConvStrats.XLXSConvStrat;
import shoreline.dal.ObjectPool.ConnectionPool;
import shoreline.dal.TitleStrats.TitleImpl;
import shoreline.dal.TitleStrats.XLSXTitleStrat;
import shoreline.exceptions.DALException;

/**
 *
 * @author
 */
public class DataManager {

    private ConnectionPool conPool;
    private PropertiesDAO pDAO;
    private UserDAO userDAO;
    private LoggingDAO logDAO;
    private ConfigDAO cfgDAO;
    
    public DataManager() throws DALException {
        this.conPool = new ConnectionPool();
        this.pDAO = new PropertiesDAO();
        this.userDAO = new UserDAO();
        this.logDAO = new LoggingDAO();
        this.cfgDAO = new ConfigDAO();
    }

    public String getProperty(String key) throws DALException {
        return pDAO.getProperty(key);
    }

    public void setProperty(String key, String input) throws DALException {
        pDAO.setProperty(key, input);
    }

    public String getPass(String username) throws DALException {
        Connection con = conPool.checkOut();
        String pass = userDAO.getPass(username, con);
        conPool.checkIn(con);
        return pass;
    }

    public boolean createUser(String username, String password, String firstname, String lastname) throws DALException {
        Connection con = conPool.checkOut();
        boolean bool = userDAO.createUser(username, password, firstname, lastname, con);
        conPool.checkIn(con);
        return bool;
    }
    
    public User getUser(String userName, String password) throws DALException {
        Connection con = conPool.checkOut();
        User user = userDAO.getUser(userName, password, con);
        conPool.checkIn(con);
        return user;
    }
    
    
    public void addLog(int userId, Alert.AlertType type, String message) throws DALException{
        Connection con = conPool.checkOut();
        logDAO.addLog(userId, type, message, con);
        conPool.checkIn(con);
    }
    
    public List<LogItem> getAllLogs() throws DALException{
        Connection con = conPool.checkOut();
        List<LogItem> items = logDAO.getAllLogs(con);
        conPool.checkIn(con);
        return items;
    }
    
    public List<LogItem> getNewLogs() throws DALException {
        Connection con = conPool.checkOut();
        List<LogItem> items = logDAO.getNewLogs(con);
        conPool.checkIn(con);
        return items;
    }
    
    public List<Config> getAllConfigs() throws DALException{
        Connection con = conPool.checkOut();
        List<Config> configs = cfgDAO.getAllConfigs(con);
        conPool.checkIn(con);
        return configs;
    }
    
    public void saveConfig(String name, String extension, HashMap map) throws DALException{
        Connection con = conPool.checkOut();
        cfgDAO.saveConfig(name, extension, map, con);
        conPool.checkIn(con);
    }

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
            default:
                throw new IllegalArgumentException();
        }
        return impl.getTitles(file);
    }

    public void addCallableToTask(ConvTask task) throws DALException {
        String extension = "";

        int i = task.getSource().getAbsolutePath().lastIndexOf('.');
        if (i > 0) {
            extension = task.getSource().getAbsolutePath().substring(i + 1);
        }
        ConvImpl impl;
        switch (extension) {
            case "xlsx":
                impl = new ConvImpl(new XLXSConvStrat());
                impl.addCallableToTask(task);
                break;
            default:
                throw new IllegalArgumentException();
        }
        
    }

}
