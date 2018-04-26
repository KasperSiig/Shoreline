/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.gui.controller;

import com.jfoenix.controls.JFXListView;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SelectionModel;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import shoreline.gui.model.MainModel;

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        lvMapOverview.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        lvTemplate.setItems(templateList);
        lvMapOverview.setItems(mappingList);

    }

    @Override
    public void postInit(MainModel model) {
        this.model = model;
        lvInput.setItems(inputList);

    }

    @FXML
    private void handleMap(ActionEvent event) {

        SelectionModel<String> inputSelection = lvInput.getSelectionModel();
        SelectionModel<String> templateSelection = lvTemplate.getSelectionModel();
        SelectionModel<String> mappignSelection = lvMapOverview.getSelectionModel();

        String temp = inputSelection.getSelectedItem() + " -> " + templateSelection.getSelectedItem();

        JSONmap.put(templateSelection.getSelectedItem(), inputSelection.getSelectedItem());
        System.out.println(JSONmap);
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
        System.out.println(targetPath);
    }

    @FXML
    private void handelInputFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File tempFile = fileChooser.showOpenDialog(bPane.getScene().getWindow());
        if (tempFile != null) {
            inputFile = tempFile;
//            XLXSConvStratTest();
            getCellData();
        }
    }

    @FXML
    private void handleDelMap(ActionEvent event) {
        List<String> tempList;
        tempList = lvMapOverview.getSelectionModel().getSelectedItems();

        mappingList.removeAll(tempList);
    }

    private void getCellData() {
        cellIndexMap.forEach((key,value) -> {
            inputList.add(key);
        });
        FXCollections.sort(inputList);
    }

}