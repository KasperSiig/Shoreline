package shoreline.dal.DAO;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import shoreline.be.User;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class UserDAO {

    /**
     * Get hashed password from database
     *
     * @param username Username to get password from
     * @param con Connection to database
     * @return Hashed password
     * @throws DALException
     */
    public String getPass(String username, Connection con) throws DALException {
        String sql = "SELECT password FROM UserTable WHERE username = ?";
        String password = null;
        try (PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, username);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                password = rs.getString("password");
            }
        } catch (SQLServerException ex) {
            throw new DALException("Error fetching password from DB.", ex);
        } catch (SQLException ex) {
            throw new DALException("Error fetching password from DB.", ex);
        }
        return password;
    }

    /**
     * Creates new user in database
     *
     * @param user User to be created
     * @param password Hashed password of the new user
     * @param con Connection to database
     * @return Same User as passed down, but now with ID from database
     * @throws DALException
     */
    public User createUser(User user, String password, Connection con) throws DALException {
        String sql = "INSERT INTO UserTable VALUES(?,?,?,?,?)";
        try (PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int userLevel = getAllUsers(con).isEmpty() ? 1 : 0;
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setString(4, password);
            statement.setInt(5, userLevel);

            if (statement.executeUpdate() == 1) {
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                user.setId(rs.getInt(1));
            }
        } catch (SQLServerException ex) {
            throw new DALException("Error saving new user in DB.", ex);
        } catch (SQLException ex) {
            throw new DALException("Error saving new user in DB.", ex);
        }
        return user;
    }

    /**
     * Gets user from database
     *
     * @param userName Username of the User to fetch
     * @param password Hashed password of the user
     * @param con Connection to database
     * @return User object
     * @throws DALException
     */
    public User getUser(String userName, String password, Connection con) throws DALException {
        String sql = "SELECT * FROM UserTable WHERE username = ? AND password = ?";
        User user = null;
        try (PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, userName);
            statement.setString(2, password);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("lastName"), rs.getString("firstName"),
                        rs.getString("username"), rs.getInt("id"), rs.getInt("userLevel"));
            }

        } catch (SQLException ex) {
            throw new DALException("Error loading user", ex);
        }
        return user;
    }

    public void updateUser(User user, String password, Connection con) {
        String sql = "UPDATE UserTable SET username = ?, firstName = ?, lastName = ?, password = ? WHERE id = ?";
        try (PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setString(4, password);
            statement.setInt(5, user.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateUser(User user, Connection con) {
        String sql = "UPDATE UserTable SET username = ?, firstName = ?, lastName = ?, userLevel = ? WHERE id = ?";
        try (PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setInt(4, user.getUserLevel());
            statement.setInt(5, user.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteUser(User user, Connection con) {
        String sql = "DELETE FROM LogTable WHERE userId = ?; DELETE FROM UserTable WHERE id = ?";
        try (PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, user.getId());
            statement.setInt(2, user.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<User> getAllUsers(Connection con) {
        String sql = "SELECT * FROM UserTable";
        List<User> users = new ArrayList();
        try (PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                User user = new User(rs.getString("lastName"), rs.getString("firstName"),
                        rs.getString("username"), rs.getInt("id"), rs.getInt("userLevel"));
                users.add(user);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return users;
    }

}
