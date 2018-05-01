/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.dal.ConvStrats;

import org.json.JSONArray;
import shoreline.be.ConvTask;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kasper Siig
 */
public interface ConvStrategy {
    /**
     * 
     * 
     * @param task
     * @return
     * @throws DALException 
     */
    public JSONArray addCallableToTask(ConvTask task) throws DALException;
}
