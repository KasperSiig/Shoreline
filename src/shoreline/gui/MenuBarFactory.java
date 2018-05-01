package shoreline.gui;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import shoreline.exceptions.GUIException;
import shoreline.gui.model.MainModel;
import shoreline.statics.Window;

/**
 *
 * @author
 */
public class MenuBarFactory {

    MainModel model;
    boolean taskIsOpen = false;
    boolean logIsOpen = false;

    public enum MenuType {
        Default
    }

    public MenuBar createMenuBar(MenuType menuType, MainModel model) {
        this.model = model;
        switch (menuType) {
            case Default:
                return defaultMenuBar();
            default:
                throw new AssertionError();
        }
    }

    private MenuBar defaultMenuBar() {
        Menu options = new Menu("Options");
        options.getItems().add(openTaskView());
        options.getItems().add(openLogView());
        MenuBar menuBar = new MenuBar(options);
        return menuBar;
    }

    private MenuItem openTaskView() {
        MenuItem openTaskView = new MenuItem("Open Task view");
        openTaskView.setOnAction((event) -> {
            try {
                if (!taskIsOpen) {
                    Window.openView(model, model.getBorderPane(), Window.View.TaskView, "right");
                } else {
                    Window.closeWindow("right", model.getBorderPane());
                }
                taskIsOpen = !taskIsOpen;
            } catch (GUIException ex) {
                Window.openExceptionWindow("There was a problem generating your menu bar");
            }
        });
        return openTaskView;
    }

    private MenuItem openLogView() {
        MenuItem openLogView = new MenuItem("Open Log view");
        openLogView.setOnAction((event) -> {
            try {
                if (!logIsOpen) {
                    Window.openView(model, model.getBorderPane(), Window.View.logView, "bottom");
                } else {
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
