package shoreline.dal.ObjectPool;

import java.sql.Connection;
import java.sql.SQLException;
import shoreline.dal.DataBaseConnector;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class ConnectionPool extends ObjectPool<Connection> {

    private DataBaseConnector dbConnect;

    /**
     * Instantiates the ConnectionPool
     *
     * @throws DALException
     */
    public ConnectionPool() throws DALException {
        // Call to constructor in ObjectPool
        super();
        this.dbConnect = new DataBaseConnector();
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
