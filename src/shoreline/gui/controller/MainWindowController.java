package shoreline.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import shoreline.be.ConvTask;
import shoreline.dal.ConvStrats.ConvImpl;
import shoreline.dal.ConvStrats.XLXSConvStrat;
import shoreline.dal.TitleStrats.TitleImpl;
import shoreline.gui.model.MainModel;

/**
 * FXML Controller class
 *
 * @author
 */
public class MainWindowController implements Initializable, IController {

    private MainModel model;
    private BorderPane borderPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(System.getProperty("user.home"));
    }    

    @Override
    public void postInit(MainModel model) {
        this.model = model;
        this.borderPane = model.getBorderPane();
    }
}
