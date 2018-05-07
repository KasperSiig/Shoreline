/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.gui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SelectionModel;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import shoreline.be.Config;
import shoreline.be.ConvTask;
import shoreline.bll.ThreadPool;
import shoreline.exceptions.GUIException;
import shoreline.gui.model.MainModel;
import shoreline.statics.Styling;
import shoreline.statics.Window;

/**
 * FXML Controller class
 *
 * @author madst
 */
public class MappingWindowController implements Initializable, IController {

    ObservableList<String> templateList = FXCollections.observableArrayList();
    ObservableList<String> inputList = FXCollections.observableArrayList();
    ObservableList<String> mappingList = FXCollections.observableArrayList();
    HashMap<String, String> JSONmap = new HashMap<>();

    MainModel model;
    String targetPath;
    File inputFile;

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
    private JFXButton btnTarget;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @Override
    public void postInit(MainModel model) {
        this.model = model;
        lvInput.setItems(inputList);
        lvTemplate.setItems(model.getTemplateList());
        lvMapOverview.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lvMapOverview.setItems(mappingList);
        lvMapOverview.setOnMouseClicked((event) -> {
            if (lvMapOverview.getSelectionModel().getSelectedItems().size() == 1) {
                Delete.setText("Remove");
            } else if (lvMapOverview.getSelectionModel().getSelectedItems().size() > 1) {
                Delete.setText("Remove all");
            }
        });
        if (!model.getConfigList().isEmpty()) {
            generateRightclickMenu();
        }

        model.getConfigList().addListener(new ListChangeListener<Config>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Config> c) {
                c.next();
                if (c.wasUpdated() || c.wasAdded()) {
                    generateRightclickMenu();
                }
            }
        });

        model.getBorderPane().getScene().getWindow().setOnCloseRequest((event) -> {
            ThreadPool tPool = ThreadPool.getInstance();
            tPool.closeThreadPool();
            model.getTimer().cancel();
        });

    }

    @FXML
    private void handleMap(ActionEvent event) {

        SelectionModel<String> inputSelection = lvInput.getSelectionModel();
        SelectionModel<String> templateSelection = lvTemplate.getSelectionModel();

        if (inputSelection.getSelectedItem() == null) {
            return;
        }

        if (templateSelection.getSelectedItem() == null) {
            return;
        }

        if (inputSelection.getSelectedItem() == null && templateSelection.getSelectedItem() == null) {
            return;
        }
        if (JSONmap.containsKey(templateSelection.getSelectedItem())) {
            Window.openExceptionWindow(templateSelection.getSelectedItem() + " is already mapped");
            return;
        }
        if (JSONmap.containsValue(inputSelection.getSelectedItem())) {
            Window.openExceptionWindow(inputSelection.getSelectedItem() + " is already mapped");
            return;
        }

        String temp = inputSelection.getSelectedItem() + " -> " + templateSelection.getSelectedItem();

        JSONmap.put(templateSelection.getSelectedItem(), inputSelection.getSelectedItem());
        mappingList.add(temp);
        Styling.clearRedOutline(lvMapOverview);
    }

    @FXML
    private void handleTargetDir(ActionEvent event) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        File tempFile = dirChooser.showDialog(bPane.getScene().getWindow());
        if (tempFile != null) {
            targetPath = tempFile.getAbsolutePath();
            Styling.clearRedOutline(btnTarget);
        } else {
            return;
        }
    }

    @FXML
    private void handelInputFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All supported types", "*.xlsx"),
                new FileChooser.ExtensionFilter("XLSX files (.xlsx)", ".xlsx")
        // ADD NEW EXTENSIONS HERE, Seperate with comma (,)
        );
        File tempFile = fileChooser.showOpenDialog(bPane.getScene().getWindow());

        if (tempFile != null) {
            try {
                JSONmap.clear();
                inputList.clear();
                inputFile = tempFile;
                cellIndexMap = model.getTitles(inputFile);
                getCellData();
                Styling.clearRedOutline(lvInput);
                Styling.clearRedOutline(btnInput);
            } catch (GUIException ex) {
                Window.openExceptionWindow("Whoops");
            }
        }
    }

    @FXML
    private void handleDelMap(ActionEvent event) {
        if (openConfirmWindow("Are you sure, you want to delete the entire map", null, null, false)) {
            List<String> tempList;
            tempList = lvMapOverview.getItems();

            //Show confirm box before deleting
            tempList.forEach((string) -> {
                JSONmap.remove(string.split(" -> ")[1]);
            });

            System.out.println(JSONmap);
            mappingList.removeAll(tempList);
        }
    }

    private void getCellData() {
        cellIndexMap.forEach((key, value) -> {
            inputList.add(key);
        });
        FXCollections.sort(inputList);
    }

    @FXML
    private void handleCreateTask(ActionEvent event) {
        try {
            if (checkRequired()) {
                return;
            }

            String targetName = txtFileName.getText();
            String name = inputFile.getName() + " -> " + targetName + ".json";

            HashMap temp = new HashMap(JSONmap);
            HashMap cellTemp = new HashMap(cellIndexMap);
            ConvTask task = new ConvTask(cellTemp, temp, name, inputFile, new File(targetPath + "\\" + targetName + ".json"));

            model.addToTaskList(task);
            model.addCallableToTask(task);

            Window.openSnack("Task " + task.getName() + " was created", bPane);
            if (task == null) {
                model.addLog(model.getUser().getId(), Alert.AlertType.ERROR, model.getUser().getfName() + "Tried to create a task and it failed");
            }
        } catch (GUIException ex) {
            Window.openExceptionWindow("There was truble making a task", ex.getStackTrace());
        }
    }

    private boolean checkRequired() {
        if (inputFile == null) {
            Styling.redOutline(lvInput);
            Styling.redOutline(btnInput);
            Window.openSnack("Please choose an input file", bPane);
            return true;
        } else {
            Styling.clearRedOutline(lvInput);
            Styling.clearRedOutline(btnInput);
        }
        if (JSONmap.isEmpty()) {
            Styling.redOutline(lvMapOverview);
            Window.openSnack("Please make a link or load a config", bPane);
            return true;
        } else {
            Styling.clearRedOutline(lvMapOverview);
        }
        if (txtFileName.getText().equals("")) {
            Styling.redOutline(txtFileName);
            Window.openSnack("Please enter a file name", bPane);
            return true;
        } else {
            Styling.clearRedOutline(txtFileName);
        }
        if (targetPath == null) {
            Styling.redOutline(btnTarget);
            Window.openSnack("Please choose a target folder", bPane);
            return true;
        } else {
            Styling.clearRedOutline(btnTarget);
        }
        return false;
    }

    @FXML
    private void handleInputFile(MouseEvent event) {
        if (lvInput.getItems().isEmpty()) {
            handelInputFile(new ActionEvent());
        }
    }

    @FXML
    private void delMap(ActionEvent event) {
        DelSelection();
    }

    private void generateRightclickMenu() {
        configMenu.getItems().clear();
        model.getConfigList().forEach((config) -> {
            MenuItem item = new MenuItem(config.getName());
            item.setOnAction((event) -> {
                if (inputFile == null) {
                    Styling.redOutline(lvInput);
                    return;
                }
                mappingList.clear();
                JSONmap.clear();
                JSONmap.putAll(config.getMap());
                setInfoInlvMap(JSONmap);
                Window.openSnack("Config " + config.getName() + " was loaded", bPane);
            });
            configMenu.getItems().add(item);
        });
    }

    private boolean openConfirmWindow(String msg, HashMap map, File file, boolean txtField) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(Window.View.Confirm.getView()));
            Parent root = fxmlLoader.load();

            ConfirmationWindowController cwc = fxmlLoader.getController();
            cwc.postInit(model);
            cwc.setInfo(msg, map, file, txtField);

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

    private void setInfoInlvMap(HashMap map) {
        map.forEach((k, v) -> {
            String temp = v + " -> " + k;
            mappingList.add(temp);
        });
    }

    private void DelSelection() {
        List<String> tempList;
        tempList = lvMapOverview.getSelectionModel().getSelectedItems();

        //Show confirm box before deleting
        tempList.forEach((string) -> {
            JSONmap.remove(string.split(" -> ")[1]);
        });

        mappingList.removeAll(tempList);
    }

    @FXML
    private void HandleCreateConfig(ActionEvent event) {
        HashMap<String, String> temp = new HashMap<>(JSONmap);

        if (!model.getConfigList().isEmpty()) {
            for (Config config : model.getConfigList()) {
                if (config.getMap() == temp) {
                    return;
                } else {
                    openConfirmWindow("Do you want to save this map, if yes please enter name blow", temp, inputFile, true);
                    return;
                }
            }
        } else {
            openConfirmWindow("Do you want to save this map, if yes please enter name blow", temp, inputFile, true);
        }
        Window.openSnack("Config created", bPane);
    }

}
