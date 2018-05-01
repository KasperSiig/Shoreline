/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.dal.ConvStrats;

import shoreline.be.ConvTask;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kasper Siig
 */
public class ConvImpl {
    private ConvStrategy strategy;

    /**
     * Initiates the Conversion Implementation, and sets the strategy to use
     * 
     * @param strategy The strategy to be used
     */
    public ConvImpl(ConvStrategy strategy) {
        this.strategy = strategy;
    }
    
    /**
     * Runs the addCallableToTask method, with the strategy chosen in constructor
     * 
     * @param task The ConvTask to be converted
     * @throws DALException 
     */
    public void addCallableToTask(ConvTask task) throws DALException {
        strategy.addCallableToTask(task);
    }
    
}
