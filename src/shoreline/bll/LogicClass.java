package shoreline.bll;

import shoreline.dal.DataManager;

/**
 * Holds information that all Logic Classes should have
 * 
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public abstract class LogicClass {
    protected DataManager dataManager;

    public LogicClass(DataManager dataManager) {
        this.dataManager = dataManager;
    }
    
}
