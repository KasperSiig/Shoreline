/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.controlsfx.control.PopOver;
import shoreline.Main;
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

    private ObservableList<String> templateList = FXCollections.observableArrayList();
    private ObservableList<String> inputList = FXCollections.observableArrayList();
    private ObservableList<String> mappingList = FXCollections.observableArrayList();
    private HashMap<String, String> JSONmap = new HashMap<>();
    private HashMap<String, String> secondPriMap = new HashMap<>();
    private HashMap<String, String> defaultValuesMap = new HashMap<>();

    private ModelManager model;
    private String targetPath;
    private File inputFile;
    private Parent root;

    private HashMap<String, Integer> cellIndexMap;

    @FXML
    private JFXListView<String> lvInput;
    @FXML
    private JFXListView<String> lvMapOverview;
    @FXML
    private JFXListView<String> lvTemplate;
    @FXML
    private BorderPane bPane;
    @FXML
    private JFXTextField txtFileName;
    @FXML
    private MenuItem Delete;
    @FXML
    private Menu configMenu;
    @FXML
    private MenuItem Delete1;
    @FXML
    private JFXButton btnInput;
    @FXML
    private Label lblInfo;
    @FXML
    private TabPane tabPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    /**
     * Runs before the rest of the class.
     *
     * @param model
     */
    @Override
    public void postInit(ModelManager model) {
        this.model = model;
        defaultValuesMap = new HashMap<>();
        lvInput.setItems(inputList);
        lvTemplate.setItems(model.getConfigModel().getTemplateList());
        generateRightclickMenu();
        lvMappingSetup();
        makeConfigListener();
        try {
            tabPane.getTabs().add(makeTab(model, Window.View.SingleTask, "Single task"));
            tabPane.getTabs().add(makeTab(model, Window.View.Batch, "Batch"));
            tabPane.getTabs().add(makeTab(model, Window.View.logView, "Log"));
        } catch (GUIException ex) {
            Window.openExceptionWindow(ex.getMessage());
        }

//        TabPaneDetacher.create().makeTabsDetachable(tabPane);
        lvMapOverview.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(final ListView<String> list) {
                return new ListCell<String>() {
                    {
                        Text text = new Text();
                        text.wrappingWidthProperty().bind(list.widthProperty().subtract(15));
                        text.textProperty().bind(itemProperty());

                        setPrefWidth(0);
                        setGraphic(text);
                    }
                };
            }
        });
    }

    public Tab makeTab(ModelManager model, Window.View view, String name) throws GUIException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource(view.getView()));
            Node node = loader.load();

            IController cont = loader.getController();
            cont.postInit(model);

            Tab tab = new Tab(name);

            tab.setContent(node);

            return tab;
        } catch (IOException ex) {
            Window.openExceptionWindow(ex.getMessage());
        }
        return null;
    }

    /**
     * Sets the mapping listview to be multiple selection. sets the content of
     * the listview to be the mapping list makes a on click to check what the
     * name of the rightclick delete button needs to be.
     *
     */
    private void lvMappingSetup() {
        lvMapOverview.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lvMapOverview.setItems(mappingList);
        lvMapOverview.setOnMouseClicked((event) -> {
            if (lvMapOverview.getSelectionModel().getSelectedItems().size() == 1) {
                Delete.setText("Remove");
            } else if (lvMapOverview.getSelectionModel().getSelectedItems().size() > 1) {
                Delete.setText("Remove all");
            }
        });
    }

    /**
     * Adds a listener on the list of configs in model
     *
     */
    private void makeConfigListener() {
        model.getConfigModel().getConfigList().addListener(new ListChangeListener<Config>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Config> c) {
                c.next();
                if (c.wasUpdated() || c.wasAdded()) {
                    generateRightclickMenu();
                }
            }
        });
    }

    /**
     * handles the map event makes checks for null and if the item selected
     * already is linked. makes a temp string based on selected item adds the
     * item to a JSON array and adds the temp string to the observable list
     *
     * @param event
     */
    @FXML
    private void handleMap(ActionEvent event) {

        SelectionModel<String> inputSelection = lvInput.getSelectionModel();
        SelectionModel<String> templateSelection = lvTemplate.getSelectionModel();

        if (inputSelection.getSelectedItem() == null || templateSelection.getSelectedItem() == null) {
            return;
        }
        if (JSONmap.containsKey(templateSelection.getSelectedItem())) {
            Window.openSnack(templateSelection.getSelectedItem() + " is already mapped", bPane, "red");
            return;
        }
        if (JSONmap.containsValue(inputSelection.getSelectedItem())) {
            Window.openSnack(inputSelection.getSelectedItem() + " is already mapped", bPane, "red");
            return;
        }

        String temp = inputSelection.getSelectedItem() + " -> " + templateSelection.getSelectedItem();

        JSONmap.put(templateSelection.getSelectedItem(), inputSelection.getSelectedItem());
        mappingList.add(temp);
        Styling.clearRedOutline(lvMapOverview);
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
        File tempFile = fileChooser.showOpenDialog(bPane.getScene().getWindow());

        if (tempFile != null) {
            try {
                int i = tempFile.getName().lastIndexOf('.');
                if (i > 0) {
                    txtFileName.setText(tempFile.getName().substring(0, i));
                }
                lblInfo.setVisible(false);
                JSONmap.clear();
                inputList.clear();
                inputFile = tempFile;
                cellIndexMap = model.getConfigModel().getTitles(inputFile);
                getCellData();
                Styling.clearRedOutline(lvInput);
                Styling.clearRedOutline(btnInput);
                generateRightclickMenu();
            } catch (GUIException ex) {
                Window.openExceptionWindow(ex.getMessage());
            }
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
        if (openConfirmWindow("Are you sure, you want to delete the entire map", false)) {
            List<String> tempList;
            tempList = lvMapOverview.getItems();

            tempList.forEach((string) -> {
                String split = string.split(" -> ")[1].split("\n")[0];
                JSONmap.remove(split);
                secondPriMap.remove(split);
            });

            mappingList.removeAll(tempList);
            System.out.println(secondPriMap);
            System.out.println(JSONmap);
        }
    }

    /**
     * Runs a for each on a hashmap and adds the key to the inputlist then sorts
     * it.
     *
     */
    private void getCellData() {
        cellIndexMap.forEach((key, value) -> {
            inputList.add(key);
        });
        FXCollections.sort(inputList);
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
        validateCreateConfig();
    }

    /**
     * Handles the input file event on the input listview.
     *
     * runs the handleInputFile from the button.
     *
     * @param event
     */
    @FXML
    private void handleInputFile(MouseEvent event) {
        if (lvInput.getItems().isEmpty()) {
            handelInputFile(new ActionEvent());
        }
    }

    /**
     * Runs the delete selcetion method
     *
     * @param event
     */
    @FXML
    private void delMap(ActionEvent event) {
        DelSelection();
    }

    /**
     * Generates a menuitem for each of the configs in the config list in model.
     *
     */
    private void generateRightclickMenu() {

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
        configMenu.getItems().clear();
        temp.forEach((config) -> {
            MenuItem item = new MenuItem(config.getName());
            item.setOnAction((event) -> {
                if (inputFile == null) {
                    Styling.redOutline(lvInput);
                    Window.openSnack("Please select a import file", bPane, "red");
                    return;
                }
                mappingList.clear();
                JSONmap.clear();
                JSONmap.putAll(config.getHeaderMap());
                setInfoInlvMap(JSONmap);
                Window.openSnack("Config " + config.getName() + " was loaded", bPane, "blue");
            });
            configMenu.getItems().add(item);
        });
    }

    /**
     * Open confirm window
     *
     * @param msg
     * @param txtField
     * @return
     */
    private boolean openConfirmWindow(String msg, boolean txtField) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(Window.View.Confirm.getView()));
            root = fxmlLoader.load();

            ConfirmationWindowController cwc = fxmlLoader.getController();
            cwc.postInit(model);
            cwc.setInfo(msg, txtField);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Confirmation");
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.showAndWait();
            return cwc.getConfirmation();
        } catch (IOException ex) {
            Window.openExceptionWindow("Couldn't open confirmation window.");
        }
        return false;
    }

    /**
     * pulls the key and value from a hashmap and makes a temp string out of
     * them then adds it to the mapping list.
     *
     * @param map
     */
    private void setInfoInlvMap(HashMap map) {
        map.forEach((k, v) -> {
            String temp = v + " -> " + k;
            mappingList.add(temp);
        });
    }

    /**
     * Makes a temp list from the selected items if the list of selected is
     * larger then 1 it opens the confirmation window then runs a foreach on the
     * JSON map and deletes the item that fits the key and removes all elements
     * that exists in the mapping list
     *
     */
    private void DelSelection() {
        List<String> tempList;
        tempList = lvMapOverview.getSelectionModel().getSelectedItems();

        //Show confirm box before deleting
        if (tempList.size() > 1) {
            openConfirmWindow("Are you sure you want to delete all these links?", false);
        }
        tempList.forEach((string) -> {
            String split = string.split(" -> ")[1].split("\n")[0];
            JSONmap.remove(split);
            secondPriMap.remove(split);
        });

        mappingList.removeAll(tempList);
    }

    /**
     * Makes a temp map of the JSONmap then checks of the list of configs in the
     * model is empty. if it's empty is opens the create config window if it's
     * not empty it checks to see of the list already exists if it does it
     * returns if not it opens the create config window
     *
     * @param event
     */
    @FXML
    private void HandleCreateConfig(ActionEvent event) {
        validateCreateConfig();
    }

    private void validateCreateConfig() {
        if (checkRequired()) {
            return;
        }

        HashMap<String, String> tempCellMap = new HashMap<>(JSONmap);
        HashMap<String, String> tempSecondMap = new HashMap<>(secondPriMap);
        String name = txtFileName.getText();

        if (name.isEmpty()) {
            Window.openSnack("Please enter config name", bPane, "red");
            return;
        } else {

            for (Config config : model.getConfigModel().getConfigList()) {
                if (config.getName().equals(name)) {
                    Window.openSnack("The name already exists", bPane, "red");
                    return;
                }
            }
        }
        createConfig(inputFile, tempCellMap, name);
        Window.openSnack("Config " + name + " was created", bPane, "blue");
    }

    @FXML
    private void doubleClickMap(MouseEvent event) {
        if (event.getClickCount() % 2 == 0) {
            handleMap(new ActionEvent());
        }
    }

    private void createConfig(File file, HashMap cellMap, String name) {
        String extension = "";

        int i = file.getAbsolutePath().lastIndexOf('.');
        if (i > 0) {
            extension = file.getAbsolutePath().substring(i + 1);
        }
        Config config = new Config(name, extension, cellMap);
        config.setSecondPriority(secondPriMap);
        config.setDefaultValues(defaultValuesMap);
        try {
            model.getConfigModel().addToConfigList(config);
        } catch (GUIException ex) {
            Window.openExceptionWindow(ex.getMessage());
        }
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
        if (JSONmap.isEmpty()) {
            Styling.redOutline(lvMapOverview);
            hasFailed = true;
        } else {
            Styling.clearRedOutline(lvMapOverview);
        }
        if (hasFailed) {
            Window.openSnack("Could not create task. Missing input is highlighted.", bPane, "red", 4000);
        }
        return hasFailed;
    }

    @FXML
    private void handleSecondPri(ActionEvent event) {
        if (JSONmap.containsKey(lvTemplate.getSelectionModel().getSelectedItem())) {
            String inputSelection = lvInput.getSelectionModel().getSelectedItem();
            String templateSelection = lvTemplate.getSelectionModel().getSelectedItem();
            secondPriMap.put(templateSelection, inputSelection);

            String temp = null;
            for (String string : mappingList) {
                if (string.contains(templateSelection)) {
                    temp = string;
                }
            }
            mappingList.remove(temp);
            mappingList.add(temp += "\nSecond: " + inputSelection);
        }

    }

    @FXML
    private void handleDefaultValue(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(Window.View.defaultValue.getView()));
            root = fxmlLoader.load();

            DefaultVauleWindowController dvwc = fxmlLoader.getController();
            dvwc.postInit(model);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Default values");
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.showAndWait();
            defaultValuesMap = dvwc.getDefaultValues();
        } catch (IOException ex) {
            Window.openExceptionWindow("Couldn't open confirmation window.");
        }
    }

}
