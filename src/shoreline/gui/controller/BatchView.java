package shoreline.gui.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import shoreline.be.Batch;
import shoreline.gui.model.ModelManager;
import shoreline.statics.Window;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class BatchView extends BorderPane implements IController {

    private ModelManager model;
    private Batch batch;

    @FXML
    private Label lblTaskName;
    @FXML
    private Label lblTargetDir;
    @FXML
    private Label lblInputDir;
    @FXML
    private Label lblPending;
    @FXML
    private Label lblHandled;
    @FXML
    private Label lblFailed;
    @FXML
    private VBox vBox;

    public BatchView(Batch batch) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/shoreline/gui/view/BatchView.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
            this.batch = batch;
        } catch (IOException ex) {
            Window.openExceptionWindow("Could not create batch view");
        }
    }

    @Override
    public void postInit(ModelManager model) {
        this.model = model;
        setInfo(batch);
        setWidthListener();
        lblTaskName.prefWidthProperty().bind(vBox.widthProperty());
    }

    /**
     * Sets the visual info in the BatchView
     * 
     * @param batch Batch to get info from
     */
    private void setInfo(Batch batch) {
        pathName(vBox.getWidth(), lblTargetDir, batch.getSourceDir().getAbsolutePath());
        pathName(vBox.getWidth(), lblInputDir, batch.getTargetDir().getAbsolutePath());
        lblTaskName.setText(batch.getName());

        bindTextProperties(batch);
    }

    /**
     * Binds the text properties in BatchView
     * 
     * @param batch 
     */
    private void bindTextProperties(Batch batch) {
        lblPending.textProperty().bind(batch.getFilesPending().asString());
        lblFailed.textProperty().bind(batch.getFilesFailed().asString());
        lblHandled.textProperty().bind(batch.getFilesHandled().asString());
    }

    /**
     * Sets a listener on the width, resizing the pathName
     */
    private void setWidthListener() {
        vBox.widthProperty().addListener((observable, oldValue, newValue) -> {
            pathName(newValue, lblTargetDir, batch.getSourceDir().getAbsolutePath());
            pathName(newValue, lblInputDir, batch.getTargetDir().getAbsolutePath());
        });
    }

    /**
     * Gets the short version of pathName, from given number
     * 
     * @param size
     * @param lbl
     * @param path 
     */
    private void pathName(Number size, Label lbl, String path) {
        int iSize = size.intValue();
        int characters = (iSize / 9);
        String root = path.substring(0, 3);
        if (path.length() > characters) {
            path = root + "..." + path.substring(path.length() - characters);
        }
        lbl.setText(path);
    }

    /**
     * @return Get Batch contained in BatchView
     */
    public Batch getBatch() {
        return batch;
    }

}
