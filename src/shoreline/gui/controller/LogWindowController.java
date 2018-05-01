/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.gui.controller;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import static javax.management.Query.value;
import shoreline.be.logItem;
import shoreline.gui.model.MainModel;

/**
 * FXML Controller class
 *
 * @author madst
 */
public class LogWindowController implements Initializable, IController {

    MainModel model;

    @FXML
    private TableView<logItem> tv;

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
        setTable();
    }

    private void setTable() {

        TableColumn typeCol = new TableColumn("Type");
        typeCol.setCellValueFactory(
                new PropertyValueFactory<logItem, Alert.AlertType>("Type"));

        TableColumn messageCol = new TableColumn("Message");
        messageCol.setCellValueFactory(
                new PropertyValueFactory<logItem, String>("Message"));

        TableColumn dateCol = new TableColumn("Date");
        dateCol.setCellValueFactory(
                new PropertyValueFactory<logItem, Date>("Date"));

        TableColumn userCol = new TableColumn("User");
        userCol.setCellValueFactory(
                new PropertyValueFactory<logItem, String>("User"));

        tv.setItems(model.getLogList());
        tv.getColumns().addAll(typeCol, messageCol, dateCol, userCol);
    }
}
