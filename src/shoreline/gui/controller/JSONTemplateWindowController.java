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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.json.JSONException;
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
    @FXML
    private JFXButton btnBack;
    @FXML
    private HBox hBoxTarget;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @Override
    public void postInit(ModelManager model) {
        this.modelManager = model;
        jsonObject = model.getConfigModel().getTemplateJson();
        setTextArea(jsonObject);
        validateTextArea();
    }

    @FXML
    private void handleInputFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON File", "*.json"));
        File tempFile = fileChooser.showOpenDialog(borderPane.getScene().getWindow());

        if (tempFile != null) {
            inputFile = tempFile;

            try {
                if (!setJSONObject(inputFile)) {
                    return;
                }
                txtInputPath.setText(inputFile.getAbsolutePath());
                setTextArea(jsonObject);
                setPredefinedValues();
                comboVariables.getItems().addAll(getStrings(jsonObject.toMap()));
                rbCustom.setSelected(true);
            } catch (GUIException ex) {
                Window.openExceptionWindow(ex.getMessage());
            }
        }
    }

    private void setPredefinedValues() {
        comboPredefined.getItems().add("Current Time");
    }

    private boolean setJSONObject(File inputFile) throws GUIException {
        try {
            Scanner scanner = new Scanner(inputFile);
            StringBuilder json = new StringBuilder();
            while (scanner.hasNextLine()) {
                json.append(scanner.nextLine());
            }
            jsonObject = new JSONObject(json.toString());
        } catch (FileNotFoundException ex) {
            throw new GUIException("Error reading JSONObject");
        } catch (JSONException ex) {
            Window.openExceptionWindow(ex.getMessage());
            return false;
        }
        return true;
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
            Window.openSnack("Template was saved", borderPane, "blue");
        } catch (GUIException ex) {
            Window.openExceptionWindow(ex.getMessage());
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            SingleTaskWindowController stwc = (SingleTaskWindowController) Window.openView(modelManager, modelManager.getBorderPane(), Window.View.SingleTask, "center");
            stwc.setTabSelected(3);
        } catch (GUIException ex) {
            Window.openExceptionWindow(ex.getMessage());
        }
    }

    private void validateTextArea() {
        txtAreaJson.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                JSONObject json = new JSONObject(newValue);
            } catch (JSONException ex) {
                Window.openExceptionWindow(ex.getMessage() + "\nTextAre has been reverted");
                txtAreaJson.setText(oldValue);
            }
        });

    }

}
