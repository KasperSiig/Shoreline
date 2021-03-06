package shoreline.gui.model;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shoreline.be.Config;
import shoreline.bll.LogicManager;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.GUIException;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class ConfigModel {

    private LogicManager logic;

    private ObservableList<Config> configList;
    private ObservableList<String> templateList;

    public ConfigModel(LogicManager logic) throws GUIException {
        this.logic = logic;

        this.templateList = FXCollections.observableArrayList("siteName", "assetSerialNumber",
                "type", "externalWorkOrderId", "systemStatus", "userStatus", "name", "priority",
                "latestFinishDate", "earliestStartDate", "latestStartDate", "estimatedTime", "createdBy", "status");
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
     * @throws GUIException
     */
    public void addToConfigList(Config config) throws GUIException {
        save(config);
    }

    /**
     * @return List of all Configurations
     * @throws GUIException
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
     * @throws GUIException
     */
    public void save(Config config) throws GUIException {
        try {
            this.configList.add(config);
            logic.getConfigLogic().saveConfig(config);
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
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
