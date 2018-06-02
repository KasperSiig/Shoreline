/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.gui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.json.JSONObject;
import shoreline.exceptions.GUIException;
import shoreline.gui.model.ModelManager;
import shoreline.statics.Window;

/**
 * FXML Controller class
 *
 * @author kaspe
 */
public class JSONTemplateWindowController implements Initializable, IController {
    
    private File inputFile;
    private JSONObject jsonObject;
    private ModelManager modelManager;

    @FXML
    private BorderPane borderPane;
    @FXML
    private TextField txtInputPath;
    @FXML
    private JFXButton btnInputPath;
    @FXML
    private TextArea txtAreaJson;
    @FXML
    private JFXComboBox<String> comboVariables;
    @FXML
    private VBox vBoxVariable;
    @FXML
    private JFXRadioButton rbCustom;
    @FXML
    private JFXTextField txtCustom;
    @FXML
    private JFXRadioButton rbPredefined;
    @FXML
    private JFXComboBox<String> comboPredefined;
    @FXML
    private ToggleGroup variables;
    @FXML
    private JFXButton btnStatic;
    @FXML
    private JFXButton btnSave;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @Override
    public void postInit(ModelManager model) {
        this.modelManager = model;
    }

    @FXML
    private void handleInputFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON File", "*.json"));
        File tempFile = fileChooser.showOpenDialog(borderPane.getScene().getWindow());

        if (tempFile != null) {
            inputFile = tempFile;
            txtInputPath.setText(inputFile.getAbsolutePath());
            try {
                setJSONObject(inputFile);
                setTextArea(jsonObject);
                setPredefinedValues();
                comboVariables.getItems().addAll(getStrings(jsonObject.toMap()));
                vBoxVariable.setDisable(false);
                rbCustom.setSelected(true);
                btnSave.setDisable(false);
            } catch (GUIException ex) {
                Window.openExceptionWindow(ex.getMessage());
            }
        }
    }

    private void setPredefinedValues() {
        comboPredefined.getItems().add("Current Time");
    }
    
    private void setJSONObject(File inputFile) throws GUIException {
        try {
            Scanner scanner = new Scanner(inputFile);
            StringBuilder json = new StringBuilder();
            while (scanner.hasNextLine()) {
                json.append(scanner.nextLine());
            }
            jsonObject = new JSONObject(json.toString());
        } catch (FileNotFoundException ex) {
            throw new GUIException("Error reading JSONObject");
        }

    }

    private void setTextArea(JSONObject jsonObject) {
        txtAreaJson.setText(jsonObject.toString(4));
    }

    private List<String> getStrings(Map<String, Object> JSONMap) {
        List<String> values = new ArrayList();
        JSONMap.forEach((key, value) -> {
            if (value instanceof HashMap) {
                values.addAll(getStrings((Map) value));
            } else {
                values.add(key);
            }
        });
        return values;
    }

    @FXML
    private void handleSetStatic(ActionEvent event) {
        String selectedVariable = comboVariables.getSelectionModel().getSelectedItem();
        if (rbCustom.isSelected()) {
            putCustomStatic(jsonObject, selectedVariable, txtCustom.getText());
        } else if (rbPredefined.isSelected()) {
            String selecetedValue = comboPredefined.getSelectionModel().getSelectedItem();
            putCustomStatic(jsonObject, selectedVariable, "PRE_" + selecetedValue);
        }
        txtAreaJson.setText(jsonObject.toString(4));
    }

    private void putCustomStatic(JSONObject json, String stringKey, String stringValue) {
        if (json.has(stringKey)) {
            json.put(stringKey, stringValue);
            return;
        }
        json.toMap().forEach((key, value) -> {
            if (value instanceof HashMap) {
                putCustomStatic(json.getJSONObject(key), stringKey, stringValue);
            }
        });
    }

    @FXML
    private void handleSave(ActionEvent event) {
        try {
            modelManager.getTemplateModel().save(jsonObject);
        } catch (GUIException ex) {
            Window.openExceptionWindow(ex.getMessage());
        }
    }

    

}
