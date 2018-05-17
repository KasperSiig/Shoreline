/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.gui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.BorderPane;
import shoreline.be.ConvTask;
import shoreline.bll.LogicManager;

/**
 *
 * @author madst
 */
public class BatchModel {
 
    private LogicManager logic;

    private ObservableList<ConvTask> batches;

    public BatchModel(BorderPane borderPane, LogicManager logic) {
        this.logic = logic;

        this.batches = FXCollections.observableArrayList();

    }

    public ObservableList<ConvTask> getBatches() {
        return batches;
    }
    
    
    
}
