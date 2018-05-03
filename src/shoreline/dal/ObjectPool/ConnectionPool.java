package shoreline.dal.ObjectPool;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import shoreline.dal.DataBaseConnector;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kasper Siig
 */
public class ConnectionPool extends ObjectPool<Connection>{

    private Connection con;
    private DataBaseConnector dbConnect;

    /**
     * Instantiates the ConnectionPool
     * 
     * @throws DALException 
     */
    public ConnectionPool() throws DALException {
        super();
        try {
            this.dbConnect = new DataBaseConnector();
        } catch (IOException ex) {
            throw new DALException("Error creating Connection Pool", ex);
        }
    }
    
    @Override
    protected Connection create() throws DALException {
        try {
            return dbConnect.getConnection();
        } catch (SQLServerException ex) {
            throw new DALException("Error returning new connection", ex);
        }
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
