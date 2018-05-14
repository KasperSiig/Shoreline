package shoreline.gui.model;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.BorderPane;
import shoreline.be.Config;
import shoreline.bll.LogicManager;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.GUIException;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class ConfigModel {

    private BorderPane borderPane;
    private LogicManager logic;

    private ObservableList<Config> configList;
    private ObservableList<String> templateList;

    public ConfigModel(BorderPane borderPane, LogicManager logic) {
        try {
            this.borderPane = borderPane;
            this.logic = logic;
            
            this.templateList = FXCollections.observableArrayList("siteName", "assetSerialNumber",
                    "type", "externalWorkOrderId", "systemStatus", "userStatus", "name", "priority",
                    "latestFinishDate", "earliestStartDate", "latestStartDate", "estimatedTime");
            this.configList = FXCollections.observableArrayList(getAllConfigs());
        } catch (BLLException ex) {
            Logger.getLogger(ConfigModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return Gets the titles from the given template
     */
    public ObservableList<String> getTemplateList() {
        return templateList;
    }

    /**
     * @return List of configurations
     */
    public ObservableList<Config> getConfigList() {
        return configList;
    }

    /**
     * Adds a configuration to the configuration list
     *
     * @param config Configuration to be added
     */
    public void addToConfigList(Config config) {
        try {
            this.configList.add(config);
            save(config.getName(), config.getExtension(), config.getMap());
        } catch (BLLException ex) {
            Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return List of all Configurations
     * @throws BLLException
     */
    public List<Config> getAllConfigs() throws BLLException {
        return logic.getAllConfigs();
    }

    /**
     * Save new configuration
     *
     * @param name Name of configuration
     * @param extension Extension of the file, fit to config
     * @param map Mapping of the configuration
     * @throws BLLException
     */
    public void save(String name, String extension, HashMap map) throws BLLException {
        logic.saveConfig(name, extension, map);
    }
    
    /**
     * @param file File to get titles from
     * @return Titles from a given file
     * @throws GUIException
     */
    public HashMap<String, Integer> getTitles(File file) throws GUIException {
        try {
            return logic.getTitles(file);
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }
}
