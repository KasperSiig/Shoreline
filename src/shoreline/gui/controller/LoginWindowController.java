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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import shoreline.gui.model.MainModel;

/**
 * FXML Controller class
 *
 * @author madst
 */
public class LoginWindowController implements Initializable, IController {

    MainModel model;
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
    public void postInit(MainModel model) {
        this.model = model;
    }

    @FXML
    private void forgotPassword(MouseEvent event) {
        System.out.println("forgot you're password eh?");
    }

    @FXML
    private void createNewUser(MouseEvent event) {
    }
    
}
