/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.statics;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

/**
 *
 * @author madst
 */
public class Styling {

    public static void redOutline(Node node) {
        if (node instanceof JFXButton) {
            node.setStyle("-fx-border-radius: 6px; -fx-border-color: red;");
        } else {
            node.setStyle("-fx-border-radius: 0px; -fx-border-color: red;");
        }
    }

    public static void clearRedOutline(Node node) {
        node.setStyle("");
    }

}
