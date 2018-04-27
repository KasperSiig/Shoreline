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
    boolean isOpen = false;

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
        MenuBar menuBar = new MenuBar(options);
        return menuBar;
    }

    private MenuItem openTaskView() {
        MenuItem openTaskView = new MenuItem("Open Task view");
        openTaskView.setOnAction((event) -> {
            try {
                if (!isOpen) {
                    Window.openView(model, model.getBorderPane(), Window.View.TaskView, "right");
                } else {
                    Window.closeWindow("right", model.getBorderPane());
                }
                isOpen = !isOpen;
            } catch (GUIException ex) {
                Window.openExceptionWindow("There was a problem generating your menu bar");
            }
        });
        return openTaskView;
    }

}
