/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.gui.controller;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import shoreline.gui.model.ModelManager;

/**
 * FXML Controller class
 *
 * @author madst
 */
public class DefaultVauleWindowController implements Initializable, IController {

    @FXML
    private VBox vBox;
    private ModelManager model;
    private HashMap<String, String> defaultValues;
    private Stage stage;
    
    @FXML
    private BorderPane bPane;

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
        defaultValues = new HashMap<>();
                
        for (String string : model.getConfigModel().getTemplateList()) {
            HBox hBox = new HBox();
            Label lbl = new Label();
            JFXTextField txtField = new JFXTextField();
            txtField.setId(string);
            lbl.setText(string);
            hBox.setSpacing(5);
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.getChildren().add(lbl);
            hBox.getChildren().add(txtField);
            vBox.getChildren().add(hBox);
        }
        
    }

    @FXML
    private void handleConfirm(ActionEvent event) {
        
        for (Node node : vBox.getChildren()) {
            HBox hbox = (HBox) node;
            JFXTextField txt = (JFXTextField) hbox.getChildren().get(1);
            defaultValues.put(txt.getId(), txt.getText());
        }
        System.out.println(defaultValues);
        stage = (Stage) bPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        stage = (Stage) bPane.getScene().getWindow();
        stage.close();
    }

    public HashMap<String, String> getDefaultValues() {
        return defaultValues;
    }
    
}
