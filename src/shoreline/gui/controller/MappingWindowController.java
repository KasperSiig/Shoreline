/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.gui.controller;

import com.jfoenix.controls.JFXListView;
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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SelectionModel;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import shoreline.be.ConvTask;
import shoreline.exceptions.GUIException;
import shoreline.gui.model.MainModel;
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
                Delete.setText("Delete");
            } else if (lvMapOverview.getSelectionModel().getSelectedItems().size() > 1) {
                Delete.setText("Delete all");
            }
        });
        if (!model.getConfigList().isEmpty()) {
            generateRightclickMenu();
        } else {
            configMenu.setDisable(true);
        }
    }

    @FXML
    private void handleMap(ActionEvent event) {

        SelectionModel<String> inputSelection = lvInput.getSelectionModel();
        SelectionModel<String> templateSelection = lvTemplate.getSelectionModel();

        String temp = inputSelection.getSelectedItem() + " -> " + templateSelection.getSelectedItem();

        if (JSONmap.containsKey(templateSelection.getSelectedItem())) {
            Window.openExceptionWindow(templateSelection.getSelectedItem() + " is already mapped");
            return;
        }
        if (JSONmap.containsValue(inputSelection.getSelectedItem())) {
            Window.openExceptionWindow(inputSelection.getSelectedItem() + " is already mapped");
            return;
        }

        JSONmap.put(templateSelection.getSelectedItem(), inputSelection.getSelectedItem());
        mappingList.add(temp);
    }

    @FXML
    private void handleTargetDir(ActionEvent event) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        File tempFile = dirChooser.showDialog(bPane.getScene().getWindow());
        if (tempFile != null) {
            targetPath = tempFile.getAbsolutePath();
        } else {
            return;
        }
    }

    @FXML
    private void handelInputFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File tempFile = fileChooser.showOpenDialog(bPane.getScene().getWindow());
        if (tempFile != null) {
            try {
                JSONmap.clear();
                inputFile = tempFile;
                cellIndexMap = model.getTitles(inputFile);
                getCellData();
            } catch (GUIException ex) {
                Window.openExceptionWindow("Whoops");
            }

        }
    }

    @FXML
    private void handleDelMap(ActionEvent event) {
        List<String> tempList;
        tempList = lvMapOverview.getSelectionModel().getSelectedItems();

        //Show confirm box before deleting
        for (String string : tempList) {
            JSONmap.remove(string.split(" -> ")[1]);
        }
        mappingList.removeAll(tempList);
    }

    private void getCellData() {
        cellIndexMap.forEach((key, value) -> {
            inputList.add(key);
        });
        FXCollections.sort(inputList);
    }

    @FXML
    private void handleCreateTask(ActionEvent event) {

        if (txtFileName.getText().equals("")) {
            Window.openExceptionWindow("Enter target filename");
            return;
        }

        String targetName = txtFileName.getText();
        String name = inputFile.getName() + " -> " + targetName + ".json";

        if (targetPath == null) {
            Window.openExceptionWindow("Choose a target path.");
            return;
        }

        ConvTask task = new ConvTask(cellIndexMap, JSONmap, name, inputFile, new File(targetPath + "\\" + targetName + ".json"));
        model.addToTaskList(task);
        
        if (!model.getConfigList().contains(JSONmap)) {
            openConfirmWindow("Do you want to save this map? if yes enter the name below", JSONmap);
        }
        try {
            model.addCallableToTask(task);
        } catch (GUIException ex) {
            Window.openExceptionWindow(ex.getMessage());
        }
    }

    @FXML
    private void handleInputFile(MouseEvent event) {
        if (lvInput.getItems().isEmpty()) {
            handelInputFile(new ActionEvent());
        }
    }

    @FXML
    private void delMap(ActionEvent event) {
        if (lvMapOverview.getSelectionModel().getSelectedItems().size() == 1) {
            mappingList.remove(lvMapOverview.getSelectionModel().getSelectedItem());
        } else if (lvMapOverview.getSelectionModel().getSelectedItems().size() > 1) {
            mappingList.removeAll(lvMapOverview.getSelectionModel().getSelectedItems());
        }
    }

    private void generateRightclickMenu() {
        configMenu.getItems().clear();
        model.getConfigList().forEach((config) -> {
            MenuItem item = new MenuItem(config.getName());
            item.setOnAction((event) -> {
                JSONmap.clear();
                JSONmap.putAll(config.getMap());
            });
            configMenu.getItems().add(item);
        });
    }

    private void openConfirmWindow(String msg, HashMap map) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(Window.View.Confirm.getView()));
            Parent root = fxmlLoader.load();

            ConfirmationWindowController cwc = fxmlLoader.getController();
            cwc.postInit(model);
            cwc.setInfo(msg, map);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Conforimation");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(MappingWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
