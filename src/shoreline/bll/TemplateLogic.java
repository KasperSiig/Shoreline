package shoreline.bll;

import org.json.JSONObject;
import shoreline.dal.DataManager;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.DALException;

/**
 *
 * @author kaspe
 */
public class TemplateLogic extends LogicClass {

    public TemplateLogic(DataManager dataManager) {
        super(dataManager);
    }

    public void save(JSONObject jsonObject) throws BLLException {
        try {
            dataManager.saveTemplate(jsonObject);
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }
    
    public JSONObject getTemplate() throws BLLException {
        try {
            return dataManager.getTemplate();
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }
    
}
