package shoreline.gui.model;

import javafx.scene.layout.BorderPane;
import shoreline.bll.LogicManager;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.GUIException;

/**
 * Holds references to the different models
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class ModelManager {

    /* Models */
    private TaskModel taskModel;
    private ConfigModel configModel;
    private LogModel logModel;
    private UserModel userModel;
    private BatchModel batchModel;

    private BorderPane borderPane;

    private LogicManager logic;

    /**
     * Constructor for ModelManager
     *
     * @throws GUIException
     */
    public ModelManager() throws GUIException {
        try {
            this.logic = new LogicManager();

            taskModel = new TaskModel(borderPane, logic);
            configModel = new ConfigModel(borderPane, logic);
            logModel = new LogModel(borderPane, logic);
            userModel = new UserModel(borderPane, logic);
            batchModel = new BatchModel(borderPane, logic);
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }

    /**
     * @return BorderPane saved in ModelManager
     */
    public BorderPane getBorderPane() {
        return borderPane;
    }

    /**
     * Sets the BorderPane in model
     *
     * @param borderPane BorderPane to be saved
     */
    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    /**
     * Writes a property to the config.properties file
     *
     * @param key Key for the property
     * @param string Value for the property
     * @throws GUIException
     */
    public void setProperty(String key, String string) throws GUIException {
        try {
            logic.setProperty(key, string);
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }

    /**
     * Gets a property from the config.properties
     *
     * @param key Key from the property
     * @return Property based on key given
     * @throws GUIException
     */
    public String getProperty(String key) throws GUIException {
        try {
            return logic.getProperty(key);
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }

    /**
     * @return Instance of TaskModel
     */
    public TaskModel getTaskModel() {
        return taskModel;
    }

    /**
     * @return Instance of ConfigModel
     */
    public ConfigModel getConfigModel() {
        return configModel;
    }

    /**
     * @return Instance of LogModel 
     */
    public LogModel getLogModel() {
        return logModel;
    }

    /**
     * @return Instance of UserModel
     */
    public UserModel getUserModel() {
        return userModel;
    }
    
    /**
     * @return Instance of UserModel
     */
    public BatchModel getBatchModel() {
        return batchModel;
    }
    
    

}
