package shoreline;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author
 */
public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/shoreline/gui/view/RootWindow.fxml"));
        
        Scene scene = new Scene(root);
        stage.setMinHeight(750);
        stage.setMinWidth(1050);
        stage.setScene(scene);
        stage.getIcons().add(new Image("/shoreline/res/ShorelineLogo.png"));
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
