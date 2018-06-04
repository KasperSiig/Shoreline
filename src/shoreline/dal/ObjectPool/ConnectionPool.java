package shoreline.dal.ObjectPool;

import java.sql.Connection;
import java.sql.SQLException;
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

}
