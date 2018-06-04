package shoreline.gui.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;
import shoreline.be.Batch;
import shoreline.be.Config;
import shoreline.exceptions.GUIException;
import shoreline.gui.model.ModelManager;
import shoreline.statics.Styling;
import shoreline.statics.Window;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class BatchWindowController implements Initializable, IController {

    @FXML
    private BorderPane borderPane;
    @FXML
    private HBox hBoxImport;
    @FXML
    private TextField txtImportPath;
    @FXML
    private HBox hBoxTarget;
    @FXML
    private TextField txtTargetPath;
    @FXML
    private JFXTextField txtFileName;
    @FXML
    private JFXComboBox<Config> comboConfig;
    @FXML
    private BorderPane bPaneSplit;

    private ModelManager model;
    private File targetFolder;
    private File inputFolder;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @Override
    public void postInit(ModelManager model) {
        this.model = model;
        try {
            Window.openView(model, bPaneSplit, Window.View.BatchTask, "center");
            comboConfig.setItems(model.getConfigModel().getConfigList());
        } catch (GUIException ex) {
            Logger.getLogger(BatchWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        comboConfig.setDisable(true);
    }

    /**
     * Handles choosing target folder
     *
     * @param event
     */
    @FXML
    private void handleTargetFolderBtn(ActionEvent event) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        File file = dirChooser.showDialog(null);

        if (file != null) {
            targetFolder = file;
            txtTargetPath.setText(targetFolder.getAbsolutePath());
        }
    }

    /**
     * Handles choosing input folder
     *
     * @param event
     */
    @FXML
    private void handleInputFolderBtn(ActionEvent event) {
        try {
            DirectoryChooser dirChooser = new DirectoryChooser();
            File file = dirChooser.showDialog(null);

            model.getConfigModel().getAllConfigs();
            comboConfig.setItems(model.getConfigModel().getConfigList());

            if (file != null) {
                inputFolder = file;
                txtImportPath.setText(inputFolder.getAbsolutePath());
            }
            comboConfig.setDisable(false);
        } catch (GUIException ex) {
            Window.openExceptionWindow("Error choosing input folder");
        }
    }

    /**
     * Handles creating new batch
     * 
     * @param event 
     */
    @FXML
    private void handleCreateBatch(ActionEvent event) {
        if (checkRequired()) {
            return;
        }

        try {
            Config config = comboConfig.getSelectionModel().getSelectedItem();
            config.setTemplate(model.getConfigModel().getTemplateJson());
            Batch batch = new Batch(inputFolder, targetFolder, txtFileName.getText(), config);
            addListenerForNotification(batch);
            model.getBatchModel().addToBatches(batch);
            Window.openSnack("New batch " + batch.getName() + " was created", borderPane, "blue");
        } catch (GUIException ex) {
            Window.openExceptionWindow("Error creating new batch");
        }
        txtFileName.clear();
        txtImportPath.clear();
        txtTargetPath.clear();
        comboConfig.setDisable(true);
        comboConfig.getSelectionModel().clearSelection();

    }

    /**
     * Checks if there is an input file. 
     * Checks if the JSONmap is empty. 
     * Checks if there is a file name. 
     * Checks if there is a target path.
     *
     * @return
     */
    private boolean checkRequired() {
        boolean hasFailed = false;
        if (inputFolder == null) {
            Styling.redOutline(hBoxImport);
            hasFailed = true;
        } else {
            Styling.clearRedOutline(hBoxImport);
        }
        if (txtFileName.getText().equals("")) {
            Styling.redOutline(txtFileName);
            hasFailed = true;
        } else {
            Styling.clearRedOutline(txtFileName);
        }
        if (inputFolder == null) {
            Styling.redOutline(hBoxTarget);
            hasFailed = true;
        } else {
            Styling.clearRedOutline(hBoxTarget);
        }
        if (comboConfig.getSelectionModel().getSelectedItem() == null) {
            Styling.redOutline(comboConfig);
            hasFailed = true;
        } else {
            Styling.clearRedOutline(comboConfig);
        }
        if (hasFailed) {
            Window.openSnack("Could not create task. Missing input is highlighted.", borderPane, "red", 4000);
        }
        return hasFailed;
    }

    /**
     * Adds listener to show notifications, when new files are added to batch
     * and when all files are done
     * 
     * @param batch 
     */
    private void addListenerForNotification(Batch batch) {
        batch.getFilesPending().addListener((observable, oldValue, newValue) -> {
            if (oldValue.intValue() == 0 && newValue.intValue() == 1) {
                TrayNotification tray = new TrayNotification("Batch: " + batch.getName(),
                        "Started converting new files", NotificationType.SUCCESS);
                tray.setAnimationType(AnimationType.POPUP);
                tray.showAndDismiss(Duration.seconds(3));
            }
            if (oldValue.intValue() == 1 && newValue.intValue() == 0) {
                TrayNotification tray = new TrayNotification("Batch: " + batch.getName(),
                        "Done converting files", NotificationType.SUCCESS);
                tray.setAnimationType(AnimationType.POPUP);
                tray.showAndDismiss(Duration.seconds(3));
            }
        });

    }

}
