/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.bll;

import java.io.File;
import java.util.HashMap;
import shoreline.be.ConvTask;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kasper Siig
 */
public class TaskLogic {
    private LogicManager logicManager;

    public TaskLogic(LogicManager logicManager) {
        this.logicManager = logicManager;
    }
    
    public HashMap<String, Integer> getTitles(File file) throws BLLException {
        try {
            return logicManager.getDataManager().getTitles(file);
        } catch (DALException ex) {
            throw new BLLException("File format not supported.", ex);
        }
    }
    
    public void startTask(ConvTask task) {
        ThreadPool threadPool = ThreadPool.getInstance();
        threadPool.startTask(task);
    }
    
    public void addCallableToTask(ConvTask task) throws BLLException {
        try {
            logicManager.getDataManager().addCallableToTask(task);
        } catch (DALException ex) {
            throw new BLLException("File format not supported.", ex);
        }
    }
}
