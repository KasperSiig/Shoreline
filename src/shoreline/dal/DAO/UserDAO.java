/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.dal.DAO;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import shoreline.be.User;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kasper Siig
 */
public class UserDAO {

    public UserDAO() {
    }

    public String getPass(String username, Connection con) throws DALException {
        String sql = "SELECT password FROM UserTable WHERE username = ?";
        try (PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, username);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getString("password");
            }
        } catch (SQLServerException ex) {
            throw new DALException("Error fetching password from DB.", ex);
        } catch (SQLException ex) {
            throw new DALException("Error fetching password from DB.", ex);
        }
        return null;
    }

    /**
     * Connects to the database and insert the user details into it.
     *
     * @param username
     * @param password
     * @param firstname
     * @param lastname
     * @return
     * @throws DALException
     */
    public int createUser(String username, String password, String firstname, String lastname, Connection con) throws DALException {
        String sql = "INSERT INTO UserTable VALUES(?,?,?,?)";
        try (PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, username);
            statement.setString(2, firstname);
            statement.setString(3, lastname);
            statement.setString(4, password);

            if (statement.executeUpdate() == 1) {
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLServerException ex) {
            throw new DALException("Error saving new user in DB.", ex);
        } catch (SQLException ex) {
            throw new DALException("Error saving new user in DB.", ex);
        }
        return 0;
    }

    public User getUser(String userName, String password, Connection con) throws DALException {
        String sql = "SELECT * FROM UserTable WHERE username = ? AND password = ?";

        try (PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, userName);
            statement.setString(2, password);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new User(rs.getString("lastName"), rs.getString("firstName"), rs.getString("username"), rs.getInt("id"));
            }

        } catch (SQLException ex) {
            throw new DALException("Error loading user", ex);
        }
        System.out.println("hi");
        return null;
    }

}
