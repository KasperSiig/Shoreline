package shoreline.dal;

import java.io.File;
import java.util.HashMap;
import shoreline.be.ConvTask;
import shoreline.dal.ConvStrats.ConvImpl;
import shoreline.dal.ConvStrats.XLXSConvStrat;
import shoreline.dal.TitleStrats.TitleImpl;
import shoreline.dal.TitleStrats.XLSXTitleStrat;
import shoreline.exceptions.DALException;

/**
 *
 * @author
 */
public class DataManager {

    private PropertiesDAO pDAO;
    private UserDAO userDAO;

    public DataManager() throws DALException {
        this.pDAO = new PropertiesDAO();
        this.userDAO = new UserDAO();
    }

    public String getProperty(String key) throws DALException {
        return pDAO.getProperty(key);
    }

    public void setProperty(String key, String input) throws DALException {
        pDAO.setProperty(key, input);
    }

    public String getPass(String username) throws DALException {
        return userDAO.getPass(username);
    }

    public boolean createUser(String username, String password, String firstname, String lastname) throws DALException {
        return userDAO.createUser(username, password, firstname, lastname);
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
