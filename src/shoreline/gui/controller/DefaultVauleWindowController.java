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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
            hBox.setStyle("-fx-background-color: white;");
            Label lbl = new Label();
            lbl.setMinWidth(180);
            JFXTextField txtField = new JFXTextField();
            txtField.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (newValue) {
                        lbl.setText(" " + lbl.getText());
                        lbl.setStyle("-fx-border-color: #2e6da4;-fx-border-width: 0px 0px 0px 5px; -fx-opacity: 1.0;");
                    } else {
                        lbl.setText(lbl.getText().substring(1));
                        lbl.setStyle("");
                    }
                }
            });
            txtField.setId(string);
            lbl.setText(string.substring(0, 1).toUpperCase() + string.substring(1) + "");
            hBox.setSpacing(5);
            hBox.setAlignment(Pos.CENTER_LEFT);
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
