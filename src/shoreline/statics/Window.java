package shoreline.statics;

import com.jfoenix.controls.JFXSnackbar;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import shoreline.Main;
import shoreline.exceptions.GUIException;
import shoreline.gui.MenuBarFactory;
import shoreline.gui.controller.IController;
import shoreline.gui.model.MainModel;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class Window {

    private static String dir = "/shoreline/gui/view/";

    /**
     * Sets a predefined values that a View can have
     */
    public enum View {
        Main("MainWindow.fxml"),
        Login("LoginWindow.fxml"),
        CreateUser("CreateUserWindow.fxml"),
        Mapping("MappingWindow.fxml"),
        TaskView("TaskWindow.fxml"),
        logView("LogWindow.fxml"),
        Confirm("ConfirmationWindow.fxml");

        String view;

        View(String view) {
            this.view = view;
        }

        public String getView() {
            return dir + view;
        }
    }

    /**
     * Insert an FXML view into a desired position, in a given BorderPane
     *
     * @param model The model to be passed, to the new controller
     * @param borderpane The BorderPane that needs to be updated
     * @param view The view that is going to be loaded
     * @param pos The position of the new view in the BorderPane
     * @return an IController object
     * @throws GUIException
     */
    public static IController openView(MainModel model, BorderPane borderpane, View view, String pos) throws GUIException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource(view.getView()));
            Node node = loader.load();

            switch (pos) {
                case "top":
                    borderpane.setTop(node);
                    break;
                case "center":
                    borderpane.setCenter(node);
                    break;
                case "bottom":
                    borderpane.setBottom(node);
                    break;
                case "left":
                    borderpane.setLeft(node);
                    break;
                case "right":
                    borderpane.setRight(node);
                    break;
            }

            IController cont = loader.getController();
            cont.postInit(model);
            return cont;
        } catch (IOException e) {
            throw new GUIException(e);
        }
    }

    public static IController openView(MainModel model, BorderPane borderpane, View view, String pos, MenuBarFactory.MenuType menuType) throws GUIException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource(view.getView()));
            Node node = loader.load();

            switch (pos) {
                case "top":
                    borderpane.setTop(node);
                    break;
                case "center":
                    borderpane.setCenter(node);
                    break;
                case "bottom":
                    borderpane.setBottom(node);
                    break;
                case "left":
                    borderpane.setLeft(node);
                    break;
                case "right":
                    borderpane.setRight(node);
                    break;
            }

            IController cont = loader.getController();
            cont.postInit(model);
            MenuBarFactory mbf = new MenuBarFactory();
            MenuBar mBar = mbf.createMenuBar(menuType, model);
            AnchorPane aPane = (AnchorPane) model.getBorderPane().getTop();
            aPane.getChildren().remove(0);
            aPane.getChildren().add(0, mBar);
            return cont;
        } catch (IOException e) {
            throw new GUIException(e);
        }
    }

    /**
     * Opens an alert window with stacktrace.
     *
     * @param header Error message to show.
     * @param stackTrace Stacktrace from exception.
     */
    public static void openExceptionWindow(String header, StackTraceElement[] stackTrace) {
        String msg = "";
        for (StackTraceElement stackTraceElement : stackTrace) {
            msg += stackTraceElement + "\n";
        }
        Alert alert = new Alert(Alert.AlertType.WARNING,
                header + "\n" + msg,
                ButtonType.CLOSE);
        alert.getDialogPane().setMinWidth(800);
        alert.show();
    }

    /**
     * Opens an alert window without stacktrace.
     *
     * @param header Error message to show.
     */
    public static void openExceptionWindow(String header) {
        Alert alert = new Alert(Alert.AlertType.WARNING,
                header,
                ButtonType.CLOSE);
        alert.getDialogPane().setMinWidth(800);
        alert.show();
    }

    /**
     * Removes Node from specified area in BorderPane
     * 
     * @param pos
     * @param borderpane 
     */
    public static void closeWindow(String pos, BorderPane borderpane) {
        switch (pos) {
            case "top":
                borderpane.setTop(null);
                break;
            case "center":
                borderpane.setCenter(null);
                break;
            case "bottom":
                borderpane.setBottom(null);
                break;
            case "left":
                borderpane.setLeft(null);
                break;
            case "right":
                borderpane.setRight(null);
                break;
        }
    }
    
    public static void openSnack(String msg, BorderPane bPane){
        JFXSnackbar bar = new JFXSnackbar(bPane);
        
        bar.show(msg, 2500);
        
    }
    
}
