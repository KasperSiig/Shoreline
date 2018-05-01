/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.be;

import java.sql.Date;
import javafx.scene.control.Alert;

/**
 *
 * @author madst
 */
public class logItem {
    
    Alert.AlertType type;
    String message, user;
    Date date;

    public logItem(Alert.AlertType type, String message, String user, Date date) {
        this.type = type;
        this.message = message;
        this.user = user;
        this.date = date;
    }

    public Alert.AlertType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getUser() {
        return user;
    }

    public Date getDate() {
        return date;
    }
}
