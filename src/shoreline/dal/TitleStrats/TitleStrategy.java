package shoreline.dal.TitleStrats;

import java.io.File;
import java.util.HashMap;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public interface TitleStrategy {
    /**
     * Gets titles from given File
     * 
     * @param file File to get titles from
     * @return LinkedHashMap of titles with indexes
     * @throws DALException 
     */
    public HashMap<String, Integer> getTitles(File file) throws DALException;
}
