/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.dal.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import shoreline.exceptions.DALException;

/**
 *
 * @author kaspe
 */
public class TemplateDAO {

    public void save(JSONObject jsonObject, Connection con) throws DALException {
        List<String[]> contents = recursiveSave(jsonObject.toMap(), null);
        for (String[] array : contents) {
            saveHeader(con, array[0], array[1], array[2]);
        }
    }

    private List<String[]> recursiveSave(Map<String, Object> map, String parentHeader) {
        List<String[]> contents = new ArrayList();
        map.forEach((key, value) -> {
            String[] array = new String[3];
            if (value instanceof HashMap) {
                contents.addAll(recursiveSave((HashMap) value, key));
            } else {
                array[0] = key;
                array[1] = (String) value;
                array[2] = parentHeader;
                contents.add(array);
            }
        });
        return contents;
    }

    private void saveHeader(Connection con, String header, String value, String parentHeader) throws DALException {
        String sql = "INSERT INTO Template VALUES(?,?,?)";
        try (PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, header);
            statement.setString(2, value);
            statement.setString(3, parentHeader);
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TemplateDAO.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

}
