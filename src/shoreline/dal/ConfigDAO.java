/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.dal;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import shoreline.be.Config;
import shoreline.exceptions.DALException;

/**
 *
 * @author kenne
 */
public class ConfigDAO {

    DataBaseConnector dbConnector;

    public ConfigDAO() throws DALException {
        try {
            dbConnector = new DataBaseConnector();
        } catch (IOException ex) {
            throw new DALException("Could not connect to database.", ex);
        }
    }
    /**
     * Fetches all configurations from the DB.
     * @return
     * @throws DALException 
     */
    public List<Config> getAllConfigs() throws DALException {
        List<Config> configs = new ArrayList();

        try (Connection con = dbConnector.getConnection()) {

            String sql = "SELECT * FROM ConfigTable";
            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                HashMap hm = new HashMap();
                sql = "SELECT * FROM MapTable WHERE cfgId = ?";
                statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                statement.setInt(1, rs.getInt("id"));
                ResultSet rsMap = statement.executeQuery();
                while (rsMap.next()) {
                    hm.put(rsMap.getString("target"), rsMap.getString("source"));
                }
                Config cfg = new Config(rs.getString("name"), rs.getString("extension"), hm);
                configs.add(cfg);
            }

        } catch (SQLException ex) {
            throw new DALException("SQL Error.", ex);
        }

        return configs;

    }
    /**
     * Saves the configuration to ConfigTable in the DB.
     * @param name
     * @param extension
     * @param map
     * @throws DALException 
     */
    public void saveConfig(String name, String extension, HashMap map) throws DALException {
        try (Connection con = dbConnector.getConnection()) {
            int id = 0;
            String sql = "INSERT INTO ConfigTable VALUES(?,?)";

            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, name);
            statement.setString(2, extension);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
            final int fid = id;
            map.forEach(new BiConsumer() {
                @Override
                public void accept(Object k, Object v) {
                    saveMap(con, k, v, fid);
                }
            });
        } catch (SQLException ex) {
            throw new DALException("SQL Error.", ex);
        }
    }
    /**
     * Saves the configurations map to MapTable in DB.
     * @param con
     * @param k
     * @param v
     * @param id
     * @throws DALException 
     */
    private void saveMap(Connection con, String k, String v, int id) throws DALException {
        try {
            String sql = "INSERT INTO MapTable VALUES(?,?,?)";
            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1,id);
            statement.setString(2,v);
            statement.setString(3, k);          
            statement.execute();         
        } catch (SQLException ex) {
            throw new DALException("SQL Error.", ex);
        }
    }

}
