package shoreline.dal.ObjectPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import shoreline.dal.DataBaseConnector;
import shoreline.dal.DataManager;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class ConnectionPool extends ObjectPool<Connection> {

    private DataBaseConnector dbConnect;
    private DataManager dataManager;

    /**
     * Instantiates the ConnectionPool
     *
     * @param dataManager
     * @throws DALException
     */
    public ConnectionPool(DataManager dataManager) throws DALException {
        // Call to constructor in ObjectPool
        super();
        this.dataManager = dataManager;
        this.dbConnect = new DataBaseConnector(dataManager);
        expireConnections();
    }

    @Override
    protected Connection create() throws DALException {
        return dbConnect.getConnection();
    }

    @Override
    public boolean validate(Connection o) throws DALException {
        try {
            return !o.isClosed();
        } catch (SQLException ex) {
            throw new DALException("Could not validate Connection", ex);
        }
    }

    @Override
    public void expire(Connection o) throws DALException {
        try {
            o.close();
        } catch (SQLException ex) {
            throw new DALException("Could not close connection", ex);
        }
    }

    private void expireConnections() {
        dataManager.getDbSwitch().addListener((observable, oldValue, newValue) -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(ConnectionPool.class.getName()).log(Level.SEVERE, null, ex);
            }
            locked.forEach((key, value) -> {
                try {
                    expire(key);
                } catch (DALException ex) {
                    Logger.getLogger(ConnectionPool.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            unlocked.forEach((key, value) -> {
                try {
                    expire(key);
                } catch (DALException ex) {
                    Logger.getLogger(ConnectionPool.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        });

    }

}
