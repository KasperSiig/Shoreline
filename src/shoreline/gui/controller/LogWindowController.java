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
import shoreline.gui.model.ModelManager;

/**
 * FXML Controller class
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class LogWindowController implements Initializable, IController {

    private ModelManager model;

    @FXML
    private TableView<LogItem> tableView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @Override
    public void postInit(ModelManager model) {
        this.model = model;
        makeTable();
        setTable();
    }

    /**
     * Creates the columns for the table view and sets a max width on them.
     * then adds them to the table view.
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

        tableView.getColumns().addAll(typeCol, userCol, messageCol, dateCol);
    }

    
    /**
     * Sets table with existing logs and sorts them. Then starts listener on new logs
     */
    private void setTable() {
        ObservableList temp = FXCollections.observableArrayList(model.getLogModel().getList());

        FXCollections.sort(temp, (LogItem t, LogItem t1) -> {
            return t.getId() < t1.getId() ? 1 : 0;
        });
        startLogListener();

        tableView.setItems(temp);
    }

    /**
     * Adds a listener to the loglist in model. If there is any change in the list 
     * it runs the setTable method again
     * 
     */
    private void startLogListener() {
        model.getLogModel().getList().addListener((ListChangeListener.Change<? extends LogItem> c) -> {
            c.next();
            if (c.wasAdded() || c.wasRemoved() || c.wasReplaced() || c.wasUpdated()) {
                setTable();
            }
        });

    }

}
