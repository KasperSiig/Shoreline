package shoreline.be;

import java.sql.Date;
import javafx.scene.control.Alert;

/**
 * Contains information regarding Logs
 * 
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class LogItem {
    
    private int id;
    private Alert.AlertType type;
    private String message, user;
    private Date date;

    /**
     * Constructor for LogItem
     * 
     * @param id Id of LogItem
     * @param type Type of LogItem
     * @param message Message in LogItem
     * @param user What user has is connected to the log
     * @param date Date the log was created
     */
    public LogItem(int id, Alert.AlertType type, String message, String user, Date date) {
        this.id = id;
        this.type = type;
        this.message = message;
        this.user = user;
        this.date = date;
    }

    /**
     * @return Id of LogItem
     */
    public int getId() {
        return id;
    }
    
    /**
     * @return AlertType of LogItem
     */
    public Alert.AlertType getType() {
        return type;
    }

    /**
     * @return Message of LogItem
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return User connected to LogItem
     */
    public String getUser() {
        return user;
    }

    /**
     * @return Date LogItem was created
     */
    public Date getDate() {
        return date;
    }
}
