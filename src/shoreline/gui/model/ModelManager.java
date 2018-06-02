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
    private TemplateModel templateModel;

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

            taskModel = new TaskModel(logic);
            configModel = new ConfigModel(logic);
            logModel = new LogModel(logic);
            userModel = new UserModel(logic);
            batchModel = new BatchModel(logic);
            templateModel = new TemplateModel(logic);
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

    /**
     * @return Instance of TemplateModel
     */
    public TemplateModel getTemplateModel() {
        return templateModel;
    }
}
