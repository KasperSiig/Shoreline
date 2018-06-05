package shoreline.gui.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.json.JSONObject;
import shoreline.be.Config;
import shoreline.bll.LogicManager;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.DALException;
import shoreline.exceptions.GUIException;
import shoreline.gui.controller.SettingsWindowController;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class ConfigModel {

    private LogicManager logic;

    private ObservableList<Config> configList;
    private ObservableList<String> templateList;
    private JSONObject templateJson;
    private Timer timer;

    public ConfigModel(LogicManager logic) throws GUIException {
        this.logic = logic;

        this.templateList = FXCollections.observableArrayList(getTemplateListFromDB());
        this.configList = FXCollections.observableArrayList(getAllConfigs());
        startTemplateTimer();
        setTemplateListener();
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
    public ObservableList<Config> getConfigList(){
        configList.clear();
        try {
            configList.addAll(getAllConfigs());
        } catch (GUIException ex) {
            Logger.getLogger(ConfigModel.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    private List<String> getTemplateListFromDB() throws GUIException {
        List<String> templateHeaders = new ArrayList();
        try {
            JSONObject template = logic.getTemplateLogic().getTemplate();
            templateJson = template;
            templateHeaders = getStrings(template.toMap());
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
        return templateHeaders;
    }

    private List<String> getStrings(Map<String, Object> JSONMap) {
        List<String> values = new ArrayList();
        JSONMap.forEach((key, value) -> {
            if (value instanceof HashMap) {
                values.addAll(getStrings((Map) value));
            } else {
                String stringValue = (String) value;
                if (stringValue.isEmpty()) {
                    values.add(key);
                }
            }
        });
        return values;
    }

    public JSONObject getTemplateJson() {
        return templateJson;
    }

    public void updateConfig(Config config) throws GUIException {
        try {
            logic.getConfigLogic().updateConfig(config);
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }

    public void deleteConfig(Config config) throws GUIException {
        try {
            logic.getConfigLogic().deleteConfig(config);
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }

    private void startTemplateTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    templateJson = logic.getTemplateLogic().getTemplate();
                    Platform.runLater(() -> {

                        try {
                            List<String> list = getTemplateListFromDB();
                            if (!list.equals(templateList)) {
                                templateList.clear();
                                templateList.setAll(getTemplateListFromDB());
                            }

                        } catch (GUIException ex) {
                            Logger.getLogger(ConfigModel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                } catch (BLLException ex) {
                    Logger.getLogger(ConfigModel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }, 0, 500);
    }

    public void stopTimer() {
        timer.cancel();
    }

    private void setTemplateListener() {

        templateList.addListener((ListChangeListener.Change<? extends String> c) -> {
            while (c.next()) {
                checkConfigs();
            }
        });
    }

    public void checkConfigs() {
        try {
            List<Config> configs = getAllConfigs();
            
            configs.forEach((config) -> {
                AtomicBoolean valid = new AtomicBoolean(true);
                HashMap<String, String> primaryHeaders = config.getPrimaryHeaders();
                HashMap<String, String> defaultValues = config.getDefaultValues();
                primaryHeaders.forEach((key, value) -> {
                    if (!templateList.contains(key)) {
                        valid.set(false);
                    }
                });
                defaultValues.forEach((key, value) -> {
                    if (!templateList.contains(key)) {
                        valid.set(false);
                    }
                });
                config.setValid(valid.get());
                try {
                    logic.getConfigLogic().updateConfig(config);
                } catch (BLLException ex) {
                    Logger.getLogger(ConfigModel.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
        } catch (GUIException ex) {
            Logger.getLogger(SettingsWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
