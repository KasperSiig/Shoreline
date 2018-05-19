/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.bll.ConvStrats;

import shoreline.be.ConvTask;
import shoreline.dal.Readers.InputReader;
import shoreline.dal.Writers.OutputWriter;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kasper Siig
 */
public class ConvImpl {
    private ConvStrategy strategy;
    private InputReader reader;
    private OutputWriter writer;

    /**
     * Initiates the Conversion Implementation, and sets the strategy to use
     * 
     * @param strategy The strategy to be used
     * @param reader
     * @param writer
     */
    public ConvImpl(ConvStrategy strategy, InputReader reader, OutputWriter writer) {
        this.strategy = strategy;
        this.reader = reader;
        this.writer = writer;
    }
    
    /**
     * Runs the addCallable method, with the strategy chosen in constructor
     * 
     * @param task The ConvTask to be converted
     * @throws DALException 
     */
    public void addCallable(ConvTask task) throws DALException {
        strategy.addCallable(task, reader, writer);
    }
    
}
