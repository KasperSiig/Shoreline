package shoreline.dal.TitleStrats;

import java.io.File;
import java.util.LinkedHashMap;
import shoreline.exceptions.DALException;

/**
 *
 * @author 
 */
public interface TitleStrategy {
    /**
     * Gets titles from given File
     * 
     * @param file File to get titles from
     * @return LinkedHashMap of titles with indexes
     * @throws DALException 
     */
    public LinkedHashMap<String, Integer> getTitles(File file) throws DALException;
}
