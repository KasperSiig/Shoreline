/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.gui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import shoreline.exceptions.GUIException;
import shoreline.gui.model.ModelManager;
import shoreline.statics.Window;

/**
 * FXML Controller class
 *
 * @author kaspe
 */
public class DatabaseSettingWindowController implements Initializable, IController {

    private String userDir = System.getProperty("user.dir");
    private ModelManager model;

    @FXML
    private BorderPane borderPane;
    @FXML
    private JFXButton btnBack;
    @FXML
    private VBox vBox;
    @FXML
    private JFXTextField txtUser;
    @FXML
    private JFXPasswordField txtPassword;
    @FXML
    private JFXTextField txtPortNumber;
    @FXML
    private JFXTextField txtDatabaseName;
    @FXML
    private JFXTextField txtServerName;
    @FXML
    private JFXComboBox<String> cbExisting;
    private Label lblError;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    public void postInit(ModelManager model) {
        this.model = model;
        setUserData(vBox);
        setFocusListener(vBox);
        txtUser.requestFocus();
        loadCredentials();
        loadPropertiesInComboBox();
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            SingleTaskWindowController stwc = (SingleTaskWindowController) Window.openView(model, model.getBorderPane(), Window.View.SingleTask, "center");
            stwc.setTabSelected(3);
        } catch (GUIException ex) {
            Window.openExceptionWindow(ex.getMessage());
        }
    }

    private void setUserData(VBox vBox) {
        vBox.getChildren().forEach((node) -> {
            if (node instanceof HBox) {
                HBox hBox = (HBox) node;
                if (hBox.getChildren().get(0) instanceof Label) {
                    Label label = (Label) hBox.getChildren().get(0);
                    hBox.getChildren().get(1).setUserData(label);
                }
            }
        });
    }

    private void setFocusListener(VBox vBox) {
        vBox.getChildren().forEach((parent) -> {
            if (parent instanceof HBox) {
                HBox hBox = (HBox) parent;
                if (hBox.getChildren().get(0) instanceof Label) {
                    Node node = hBox.getChildren().get(1);
                    node.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        Label lbl = (Label) node.getUserData();
                        if (newValue) {
                            lbl.setText(" " + lbl.getText());
                            lbl.setStyle("-fx-border-color: #2e6da4;-fx-border-width: 0px 0px 0px 5px; -fx-opacity: 1.0;");
                        } else {
                            lbl.setText(lbl.getText().substring(1));
                            lbl.setStyle("");
                        }
                    });
                }
            }
        });
    }

    private void loadCredentials() {
        txtUser.setText(model.getPropertiesModel().getProperty("user"));
        txtPassword.setText(model.getPropertiesModel().getProperty("password"));
        txtPortNumber.setText(model.getPropertiesModel().getProperty("portNumber"));
        txtDatabaseName.setText(model.getPropertiesModel().getProperty("databaseName"));
        txtServerName.setText(model.getPropertiesModel().getProperty("serverName"));
    }

    @FXML
    private void saveDBCredentials(ActionEvent event) {
        Properties properties = new Properties();
        properties.put("user", txtUser.getText());
        properties.put("password", txtPassword.getText());
        properties.put("portNumber", txtPortNumber.getText());
        properties.put("databaseName", txtDatabaseName.getText());
        properties.put("serverName", txtServerName.getText());
        savePropertiesInFile(userDir + "\\configs\\config.properties", properties, false);
        try {
            Window.openView(model, model.getBorderPane(), Window.View.Login, "center");
        } catch (GUIException ex) {
            Logger.getLogger(DatabaseSettingWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void handleImport(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Properties File (*.properties)", "*.properties"));
        File tempFile = fileChooser.showOpenDialog(borderPane.getScene().getWindow());

        if (tempFile != null) {
            importFromFile(tempFile);
        }
    }

    private void importFromFile(File file) {
        Properties properties = model.getPropertiesModel().getPropertiesFromFile(file.getAbsolutePath());
        txtUser.setText(properties.getProperty("user"));
        txtPassword.setText(properties.getProperty("password"));
        txtPortNumber.setText(properties.getProperty("portNumber"));
        txtDatabaseName.setText(properties.getProperty("databaseName"));
        txtServerName.setText(properties.getProperty("serverName"));
    }

    @FXML
    private void handleExport(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Properties File (*.properties)", "*.properties"));
        File tempFile = fileChooser.showSaveDialog(borderPane.getScene().getWindow());

        if (tempFile != null) {
            Properties properties = new Properties();
            properties.put("user", txtUser.getText());
            properties.put("password", txtPassword.getText());
            properties.put("portNumber", txtPortNumber.getText());
            properties.put("databaseName", txtDatabaseName.getText());
            properties.put("serverName", txtServerName.getText());
            savePropertiesInFile(tempFile.getAbsolutePath(), properties, false);
        }
    }

    private void loadPropertiesInComboBox() {
        HashMap<String, File> configs = model.getPropertiesModel().getAllPropertyFiles();
        configs.forEach((key, value) -> {
            cbExisting.getItems().add(key);
        });
        cbExisting.valueProperty().addListener((observable, oldValue, newValue) -> {
            importFromFile(configs.get(newValue));
        });
    }
    
    private boolean validateConnection(Properties properties) {
        boolean valid = false;
        try {
            valid = model.getPropertiesModel().validateConnection(properties);
        } catch (GUIException ex) {
            Logger.getLogger(DatabaseSettingWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valid;
    }
    
    private void savePropertiesInFile(String filePath, Properties properties, boolean overwrite) {
        try {
            if (validateConnection(properties)) {
                model.getPropertiesModel().savePropertiesFile(filePath, properties, overwrite);
                Window.openSnack("Configuration Was Saved", borderPane, "blue");
            } else {
                Window.openSnack("Connection is Not Valid", borderPane, "red");
            }
            
        } catch (GUIException ex) {
            Logger.getLogger(DatabaseSettingWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
