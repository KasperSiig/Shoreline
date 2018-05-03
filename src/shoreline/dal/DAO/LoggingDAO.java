/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import shoreline.exceptions.DALException;

/**
 *
 * @author kenne
 */
public class LoggingDAO {

    private static int currentId = 0;

    public LoggingDAO() {

    }

    /**
     * Fetches all logs, and set currentId to the highest id fetched.
     *
     * @param con
     * @return
     * @throws DALException
     */
    public List<LogItem> getAllLogs(Connection con) throws DALException {
        String sql = "SELECT LT.*, UT.username FROM LogTable LT "
                + "JOIN UserTable UT ON UT.id = LT.userId "
                + "ORDER BY UT.id";
        try (PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            List<LogItem> logItems = new ArrayList();

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Alert.AlertType at = getAlertType(rs.getString("type"));
                currentId = rs.getInt("id");
                Date date = rs.getDate("date");
                LogItem logItem = new LogItem(currentId, at, rs.getString("message"), rs.getString("username"), date);
                logItems.add(logItem);

            }
            return logItems;
        } catch (SQLException ex) {
            throw new DALException("SQL Error.", ex);
        }
    }

    /**
     * Fetches logs that has an id higher than currentId and sets currentId to
     * the highest fetched.
     *
     * @param con
     * @return
     * @throws DALException
     */
    public List<LogItem> getNewLogs(Connection con) throws DALException {
        String sql = "SELECT LT.*, UT.username FROM LogTable LT "
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
                LogItem logItem = new LogItem(currentId, at, rs.getString("message"), rs.getString("username"), date);
                logItems.add(logItem);

            }
            System.out.println(logItems.size());
            return logItems;
        } catch (SQLException ex) {
            throw new DALException("SQL Error.", ex);
        }
    }

    /**
     * Adds a log to the DB.
     *
     * @param userId
     * @param type
     * @param message
     * @throws DALException
     */
    public void addLog(int userId, String type, String message, Connection con) throws DALException {
        String sql = "INSERT INTO LogTable VALUES(?,?,GETDATE(),?)";
        try (PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, type);
            statement.setInt(2, userId);
            statement.setString(3, message);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DALException("SQL Error.", ex);
        }
    }

    /**
     * Sets logItems' AlertType to the one equal type string in DB.
     *
     * @param type
     * @return
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
