package shoreline.dal.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import shoreline.be.Config;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class ConfigDAO {

    public ConfigDAO() {
    }

    /**
     * Fetches all configurations from the DB.
     *
     * @param con Connection to database
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
                HashMap<String, String> primaryHeaders = new HashMap();
                HashMap<String, String> secondaryHeaders = new HashMap();
                HashMap<String, String> defaultValues = new HashMap();

                sql = "SELECT * FROM MapTable WHERE cfgId = ?";
                statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                statement.setInt(1, rs.getInt("id"));
                ResultSet rsMap = statement.executeQuery();

                while (rsMap.next()) {
                    if (rsMap.getString("sourceName") != null) {
                        primaryHeaders.put(rsMap.getString("targetName"), rsMap.getString("sourceName"));
                    }
                    if (rsMap.getString("source2Name") != null) {
                        secondaryHeaders.put(rsMap.getString("targetName"), rsMap.getString("source2Name"));
                    }
                    if (rsMap.getString("defaultName") != null) {
                        defaultValues.put(rsMap.getString("targetName"), rsMap.getString("defaultName"));
                    }
                }
                Config config = new Config(rs.getString("name"), rs.getString("extension"), primaryHeaders, secondaryHeaders, defaultValues);
                configs.add(config);
            }
        } catch (SQLException ex) {
            throw new DALException("SQL Error.", ex);
        }

        return configs;

    }

    /**
     * Saves the configuration to ConfigTable in the DB.
     *
     * @param config Config to be saved
     * @param con Connection to database
     * @throws DALException
     */
    public void saveConfig(Config config, Connection con) throws DALException {
        String sql = "INSERT INTO ConfigTable VALUES(?,?)";
        try (PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int id = 0;

            statement.setString(1, config.getName());
            statement.setString(2, config.getExtension());

            if (statement.executeUpdate() == 1) {
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                id = rs.getInt(1);
            }

            // Values used inside lambda expressions have to be final
            final int fId = id;

            HashMap<String, String> defaultValues = config.getDefaultValues();
            HashMap<String, String> secondaryHeaders = config.getSecondaryHeaders();

            config.getPrimaryHeaders().forEach((key, value) -> {
                try {
                    saveMap(con, key, value, secondaryHeaders, defaultValues, fId);
                } catch (DALException ex) {
                    throw new RuntimeException(ex);
                }
            });
            setRemainingDefaults(con, defaultValues, id);
        } catch (SQLException ex) {
            throw new DALException("SQL Error.", ex);
        }
    }

    /**
     * Sets all the default values
     *
     * @param con Connection to database
     * @param defaultValues HashMap containg defaultValues
     * @param configId Id of config in databse
     */
    private void setRemainingDefaults(Connection con, HashMap<String, String> defaultValues, int configId) {
        defaultValues.forEach((key, value) -> {
            String sql = "INSERT INTO MapTable VALUES(?,?,?,?,?)";
            try (PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, configId);
                statement.setString(2, null);
                statement.setString(3, key);
                statement.setString(4, null);
                statement.setString(5, value);
                statement.execute();
            } catch (SQLException ex) {
                throw new RuntimeException("There was a problem saving default values", ex);
            }
        });

    }

    /**
     * Saves the configurations map to MapTable in DB.
     *
     * @param con Connection to database
     * @param sourceName
     * @param targetName
     * @param secondaryHeaders
     * @param defaultValues
     * @param id
     * @throws DALException
     */
    private void saveMap(Connection con, String sourceName, String targetName,
            HashMap<String, String> secondaryHeaders, HashMap<String, String> defaultValues, int id) throws DALException {
        String sql = "INSERT INTO MapTable VALUES(?,?,?,?,?)";
        try (PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, id);
            statement.setString(2, targetName);
            statement.setString(3, sourceName);
            statement.setString(4, secondaryHeaders.get(sourceName));
            statement.setString(5, defaultValues.get(sourceName));
            statement.execute();
        } catch (SQLException ex) {
            throw new DALException("Error saving config.", ex);
        }
    }

}
