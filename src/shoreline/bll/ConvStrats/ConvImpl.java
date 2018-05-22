package shoreline.bll.ConvStrats;

import shoreline.be.ConvTask;
import shoreline.dal.Readers.InputReader;
import shoreline.dal.Writers.OutputWriter;
import shoreline.exceptions.BLLException;

/**
 * Class responsible for implementing Strategy Pattern
 * 
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
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
     * @throws BLLException 
     */
    public void addCallable(ConvTask task) throws BLLException {
        strategy.addCallable(task, reader, writer);
    }
    
}
