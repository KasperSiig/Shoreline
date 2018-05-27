package shoreline.gui.controller;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import shoreline.gui.model.ModelManager;

/**
 * FXML Controller class
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class DefaultVauleWindowController implements Initializable, IController {

    @FXML
    private VBox vBox;

    @FXML
    private BorderPane borderPane;

    private ModelManager model;
    private HashMap<String, String> defaultValues;
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @Override
    public void postInit(ModelManager model) {
        this.model = model;
        model.getConfigModel().getTemplateList().forEach((string) -> {
            createHBox(string);
        });

    }

    /**
     * Sets info to be used in creation of default values
     *
     * @param defaultValues
     */
    public void setInfo(HashMap<String, String> defaultValues) {
        this.defaultValues = defaultValues;
    }

    /**
     * Creates HBox containing Label and JFXTextField
     *
     * @param string String to create HBox from
     */
    private void createHBox(String string) {
        HBox hBox = new HBox();
        hBox.setStyle("-fx-background-color: white;");
        Label lbl = new Label();
        lbl.setMinWidth(180);
        JFXTextField txtField = new JFXTextField();
        txtField.setText(defaultValues.get(string));
        txtField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                lbl.setText(" " + lbl.getText());
                lbl.setStyle("-fx-border-color: #2e6da4;-fx-border-width: 0px 0px 0px 5px; -fx-opacity: 1.0;");
            } else {
                lbl.setText(lbl.getText().substring(1));
                lbl.setStyle("");
            }
        });
        txtField.setUserData(string);
        lbl.setText(string.substring(0, 1).toUpperCase() + string.substring(1) + "");
        hBox.setSpacing(5);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().add(lbl);
        hBox.getChildren().add(txtField);
        vBox.getChildren().add(hBox);
    }

    /**
     * Handles confirm button
     *
     * @param event
     */
    @FXML
    private void handleConfirm(ActionEvent event) {
        vBox.getChildren().forEach((node) -> {
            HBox hbox = (HBox) node;
            JFXTextField txt = (JFXTextField) hbox.getChildren().get(1);
            defaultValues.put((String) txt.getUserData(), txt.getText());
        });
        
        stage = (Stage) borderPane.getScene().getWindow();
        stage.close();
    }

    /**
     * Handles cancel button
     * 
     * @param event 
     */
    @FXML
    private void handleCancel(ActionEvent event) {
        stage = (Stage) borderPane.getScene().getWindow();
        stage.close();
    }
}
