/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.bll.ConvStrats;

import shoreline.be.ConvTask;
import shoreline.dal.Readers.InputReader;
import shoreline.dal.Writers.OutputWriter;

/**
 *
 * @author Kasper Siig
 */
public interface ConvStrategy {
    /**
     * 
     * 
     * @param task
     * @param inputReader
     * @param outputWriter 
     */
    public void addCallable(ConvTask task, InputReader inputReader, OutputWriter outputWriter);
}
