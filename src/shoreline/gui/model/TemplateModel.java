package shoreline.gui.model;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import shoreline.bll.LogicManager;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.GUIException;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class TemplateModel {
    private LogicManager logic;

    public TemplateModel(LogicManager logic) {
        this.logic = logic;
    }

    public void save(JSONObject jsonObject) throws GUIException {
        try {
            logic.getTemplateLogic().save(jsonObject);
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }
    
    public JSONObject getTemplate() throws GUIException {
        try {
            return logic.getTemplateLogic().getTemplate();
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }
    
    
}
