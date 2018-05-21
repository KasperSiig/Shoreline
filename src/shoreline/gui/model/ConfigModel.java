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

    public ConfigModel(BorderPane borderPane, LogicManager logic) throws GUIException {
            this.borderPane = borderPane;
            this.logic = logic;
            
            this.templateList = FXCollections.observableArrayList("siteName", "assetSerialNumber",
                    "type", "externalWorkOrderId", "systemStatus", "userStatus", "name", "priority",
                    "latestFinishDate", "earliestStartDate", "latestStartDate", "estimatedTime");
            this.configList = FXCollections.observableArrayList(getAllConfigs());
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
    public void addToConfigList(Config config) throws GUIException {
        try {
            this.configList.add(config);
            save(config);
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }

    /**
     * @return List of all Configurations
     * @throws BLLException
     */
    public List<Config> getAllConfigs() throws GUIException {
        try {
            return logic.getConfigLogic().getAllConfigs();
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }

    /**
     * Save new configuration
     *
     * @param config Config to be saved
     * @throws BLLException
     */
    public void save(Config config) throws BLLException {
        logic.getConfigLogic().saveConfig(config);
    }
    
    /**
     * @param file File to get titles from
     * @return Titles from a given file
     * @throws GUIException
     */
    public HashMap<String, Integer> getTitles(File file) throws GUIException {
        try {
            return logic.getTaskLogic().getTitles(file);
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }
}
