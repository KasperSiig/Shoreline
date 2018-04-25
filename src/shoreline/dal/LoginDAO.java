/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kasper Siig
 */
public class LoginDAO {
    
    DataBaseConnector dbConnector;

    public LoginDAO() throws DALException {
        try {
            dbConnector = new DataBaseConnector();
        } catch (IOException ex) {
            throw new DALException(ex);
        }
    }

    public String getPass(String username) throws DALException {
        try (Connection con = dbConnector.getConnection()) {
            String sql = "SELECT password FROM UserTable WHERE username = ?";

            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, username);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getString("password");
            }
        } catch (SQLServerException ex) {
            throw new DALException(ex);
        } catch (SQLException ex) {
            throw new DALException(ex);
        }
        return null;
    }
    
}
