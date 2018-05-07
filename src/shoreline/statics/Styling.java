/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.statics;

import javafx.scene.Node;

/**
 *
 * @author madst
 */
public class Styling {
    
    public static void redOutline (Node node) {
        node.setStyle("-fx-border-radius: 6px; -fx-border-color: red;");
    }
    
    public static void clearRedOutline(Node node) {
        node.setStyle("");
    }
    
    
}
