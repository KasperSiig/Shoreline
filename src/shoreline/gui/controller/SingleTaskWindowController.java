package shoreline.gui.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import shoreline.Main;
import shoreline.be.ConvTask;
import shoreline.exceptions.GUIException;
import shoreline.gui.model.ModelManager;
import shoreline.statics.Window;
import shoreline.be.Config;
import shoreline.statics.Styling;

/**
 * FXML Controller class
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class SingleTaskWindowController implements Initializable, IController {

    private ModelManager model;
    private File importFile;
    private File targetFile;

    @FXML
    private BorderPane bPane;
    @FXML
    private JFXTextField txtFileName;
    @FXML
    private JFXComboBox<Config> comboConfig;
    @FXML
    private TextField txtImportPath;
    @FXML
    private TextField txtTargetPath;
    @FXML
    private BorderPane bPaneSplit;
    @FXML
    private HBox hBoxImport;
    @FXML
    private HBox hBoxTarget;
    @FXML
    private TabPane tabPane;

  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @Override
    public void postInit(ModelManager model) {
        this.model = model;

        try {
            comboConfig.getItems().addAll(model.getConfigModel().getAllConfigs());
            Window.openView(model, bPaneSplit, Window.View.TaskView, "center");
        } catch (GUIException ex) {
            Window.openExceptionWindow(ex.getMessage());
        }
        comboConfig.setDisable(true);
        try {
            tabPane.getTabs().add(makeTab(model, Window.View.Batch, "Batch"));
            tabPane.getTabs().add(makeTab(model, Window.View.Config, "Config"));
            tabPane.getTabs().add(makeTab(model, Window.View.logView, "Log"));
        } catch (GUIException ex) {
            Window.openExceptionWindow(ex.getMessage());
        }
    }
    
    /**
     * Creates tabs for the other views
     * 
     * @param model ModelManager
     * @param view View to be opened
     * @param name Name of the tab
     * @return Newly created Tab
     * @throws GUIException 
     */
    public Tab makeTab(ModelManager model, Window.View view, String name) throws GUIException {
        Tab tab = null;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource(view.getView()));
            Node node = loader.load();

            IController cont = loader.getController();
            cont.postInit(model);

            tab = new Tab(name);

            tab.setContent(node);

            return tab;
        } catch (IOException ex) {
            Window.openExceptionWindow(ex.getMessage());
        }
        return tab;
    }

    /**
     * Opens FileChooser for choosing input file, sets the importFile variable,
     * and refreshes the view accordingly
     */
    private void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All supported types", "*.xlsx", "*.csv"),
                new FileChooser.ExtensionFilter("XLSX files (.xlsx)", "*.xlsx"),
                new FileChooser.ExtensionFilter("CSV files (.csv)", "*.csv")
        // ADD NEW EXTENSIONS HERE, Seperate with comma (,)
        );
        File tempFile = fileChooser.showOpenDialog(bPane.getScene().getWindow());

        if (tempFile != null) {
            importFile = tempFile;
            txtImportPath.setText(importFile.getAbsolutePath());

            List temp = new ArrayList();
            try {
                for (Config config : model.getConfigModel().getAllConfigs()) {
                    String extension = "";

                    int i = importFile.getAbsolutePath().lastIndexOf('.');
                    if (i > 0) {
                        extension = importFile.getAbsolutePath().substring(i + 1);
                    }
                    if (config.getExtension().equals(extension)) {
                        temp.add(config);
                    }

                }
                comboConfig.getItems().setAll(temp);
                comboConfig.setDisable(false);
                String name = importFile.getName();
                txtFileName.setText(name.substring(0, name.lastIndexOf('.')));
            } catch (GUIException ex) {
                Window.openExceptionWindow(ex.getMessage());
            }
        }
    }

    /**
     * Handles input file choice
     * 
     * @param event 
     */
    @FXML
    private void handleInputFileBtn(ActionEvent event) {
        chooseFile();
    }

    /**
     * Handles target folder choice
     * @param event 
     */
    @FXML
    private void handleTargetFolderBtn(ActionEvent event) {
        chooseTarget();
    }

    /**
     * Opens DirectoryChooser for choosing the target folder.
     */
    private void chooseTarget() {
        DirectoryChooser dirChooser = new DirectoryChooser();
        File file = dirChooser.showDialog(null);

        if (file != null) {
            targetFile = file;
            txtTargetPath.setText(targetFile.getAbsolutePath());
        }
    }

    /**
     * Handles creating a task
     * 
     * @param event 
     */
    @FXML
    private void handleCreateTask(ActionEvent event) {
        createTask();
    }

    /**
     * Creates a new ConvTask from inputs, given from user.
     */
    private void createTask() {

        if (checkRequired()) {
            return;
        }

        String name = txtFileName.getText();

        Config config = comboConfig.getSelectionModel().getSelectedItem();
        config.setOutputHeaders(model.getConfigModel().getTemplateList());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());

        File tempFile = new File(targetFile + "\\" + date + " - " + name + ".json");

        try {
            ConvTask task = new ConvTask(name, importFile, tempFile, config);

            model.getTaskModel().addToPending(new TaskView(task));
            model.getTaskModel().addCallable(task);
        } catch (GUIException ex) {
            Window.openExceptionWindow(ex.getMessage());
        }
        txtFileName.clear();
        txtImportPath.clear();
        txtTargetPath.clear();
        comboConfig.getItems().clear();
        comboConfig.setDisable(true);

    }

    /**
     * Checks if there is an input file. Checks if the JSONmap is empty. Checks
     * if there is a file name. Checks if there is a target path.
     *
     * @return
     */
    private boolean checkRequired() {
        boolean hasFailed = false;
        if (importFile == null) {
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
        if (targetFile == null) {
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
            Window.openSnack("Could not create task. Missing input.", bPane, "red", 4000);
        }
        return hasFailed;
    }

}
