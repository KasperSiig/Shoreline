package shoreline.gui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shoreline.be.Batch;
import shoreline.bll.LogicManager;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.GUIException;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class BatchModel {
 
    private LogicManager logic;

    private ObservableList<Batch> batches;

    public BatchModel(LogicManager logic) {
        this.logic = logic;
        this.batches = FXCollections.observableArrayList(logic.getBatchLogic().getBatches());
    }

    public ObservableList<Batch> getBatches() {
        return batches;
    }
    
    public void addToBatches(Batch batch) throws GUIException {
        try {
            batches.add(batch);
            logic.getBatchLogic().addToBatchList(batch);
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
        
    }

    public void deleteBatch(Batch batch) {
        logic.getBatchLogic().deleteBatch(batch);
    }
    
    
}
