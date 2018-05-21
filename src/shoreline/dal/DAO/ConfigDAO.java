/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.dal.DAO;

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
import java.util.logging.Level;
import java.util.logging.Logger;
import shoreline.be.Config;
import shoreline.dal.DataBaseConnector;
import shoreline.exceptions.DALException;

/**
 *
 * @author kenne
 */
public class ConfigDAO {

    public ConfigDAO() {
    }

    /**
     * Fetches all configurations from the DB.
     *
     * @param con
     * @return
     * @throws DALException
     */
    public List<Config> getAllConfigs(Connection con) throws DALException {
        List<Config> configs = new ArrayList();
        String sql = "SELECT * FROM ConfigTable";
        try {

            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                HashMap hm = new HashMap();
                HashMap sm = new HashMap();
                HashMap dm = new HashMap();
                sql = "SELECT * FROM MapTable WHERE cfgId = ?";
                statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                statement.setInt(1, rs.getInt("id"));
                ResultSet rsMap = statement.executeQuery();
                while (rsMap.next()) {
                    hm.put(rsMap.getString("targetName"), rsMap.getString("sourceName"));
                    if(rsMap.getString("source2Name") != null){
                        sm.put(rsMap.getString("targetName"), rsMap.getString("source2Name"));
                        System.out.println(sm);
                    }
                    if(rsMap.getString("defaultName") != null){
                        sm.put(rsMap.getString("targetName"), rsMap.getString("defaultName"));
                        System.out.println(dm);
                    }
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
     *
     * @param name
     * @param extension
     * @param map
     * @throws DALException
     */
    public void saveConfig(Config config, Connection con) throws DALException {
        try {
            int id = 0;
            String sql = "INSERT INTO ConfigTable VALUES(?,?)";

            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, config.getName());
            statement.setString(2, config.getExtension());

            if (statement.executeUpdate() == 1) {
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                id = rs.getInt(1);
            }
            
            final int fid = id;
            
            HashMap<String, String> def = config.getDefaultValues();
            HashMap<String, String> sec = config.getSecondPriority();
            
            config.getHeaderMap().forEach((String k, String v) -> {
                try {
                    saveMap(con, k, v, sec, def, fid);
                } catch (DALException ex) {
                    Logger.getLogger(ConfigDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } catch (SQLException ex) {
            throw new DALException("SQL Error.", ex);
        }
    }

    /**
     * Saves the configurations map to MapTable in DB.
     *
     * @param con
     * @param k
     * @param v
     * @param id
     * @throws DALException
     */
    private void saveMap(Connection con,String hK, String hV, HashMap<String, String> second, HashMap<String, String> defaults, int id) throws DALException {
        try {
            String sec;
            String def;
            String sql = "INSERT INTO MapTable VALUES(?,?,?,?,?)";
            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, id);
            statement.setString(2, hV);
            statement.setString(3, hK);
            statement.setString(4, second.get(hK));
            statement.setString(5, defaults.get(hK));                      
            statement.execute();
        } catch (SQLException ex) {
            throw new DALException("Error saving config.", ex);
        }
    }

}
