package shoreline.dal.TitleStrats;

import java.io.File;
import java.util.LinkedHashMap;
import shoreline.exceptions.DALException;

/**
 *
 * @author 
 */
public interface TitleStrategy {
    public LinkedHashMap<String, Integer> getTitles(File file) throws DALException;
}
