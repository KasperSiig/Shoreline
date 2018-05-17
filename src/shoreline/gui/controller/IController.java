package shoreline.gui.controller;

import shoreline.gui.model.ModelManager;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public interface IController {
    /**
     * Sets all the needed data for a controller. If more data is needed, than 
     * what the model contains, a different method for that can be implemented
     * in the individual controllers.
     * 
     * @param model 
     */    
    public void postInit(ModelManager model);
}
