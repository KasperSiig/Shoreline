package shoreline.bll;

/**
 * Holds information that all Logic Classes should have
 * 
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public abstract class LogicClass {
    protected LogicManager logicManager;

    public LogicClass(LogicManager logicManager) {
        this.logicManager = logicManager;
    }
}
