package shoreline.bll;

import java.io.File;
import java.util.HashMap;
import shoreline.be.ConvTask;
import shoreline.bll.ConvStrats.CSVConvStrat;
import shoreline.bll.ConvStrats.ConvImpl;
import shoreline.bll.ConvStrats.XLXSConvStrat;
import shoreline.dal.DataManager;
import shoreline.dal.Readers.CSVReader;
import shoreline.dal.Readers.XLSXReader;
import shoreline.dal.Writers.StringToFile;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class TaskLogic extends LogicClass{

    /**
     * @param dataManager Holds a reference to DataManager
     */
    public TaskLogic(DataManager dataManager) {
        super(dataManager);
    }
    
    /**
     * Gets titles from a given file
     * 
     * @param file File to get titles from
     * @return HashMap containing titles and their indexes in the given file
     * @throws BLLException 
     */
    public HashMap<String, Integer> getTitles(File file) throws BLLException {
        try {
            return dataManager.getTitles(file);
        } catch (DALException ex) {
            throw new BLLException("File format not supported.", ex);
        }
    }
    
    /**
     * Starts task conversion in ThreadPool
     * 
     * @param task Task to be started
     */
    public void startTask(ConvTask task) {
        ThreadPool threadPool = ThreadPool.getInstance();
        threadPool.startTask(task);
    }
    
    /**
     * Adds callable to ConvTask, making it ready for conversion
     * 
     * @param task ConvTask to add callable to
     * @throws BLLException 
     */
    public void addCallableToTask(ConvTask task) throws BLLException {
        String extension = "";

        int i = task.getSource().getAbsolutePath().lastIndexOf('.');
        if (i > 0) {
            extension = task.getSource().getAbsolutePath().substring(i + 1);
        }
        ConvImpl impl;
        switch (extension) {
            case "xlsx":
                impl = new ConvImpl(new XLXSConvStrat(), 
                        new XLSXReader(), new StringToFile());
                break;
            case "csv":
                impl = new ConvImpl(new CSVConvStrat(), 
                        new CSVReader(), new StringToFile());
                break;
            default:
                throw new IllegalArgumentException();
        }
        impl.addCallable(task);
    }
}
