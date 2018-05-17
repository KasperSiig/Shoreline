/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.gui.model;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.BorderPane;
import shoreline.be.Batch;
import shoreline.be.ConvTask;
import shoreline.bll.LogicManager;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.GUIException;

/**
 *
 * @author madst
 */
public class BatchModel {
 
    private LogicManager logic;

    private ObservableList<Batch> batches;

    public BatchModel(BorderPane borderPane, LogicManager logic) {
        this.logic = logic;

        this.batches = FXCollections.observableArrayList(logic.getBatches());

    }

    public ObservableList<Batch> getBatches() {
        return batches;
    }
    
    public void addToBatches(Batch batch) throws GUIException {
        try {
            logic.addToBatchList(batch);
            batches.add(batch);
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
        
    }
    
    
}
