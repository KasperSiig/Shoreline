package shoreline.gui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SelectionModel;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import shoreline.be.Config;
import shoreline.exceptions.GUIException;
import shoreline.gui.model.ModelManager;
import shoreline.statics.Styling;
import shoreline.statics.Window;

/**
 * FXML Controller class
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class ConfigWindowController implements Initializable, IController {

    private ObservableList<String> inputList = FXCollections.observableArrayList();
    private ObservableList<String> mappingList = FXCollections.observableArrayList();

    private HashMap<String, Integer> titleIndexMap;

    private HashMap<String, String> primaryHeaders;
    private HashMap<String, String> secondaryHeaders;
    private HashMap<String, String> defaultValues;

    private ModelManager model;
    private File inputFile;
    private Config curConfig;

    @FXML
    private JFXListView<String> lvInput;
    @FXML
    private JFXListView<String> lvMapOverview;
    @FXML
    private JFXListView<String> lvTemplate;
    @FXML
    private BorderPane borderPane;
    @FXML
    private JFXTextField txtFileName;
    @FXML
    private MenuItem remove;
    @FXML
    private Menu configMenu;
    @FXML
    private JFXButton btnInput;
    @FXML
    private Label lblInfo;

    @FXML
    private Menu configMenuRight;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @Override
    public void postInit(ModelManager model) {
        this.model = model;
        this.primaryHeaders = new HashMap<>();
        this.secondaryHeaders = new HashMap<>();
        this.defaultValues = new HashMap<>();
        this.curConfig = null;

        lvInput.setItems(inputList);
        lvTemplate.setItems(model.getConfigModel().getTemplateList());
        lvMapOverview.setDisable(true);
        lvMapSetup();
        makeConfigListener();

        lvMapOverview.setCellFactory((final ListView<String> list) -> new ListCell<String>() {
            {
                Text text = new Text();
                text.wrappingWidthProperty().bind(list.widthProperty().subtract(15));
                text.textProperty().bind(itemProperty());

                setPrefWidth(0);
                setGraphic(text);
            }
        });
    }

    /**
     * Sets the mapping ListView to be multiple selection. Sets the content of
     * mapping ListView. Sets whether right-click menu should show Remove or
     * Remove All
     */
    private void lvMapSetup() {
        lvMapOverview.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lvMapOverview.setItems(mappingList);
        lvMapOverview.setOnMouseClicked((event) -> {
            if (lvMapOverview.getSelectionModel().getSelectedItems().size() == 1) {
                remove.setText("Remove");
            } else if (lvMapOverview.getSelectionModel().getSelectedItems().size() > 1) {
                remove.setText("Remove All");
            }
        });
    }

    /**
     * Adds a listener to the list of configs in model
     */
    private void makeConfigListener() {
        model.getConfigModel().getConfigList().addListener((ListChangeListener.Change<? extends Config> c) -> {
            c.next();
            if (c.wasUpdated() || c.wasAdded()) {
                generateConfigsInMenu(configMenu);
                generateConfigsInMenu(configMenuRight);
            }
        });
    }

    /**
     * Handles linking together primary headers
     *
     * @param event
     */
    @FXML
    private void handlePrimaryHeaders(ActionEvent event) {

        SelectionModel<String> inputSelection = lvInput.getSelectionModel();
        SelectionModel<String> templateSelection = lvTemplate.getSelectionModel();

        // Makes various checks on the selection
        if (inputSelection.getSelectedItem() == null || templateSelection.getSelectedItem() == null) {
            return;
        }
        if (primaryHeaders.containsKey(templateSelection.getSelectedItem())) {
            Window.openSnack(templateSelection.getSelectedItem() + " is already mapped", borderPane, "red");
            return;
        }
        if (primaryHeaders.containsValue(inputSelection.getSelectedItem())) {
            Window.openSnack(inputSelection.getSelectedItem() + " is already mapped", borderPane, "red");
            return;
        }

        String temp = templateSelection.getSelectedItem() + " ⇔ " + inputSelection.getSelectedItem();

        primaryHeaders.put(templateSelection.getSelectedItem(), inputSelection.getSelectedItem());
        mappingList.add(temp);
        Styling.clearRedOutline(lvMapOverview);
    }

    /**
     * Handles linking by double clicking
     *
     * @param event
     */
    @FXML
    private void handleDoubleClickLink(MouseEvent event) {
        if (event.getClickCount() % 2 == 0) {
            handlePrimaryHeaders(new ActionEvent());
        }
    }

    /**
     * Handles linking secondary headers
     *
     * @param event
     */
    @FXML
    private void handleSecondaryHeaders(ActionEvent event) {
        if (primaryHeaders.containsKey(lvTemplate.getSelectionModel().getSelectedItem())) {
            String inputSelection = lvInput.getSelectionModel().getSelectedItem();
            String templateSelection = lvTemplate.getSelectionModel().getSelectedItem();
            insertSecondaryHeader(templateSelection, inputSelection);
        } else {
            Window.openSnack("Primary header hasn't been chosen for this selection", borderPane, "red");
        }
    }

    /**
     * Inserts a secondary header in the HashMap, and to the ListView visually
     *
     * @param templateSelection Chosen template header
     * @param inputSelection Chosen input header
     */
    private void insertSecondaryHeader(String templateSelection, String inputSelection) {
        secondaryHeaders.put(templateSelection, inputSelection);

        // Checks if templateSelection is already chosen
        String temp = null;
        for (String string : mappingList) {
            if (string.contains(templateSelection)) {
                temp = string;
            }
        }

        int index = mappingList.indexOf(temp);
        mappingList.remove(temp);
        if (temp.contains("\n")) {
            temp = temp.split("\n")[0];
        }
        mappingList.add(index, temp += "\nSecond: " + inputSelection);
    }

    @FXML
    private void handleDefaultValue(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(Window.View.DefaultValue.getView()));
            Parent root = fxmlLoader.load();

            DefaultVauleWindowController dvwc = fxmlLoader.getController();
            dvwc.setInfo(defaultValues);
            dvwc.postInit(model);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Default values");
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.getIcons().add(new Image("/shoreline/res/ShorelineLogo.png"));
            stage.showAndWait();
        } catch (IOException ex) {
            Window.openExceptionWindow("Couldn't open confirmation window.");
        }
    }

    /**
     * Handles the input file event
     *
     * Makes a new file chooser with some filters and opens it if there is a
     * file selected it clears the JSON array and observable list and sets the
     * data.
     *
     * @param event
     */
    @FXML
    private void handelInputFile(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All supported types", "*.xlsx", "*.csv"),
                new FileChooser.ExtensionFilter("XLSX files (.xlsx)", "*.xlsx"),
                new FileChooser.ExtensionFilter("CSV files (.CSV)", "*.CSV")
        // ADD NEW EXTENSIONS HERE, Seperate with comma (,)
        );
        File tempFile = fileChooser.showOpenDialog(borderPane.getScene().getWindow());

        if (tempFile != null) {
            try {
                inputFile = tempFile;
                lblInfo.setVisible(false);
                primaryHeaders.clear();
                inputList.clear();
                titleIndexMap = model.getConfigModel().getTitles(inputFile);
                lvMapOverview.setDisable(false);
                insertInputTitles();
                if (curConfig != null) {
                    if (!checkConfig(curConfig)) {
                        System.out.println(curConfig);
                        return;
                    }
                }
                Styling.clearRedOutline(lvInput);
                Styling.clearRedOutline(btnInput);
                generateConfigsInMenu(configMenu);
                generateConfigsInMenu(configMenuRight);
            } catch (GUIException ex) {
                Window.openExceptionWindow(ex.getMessage());
            }
        }
    }

    /**
     * Runs a for each on a hashmap and adds the key to the inputlist then sorts
     * it.
     *
     */
    private void insertInputTitles() {
        titleIndexMap.forEach((key, value) -> {
            inputList.add(key);
        });
        FXCollections.sort(inputList);
    }

    /**
     * Handles the input file event on the input listview.
     *
     * runs the handleInputFile from the button.
     *
     * @param event
     */
    @FXML
    private void handleInputListClick(MouseEvent event) {
        if (lvInput.getItems().isEmpty()) {
            handelInputFile(new ActionEvent());
        }

        if (!lvInput.getItems().isEmpty()) {
            handleDoubleClickLink(event);
        }

    }

    /**
     * Handle delete map event
     *
     * opens a confirmation window if it returns yes make a temp list from the
     * listviews content then removes them from the JSON array and the map.
     *
     * @param event
     */
    @FXML
    private void handleDelMap(ActionEvent event) {
        if (openConfirmWindow("Are you sure, you want to delete the entire map")) {
            List<String> tempList;
            tempList = lvMapOverview.getItems();

            primaryHeaders.clear();
            secondaryHeaders.clear();
            defaultValues.clear();

            mappingList.removeAll(tempList);
        }
    }

    /**
     * Handles create task event
     *
     * if check required is false, make a name of the file, name for the task.
     * then creates a temp hashmap of the links and for the cellindexmap, then
     * make a task based on the info. add the task to the task list and the
     * callabletotask list.
     *
     * @param event
     */
    @FXML
    private void handleCreateConfig(ActionEvent event) {
        try {
            validateAndCreateConfig();
        } catch (GUIException ex) {
            Window.openExceptionWindow("Error creating configuration");
        }

    }

    private void validateAndCreateConfig() throws GUIException {
        if (checkRequired()) {
            return;
        }

        HashMap<String, String> tempPrimary = new HashMap<>(primaryHeaders);
        HashMap<String, String> tempSecondary = new HashMap<>(secondaryHeaders);
        HashMap<String, String> tempDefaults = new HashMap<>(defaultValues);

        String name = txtFileName.getText();
        String extension = "";

        if (name.isEmpty()) {
            Window.openSnack("Please enter config name", borderPane, "red");
            return;
        } else {

            for (Config config : model.getConfigModel().getConfigList()) {
                if (config.getName().equals(name)) {
                    if (openConfirmWindow("Name already exists, do you wish to overwrite?")) {
                        config.setPrimaryHeaders(tempPrimary);
                        config.setSecondaryHeaders(tempSecondary);
                        config.setDefaultValues(tempDefaults);
                        model.getConfigModel().updateConfig(config);
                        Window.openSnack("Config " + name + " was updated", borderPane, "blue");
                        return;
                    } else {
                        return;
                    }
                }
            }
        }
        int i = inputFile.getAbsolutePath().lastIndexOf('.');
        if (i > 0) {
            extension = inputFile.getAbsolutePath().substring(i + 1);
        }
        Config config = new Config(name, extension, tempPrimary, tempSecondary, tempDefaults);
        model.getConfigModel().addToConfigList(config);
        Window.openSnack("Config " + name + " was saved", borderPane, "blue");
    }

    /**
     * Checks if there is an input file. Checks if the JSONmap is empty. Checks
     * if there is a file name. Checks if there is a target path.
     *
     * @return
     */
    private boolean checkRequired() {
        boolean hasFailed = false;
        if (inputFile == null) {
            Styling.redOutline(btnInput);
            Styling.redOutline(lvInput);
            hasFailed = true;
        } else {
            Styling.clearRedOutline(btnInput);
            Styling.clearRedOutline(lvInput);
        }
        if (txtFileName.getText().equals("")) {
            Styling.redOutline(txtFileName);
            hasFailed = true;
        } else {
            Styling.clearRedOutline(txtFileName);
        }
        if (primaryHeaders.isEmpty()) {
            Styling.redOutline(lvMapOverview);
            hasFailed = true;
        } else {
            Styling.clearRedOutline(lvMapOverview);
        }
        if (hasFailed) {
            Window.openSnack("Could not create task. Missing input is highlighted.", borderPane, "red", 4000);
        }
        return hasFailed;
    }

    /**
     * Runs the delete selcetion method
     *
     * @param event
     */
    @FXML
    private void deleteSelectedLink(ActionEvent event) {
        deleteSelection();
    }

    /**
     * Makes a temp list from the selected items if the list of selected is
     * larger then 1 it opens the confirmation window then runs a foreach on the
     * JSON map and deletes the item that fits the key and removes all elements
     * that exists in the mapping list
     *
     */
    private void deleteSelection() {
        List<String> tempList;
        tempList = lvMapOverview.getSelectionModel().getSelectedItems();

        //Show confirm box before deleting
        if (tempList.size() > 1) {
            openConfirmWindow("Are you sure you want to delete all these links?");
        }
        tempList.forEach((string) -> {
            String split = string.split(" ⇔ ")[0];
            primaryHeaders.remove(split);
            secondaryHeaders.remove(split);
        });

        mappingList.removeAll(tempList);
    }

    /**
     * Generates a menuitem for each of the configs in the config list in model.
     *
     */
    private void generateConfigsInMenu(Menu menu) {

        List<Config> temp = new ArrayList();

        if (inputFile != null) {
            for (Config config : model.getConfigModel().getConfigList()) {
                String extension = "";

                int i = inputFile.getAbsolutePath().lastIndexOf('.');
                if (i > 0) {
                    extension = inputFile.getAbsolutePath().substring(i + 1);
                }
                if (config.getExtension().equals(extension)) {
                    temp.add(config);
                }

            }
        } else {
            try {
                temp.addAll(model.getConfigModel().getAllConfigs());
            } catch (GUIException ex) {
                Window.openExceptionWindow(ex.getMessage());
            }
        }
        menu.getItems().clear();
        temp.forEach((config) -> {
            MenuItem item = new MenuItem(config.getName());
            item.setOnAction((event) -> {
                if (!checkConfig(config)) {
                    // Do nothing
                } else {
                    if (inputFile == null) {
                        Styling.redOutline(lvInput);
                        Window.openSnack("Please select a import file", borderPane, "red");
                        return;
                    }
                    setInfo(config);
                }
            });
            menu.getItems().add(item);
        });
    }

    private boolean checkConfig(Config config) {
        List<String> primaryList = checkHeaders(config.getPrimaryHeaders(), inputList);
        List<String> secondaryList = checkHeaders(config.getSecondaryHeaders(), inputList);
        if (!primaryList.isEmpty() || !secondaryList.isEmpty()) {
            StringBuilder header = new StringBuilder("Following headers do not exist in input file: \n");
            primaryList.forEach((string) -> {
                header.append(string);
                header.append("\n");
            });

            secondaryList.forEach((string) -> {
                header.append(string);
                header.append("\n");
            });
            Window.openExceptionWindow(header.toString());
            mappingList.clear();
            return false;
        } else {
            return true;
        }
    }

    /**
     * Checks if headers are valid, according to given inputList
     *
     * @param inputHeaders
     * @param inputList
     * @return
     */
    private List<String> checkHeaders(HashMap<String, String> inputHeaders, List<String> inputList) {
        List<String> headers = new ArrayList();
        inputHeaders.forEach((key, value) -> {
            if (!inputList.contains(value)) {
                headers.add(value);
            }
        });
        return headers;
    }

    /**
     * Open confirm window
     *
     * @param message
     * @param txtField
     * @return
     */
    private boolean openConfirmWindow(String message) {
        boolean yes = false;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(Window.View.Confirm.getView()));
            Parent root = fxmlLoader.load();

            ConfirmationWindowController cwc = fxmlLoader.getController();
            cwc.postInit(model);
            cwc.setInfo(message);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Confirmation");
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.showAndWait();
            yes = cwc.getConfirmation();
        } catch (IOException ex) {
            Window.openExceptionWindow("Couldn't open confirmation window.");
        }
        return yes;
    }

    /**
     * pulls the key and value from a hashmap and makes a temp string out of
     * them then adds it to the mapping list.
     *
     * @param primaryHeaders
     */
    private void setInfoInlvMap(HashMap<String, String> primaryHeaders) {
        primaryHeaders.forEach((key, value) -> {
            String temp = key + " ⇔ " + value;
            mappingList.add(temp);
            if (secondaryHeaders.get(key) != null) {
                insertSecondaryHeader(key, secondaryHeaders.get(key));
            }
        });
    }

    public void setInfo(Config config) {
        mappingList.clear();
        primaryHeaders.clear();
        primaryHeaders.putAll(config.getPrimaryHeaders());
        secondaryHeaders.clear();
        secondaryHeaders.putAll(config.getSecondaryHeaders());
        defaultValues.clear();
        defaultValues.putAll(config.getDefaultValues());
        txtFileName.setText(config.getName());
        setInfoInlvMap(primaryHeaders);
        Window.openSnack("Config " + config.getName() + " was loaded\nChoose Input File", borderPane, "blue");
        curConfig = config;
    }

}
