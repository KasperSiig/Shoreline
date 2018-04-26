/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.bll.TitleStrats;

import java.io.File;
import java.util.HashMap;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kasper Siig
 */
public class TitleImpl {
    private TitleStrategy strat;

    public TitleImpl(TitleStrategy strat) {
        this.strat = strat;
    }
    
    public HashMap<String, Integer> getTitles(File file) throws DALException {
        return strat.getTitles(file);
    }
    
}
