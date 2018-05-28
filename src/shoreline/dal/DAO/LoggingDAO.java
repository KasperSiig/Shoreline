package shoreline.dal.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;
import shoreline.be.LogItem;
import shoreline.be.User;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class LoggingDAO {

    private int currentId = 0;
    
    /**
     * Fetches all existing logs, and set currentId to the highest id fetched.
     *
     * @param con Connection to database
     * @return All logs in database
     * @throws DALException
     */
    public List<LogItem> getExistingLogs(Connection con) throws DALException {
        String sql = "SELECT LT.*, UT.* FROM LogTable LT "
                + "JOIN UserTable UT ON UT.id = LT.userId "
                + "ORDER BY UT.id";
        try (PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            List<LogItem> logItems = new ArrayList();

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Alert.AlertType alertType = getAlertType(rs.getString("type"));
                currentId = rs.getInt("id");
                Date date = rs.getDate("date");
                LogItem logItem = new LogItem(currentId, alertType, rs.getString("message"), 
                        rs.getString("firstName") + " " + rs.getString("lastName"), date);
                logItems.add(logItem);

            }
            return logItems;
        } catch (SQLException ex) {
            throw new DALException("Could not get existing logs", ex);
        }
    }

    /**
     * Fetches logs that has an id higher than currentId and sets currentId to
     * the highest fetched.
     *
     * @param con Connection to database
     * @return New logs
     * @throws DALException
     */
    public List<LogItem> getNewLogs(Connection con) throws DALException {
        String sql = "SELECT LT.*, UT.* FROM LogTable LT "
                + "JOIN UserTable UT ON UT.id = LT.userId "
                + "WHERE LT.id > ? "
                + "ORDER BY LT.id;";
        try (PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            List<LogItem> logItems = new ArrayList();

            statement.setInt(1, currentId);

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Alert.AlertType at = getAlertType(rs.getString("type"));
                Date date = rs.getDate("date");
                currentId = rs.getInt("id");
                LogItem logItem = new LogItem(currentId, at, rs.getString("message"), rs.getString("firstName") + " " + rs.getString("lastName"), date);
                logItems.add(logItem);

            }
            return logItems;
        } catch (SQLException ex) {
            throw new DALException("SQL Error.", ex);
        }
    }

    /**
     * Adds new log to database
     * 
     * @param user User connected to the log
     * @param alertType Type of log
     * @param message Message in log
     * @param con Connection to database
     * @throws DALException 
     */
    public void addLog(User user, Alert.AlertType alertType, 
            String message, Connection con) throws DALException {
        String sql = "INSERT INTO LogTable VALUES(?,?,GETDATE(),?)";
        try (PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, alertType.toString());
            statement.setInt(2, user.getId());
            statement.setString(3, message);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DALException("SQL Error.", ex);
        }
    }

    /**
     * Returns enum AlertType based on String input
     *
     * @param type Type of enum
     * @return Enum AlertType
     */
    private Alert.AlertType getAlertType(String type) {
        switch (type) {
            case "ERROR":
                return Alert.AlertType.ERROR;
            case "INFORMATION":
                return Alert.AlertType.INFORMATION;
            case "CONFIRMATION":
                return Alert.AlertType.CONFIRMATION;
            case "WARNING":
                return Alert.AlertType.WARNING;
            default:
                return Alert.AlertType.NONE;
        }
    }

}
