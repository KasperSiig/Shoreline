package shoreline.gui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import shoreline.gui.model.ModelManager;

/**
 * FXML Controller class
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class ConfirmationWindowController implements Initializable, IController {

    private boolean confirmation = false;

    @FXML
    private JFXTextField txtInput;
    @FXML
    private Label lblInfo;
    @FXML
    private JFXButton btnYes;
    @FXML
    private BorderPane bPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @Override
    public void postInit(ModelManager model) {
    }

    /**
     * Sets the information to be shown, in the confirmation window
     *
     * @param message Header to show
     */
    public void setInfo(String message) {
        lblInfo.setText(message);
        btnYes.requestFocus();
    }

    /**
     * Handles action for pressing yes
     *
     * @param event
     */
    @FXML
    private void handleYes(ActionEvent event) {
        confirmation = true;
        Stage stage = (Stage) lblInfo.getScene().getWindow();
        stage.close();
    }

    /**
     * Handles action for pressing no
     *
     *
     * @param event
     */
    @FXML
    private void handleNo(ActionEvent event) {
        confirmation = false;
        Stage stage = (Stage) lblInfo.getScene().getWindow();
        stage.close();
    }

    /**
     * @return Confirmation boolean
     */
    public boolean getConfirmation() {
        return confirmation;
    }
}
