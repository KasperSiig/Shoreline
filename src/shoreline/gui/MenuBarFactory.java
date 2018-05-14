package shoreline.gui;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import shoreline.exceptions.GUIException;
import shoreline.gui.model.ModelManager;
import shoreline.statics.Window;

/**
 * Factory to generate MenuBar based on Enum MenuType
 * 
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class MenuBarFactory {

    private ModelManager model;
    private boolean taskIsOpen = false;
    private boolean logIsOpen = false;

    /**
     * Defines what type of Menu can be generated
     */
    public enum MenuType {
        Default
    }

    /**
     * Returns MenuBar based on Enum MenuType
     * 
     * @param menuType What MenuBar to be generated
     * @param model Makes it possible to call methods in model from MenuBar
     * @return MenuBar
     */
    public MenuBar createMenuBar(MenuType menuType, ModelManager model) {
        this.model = model;
        switch (menuType) {
            case Default:
                return defaultMenuBar();
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Is called to return the Default MenuBar
     * 
     * @return MenuBar
     */
    private MenuBar defaultMenuBar() {
        Menu options = new Menu("View");
        options.getItems().add(openTaskView());
        options.getItems().add(openLogView());
        MenuBar menuBar = new MenuBar(options);
        menuBar.getStyleClass().clear();
        menuBar.getStyleClass().add("menu-menu");
        return menuBar;
    }

    /**
     * @return MenuItem to open/close Task View
     */
    private MenuItem openTaskView() {
        MenuItem openTaskView = new MenuItem("Open Task View");
        openTaskView.setOnAction((event) -> {
            try {
                if (!taskIsOpen) {
                    openTaskView.setText("Close Task View");
                    Window.openView(model, model.getBorderPane(), Window.View.TaskView, "right");
                } else {
                    openTaskView.setText("Open Task View");
                    Window.closeWindow("right", model.getBorderPane());
                }
                taskIsOpen = !taskIsOpen;
            } catch (GUIException ex) {
                Window.openExceptionWindow("There was a problem generating your menu bar");
            }
        });
        return openTaskView;
    }

    /**
     * @return MenuItem to open/close Log View
     */
    private MenuItem openLogView() {
        MenuItem openLogView = new MenuItem("Open Log View");
        openLogView.setOnAction((event) -> {
            try {
                if (!logIsOpen) {
                    openLogView.setText("Close Log View");
                    Window.openView(model, model.getBorderPane(), Window.View.logView, "bottom");
                } else {
                    openLogView.setText("Open Log View");
                    Window.closeWindow("bottom", model.getBorderPane());
                }
                logIsOpen = !logIsOpen;
            } catch (GUIException ex) {
                Window.openExceptionWindow("There was a problem generating your menu bar");
            }
        });
        return openLogView;
    }
}
