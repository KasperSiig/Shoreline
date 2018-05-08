/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.gui.controller;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import shoreline.be.LogItem;
import shoreline.gui.model.MainModel;

/**
 * FXML Controller class
 *
 * @author madst
 */
public class LogWindowController implements Initializable, IController {

    MainModel model;

    @FXML
    private TableView<LogItem> tv;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    /**
     * Runs before the rest of the class.
     * 
     * @param model 
     */
    @Override
    public void postInit(MainModel model) {
        this.model = model;
        makeTable();
        setTable();
    }

    /**
     * Creates the columns for the table view
     * and set a max width on them.
     * then adds them to the table view.
     * 
     */
    private void makeTable() {
        TableColumn typeCol = new TableColumn("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<LogItem, Alert.AlertType>("Type"));
        typeCol.setMaxWidth(2000);

        TableColumn messageCol = new TableColumn("Message");
        messageCol.setCellValueFactory(new PropertyValueFactory<LogItem, String>("Message"));

        TableColumn dateCol = new TableColumn("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<LogItem, Date>("Date"));
        dateCol.setMaxWidth(2500);

        TableColumn userCol = new TableColumn("User");
        userCol.setCellValueFactory(new PropertyValueFactory<LogItem, String>("User"));
        userCol.setMaxWidth(1500);

        tv.getColumns().addAll(typeCol, userCol, messageCol, dateCol);
    }

    
    /**
     * makes a temp observable list then
     * sorts it.
     * runes the LogListListener method
     * then sets the table view to be 
     * the content of the temp list.
     * 
     */
    private void setTable() {
        ObservableList temp = FXCollections.observableArrayList(model.getLogList());

        FXCollections.sort(temp, (LogItem t, LogItem t1) -> {
            if (t.getId() < t1.getId()) {
                return 1;
            } else {
                return 0;
            }
        });
        logListListener();

        tv.setItems(temp);
    }

    /**
     * Adds a listener to the loglist in model
     * if there is any change in the list it
     * runs the setTable method again
     * 
     */
    private void logListListener() {
        model.getLogList().addListener(new ListChangeListener<LogItem>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends LogItem> c) {
                c.next();
                if (c.wasAdded() || c.wasRemoved() || c.wasReplaced() || c.wasUpdated()) {
                    setTable();
                }
            }
        });

    }

}
