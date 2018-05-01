/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import shoreline.be.LogItem;
import shoreline.exceptions.DALException;

/**
 *
 * @author kenne
 */
public class LoggingDAO {

    DataBaseConnector dbConnector;
    private static int currentId = 0;

    public LoggingDAO() throws DALException {

        try {
            dbConnector = new DataBaseConnector();
        } catch (IOException ex) {
            throw new DALException("Could not connect to database.", ex);
        }
    }

    /**
     * Fetches all logs, and set currentId to the highest id fetched.
     * @return
     * @throws DALException 
     */
    public List<LogItem> getAllLogs() throws DALException {
        try (Connection con = dbConnector.getConnection()) {
            List<LogItem> logItems = new ArrayList();
            String sql = "SELECT LT.*, UT.username FROM LogTable LT "
                    + "JOIN UserTable UT ON UT.id = LT.userId "
                    + "ORDER BY UT.id";

            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Alert.AlertType at = getAlertType(rs.getString("type"));
                Date date = rs.getDate("date");
                LogItem logItem = new LogItem(at, rs.getString("message"), rs.getString("username"), date);
                logItems.add(logItem);
                currentId = rs.getInt("id");
            }
            return logItems;
        } catch (SQLException ex) {
            throw new DALException("SQL Error.", ex);
        }
    }

    /**
     * Fetches logs that has an id higher than currentId and sets currentId to the highest fetched.
     * @return
     * @throws DALException 
     */
    public List<LogItem> getNewLogs() throws DALException {
        try (Connection con = dbConnector.getConnection()) {
            List<LogItem> logItems = new ArrayList();
            String sql = "SELECT LT.*, UT.username FROM LogTable LT "
                    + "JOIN UserTable UT ON UT.id = LT.userId "
                    + "WHERE LT.id > ? "
                    + "ORDER BY LT.id;";

            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            statement.setInt(1, currentId);

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Alert.AlertType at = getAlertType(rs.getString("type"));
                Date date = rs.getDate("date");
                LogItem logItem = new LogItem(at, rs.getString("message"), rs.getString("username"), date);
                logItems.add(logItem);
                currentId = rs.getInt("id");
            }
            return logItems;
        } catch (SQLException ex) {
            throw new DALException("SQL Error.", ex);
        }
    }
    
    /**
     * Adds a log to the DB. 
     * @param userId
     * @param type
     * @param message
     * @throws DALException 
     */
    public void addLog(int userId, String type, String message) throws DALException{
        try (Connection con = dbConnector.getConnection() ){
            
            String sql = "INSERT INTO LogTable VALUES(?,?,GETDATE(),?)";
            
            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            statement.setString(1, type);
            statement.setInt(2, userId);
            statement.setString(3, message);
        } catch (SQLException ex) {
            throw new DALException("SQL Error.", ex);
        }
    }
    
    
    /**
     * Sets logItems' AlertType to the one equal type string in DB.
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
