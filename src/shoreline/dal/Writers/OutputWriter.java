package shoreline.dal.Writers;

import java.io.File;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 * @param <T> What type of input to write
 */
public interface OutputWriter<T> {
    /**
     * Writes given file to given output.
     * 
     * @param file File to be written to
     * @param output The output to be written.
     * @throws DALException 
     */
    public void write(File file, T output) throws DALException;
}
