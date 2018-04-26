package shoreline.bll.TitleStrats;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import shoreline.exceptions.DALException;

/**
 *
 * @author 
 */
public interface TitleStrategy {
    public HashMap<String, Integer> getTitles(File file) throws DALException;
}
