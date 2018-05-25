package shoreline.dal.TitleStrats;

import java.io.File;
import java.util.HashMap;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class TitleImpl {
    private TitleStrategy strat;

    /**
     * Takes in what TitleStrat should be used
     * @param strat 
     */
    public TitleImpl(TitleStrategy strat) {
        this.strat = strat;
    }
    
    /**
     * Calls the method in strategy, given in the constructor
     * 
     * @param file File to get titles from
     * @return HashMap containing titles and given indexes
     * @throws DALException 
     */
    public HashMap<String, Integer> getTitles(File file) throws DALException {
        return strat.getTitles(file);
    }
    
}
