package shoreline.dal.Readers;

import java.io.File;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 * @param <T> What reader should return
 */
public interface InputReader<T> {
    /**
     * Reads from a given file
     * 
     * @param file
     * @return
     * @throws DALException 
     */
    public T read(File file) throws DALException;
}
