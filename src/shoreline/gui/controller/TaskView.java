package shoreline.gui.controller;

import com.jfoenix.controls.JFXSpinner;
import java.io.IOException;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import shoreline.be.ConvTask;
import shoreline.gui.model.ModelManager;
import shoreline.statics.Window;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class TaskView extends BorderPane implements IController {

    private ModelManager model;
    private ConvTask task;

    @FXML
    private Label lblTaskName;
    @FXML
    private Label lblTargetDir;
    @FXML
    private Label lblStatus;
    @FXML
    private VBox vBox;
    
    // The selectedTasks list the TaskView is in
    private List<TaskView> curList;

    public TaskView(ConvTask task) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/shoreline/gui/view/TaskView.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
            this.task = task;
        } catch (IOException ex) {
            Window.openExceptionWindow(ex.getMessage());
        }
    }

    @Override
    public void postInit(ModelManager model) {
        this.model = model;
        setInfo(task);
        setWidthListener();
        lblTaskName.prefWidthProperty().bind(vBox.widthProperty());
    }

    /**
     * Sets info in the visual TaskView
     * 
     * @param task ConvTask to get info from
     */
    private void setInfo(ConvTask task) {
        lblStatus.textProperty().bind(task.getStatus());
        task.getStatus().addListener((observable, oldValue, newValue) -> {
            if (task.getStatus().getValue().equals(ConvTask.Status.Running.getValue())) {
                JFXSpinner spin = new JFXSpinner();
                vBox.getChildren().set(0, spin);
            } else {
                vBox.getChildren().set(0, lblStatus);
            }
        });
        lblTargetDir.setText(pathName(vBox.getWidth()));
        lblTaskName.setText(task.getName());

    }

    /**
     * Adds to the width, resizing the TaskView correctly
     */
    private void setWidthListener() {
        vBox.widthProperty().addListener((observable, oldValue, newValue) -> {
            lblTargetDir.setText(pathName(vBox.getWidth()));
        });
    }

    /**
     * Gets pathName, according to the size given
     * 
     * @param size Size to resize by
     * @return 
     */
    private String pathName(Number size) {
        int iSize = size.intValue();
        int characters = (iSize / 9);
        String path = task.getTarget().getAbsolutePath();
        String root = path.substring(0, 3);
        if (path.length() > characters) {
            path = root + "..." + path.substring(path.length() - characters);
        }
        return path;
    }

    /**
     * @return ConvTask contained in TaskView
     */
    public ConvTask getTask() {
        return task;
    }

    /**
     * @return Get text from lblTaskName
     */
    public String getLblTaskName() {
        return lblTaskName.getText();
    }

    /**
     * @return Get the selectedTasks list the TaskView is in
     */
    public List<TaskView> getCurList() {
        return curList;
    }

    /**
     * Set what selectedTasks list the TaskView is in
     * 
     * @param curList 
     */
    public void setCurList(List<TaskView> curList) {
        this.curList = curList;
    }
    
    
}
