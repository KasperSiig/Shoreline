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

    public ConvImpl(ConvStrategy strategy) {
        this.strategy = strategy;
    }
    
    public void convertAndWrite(ConvTask task) throws DALException {
        strategy.convertAndWrite(task);
    }
    
}
