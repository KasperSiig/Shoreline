package shoreline.bll.ConvStrats;

import shoreline.be.ConvTask;
import shoreline.dal.Readers.InputReader;
import shoreline.dal.Writers.OutputWriter;
import shoreline.exceptions.BLLException;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public interface ConvStrategy {
    /**
     * Adds Callable to ConvTask, making it ready for conversion
     * 
     * @param task ConvTask to add Callable to
     * @param reader What should be read and how
     * @param writer How it should be written
     * @throws BLLException
     */
    public void addCallable(ConvTask task, InputReader reader, OutputWriter writer) throws BLLException;
}
