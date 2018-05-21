package shoreline.be;

import shoreline.exceptions.BEException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Business Entity for Batch conversion
 * 
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class Batch {
    private File sourceDir, targetDir, failedDir;
    private String name;
    private Config config;
    private IntegerProperty filesPending, filesHandled, filesFailed;
    private List<ConvTask> pendingTasks;

    /**
     * Constructor for Batch
     * 
     * @param sourceDir Folder to convert from
     * @param targetDir Folder to convert to
     * @param name Name of Batch
     * @param config Configuration to use when converting 
     */
    public Batch(File sourceDir, File targetDir, String name, Config config) {
        this.sourceDir = sourceDir;
        this.targetDir = targetDir;
        this.name = name;
        this.config = config;
        this.failedDir = getFailedDir(targetDir);
        this.filesPending = new SimpleIntegerProperty(0);
        this.filesHandled = new SimpleIntegerProperty(0);
        this.filesFailed = new SimpleIntegerProperty(0);
        this.pendingTasks = new ArrayList();
    }

    /**
     * Creates a "Failed" folder, if it doesn't already exists
     * 
     * @param targetDir Folder to create "Failed" folder in
     * @return File to "Failed" folder
     */
    private File getFailedDir(File targetDir) {
        File file = new File(targetDir.getAbsolutePath() + "\\failed");
        file.mkdir();
        return file;
    }

    /**
     * @return Source Directory
     */
    public File getSourceDir() {
        return sourceDir;
    }

    /**
     * @return Target Directory 
     */
    public File getTargetDir() {
        return targetDir;
    }

    /**
     * @return Failed Directory
     */
    public File getFailedDir() {
        return failedDir;
    }

    /**
     * @return Name of Batch
     */
    public String getName() {
        return name;
    }

    /**
     * @return Configuration used
     */
    public Config getConfig() {
        return config;
    }
    
    /**
     * @return IntegerProperty of filesPending
     */
    public IntegerProperty getFilesPending() {
        return filesPending;
    }

    /**
     * @return IntegerProperty of filesHandled
     */
    public IntegerProperty getFilesHandled() {
        return filesHandled;
    }

    /**
     * @return IntegerProperty of filesFailed
     */
    public IntegerProperty getFilesFailed() {
        return filesFailed;
    }

    /**
     * @return List of pendingTasks
     */
    public List<ConvTask> getPendingTasks() {
        return pendingTasks;
    }

    /**
     * Removes ConvTask from pendingTasks
     * 
     * @param task ConvTask to be removed
     */
    public void removeFromPending(ConvTask task) {
        pendingTasks.remove(task);
    }
    
    /**
     * Adds ConvTask to pendingTasks and increments filesPending
     * 
     * @param task ConvTask to be added
     */
    public void addToPending(ConvTask task) {
        pendingTasks.add(task);
        increment(filesPending);
    }
    
    /**
     * Increments IntegerProperty
     * 
     * @param prop IntegerProperty to be incremented
     */
    public void increment(IntegerProperty prop) {
        prop.setValue(prop.intValue() + 1);
    }
    
    /**
     * Decrements IntegerProperty
     * @param prop IntegerProperty to be decremented
     */
    public void decrement(IntegerProperty prop) {
        prop.setValue(prop.intValue() - 1);
    }
    
    @Override
    public String toString() {
        return "Batch{" + "sourceDir=" + sourceDir + ", targetDir=" + targetDir + ", name=" + name + ", pendingTasks=" + pendingTasks + '}';
    }    
}
