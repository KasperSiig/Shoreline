package shoreline.gui;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import shoreline.gui.model.MainModel;

/**
 *
 * @author
 */
public class MenuBarFactory {

    public enum MenuType {
        Default
    }

    public MenuBar createMenuBar(MenuType menuType, MainModel model) {
        switch (menuType) {
            case Default:
                return defaultMenuBar();
            default:
                throw new AssertionError();
        }
    }
    
    private MenuBar defaultMenuBar() {
        Menu options = new Menu("Options");
        options.getItems().add(test1());
        MenuBar menuBar = new MenuBar(options);
        return menuBar;
    }

    private MenuItem test1() {
        MenuItem test1 = new Menu("test1");
        test1.setOnAction((event) -> {
            System.out.println("test1");
        });
        return test1;
    }
    
}
