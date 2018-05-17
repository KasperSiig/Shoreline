/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import shoreline.gui.model.ModelManager;

/**
 * FXML Controller class
 *
 * @author madst
 */
public class BatchTaskWindowController implements Initializable, IController {

    @FXML
    private ScrollPane scrlPanePen;
    @FXML
    private VBox vBoxPen;
    private ModelManager model;

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
    }
    
}
