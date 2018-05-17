package shoreline.be;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author Kasper Siig
 */
public class Batch {
    private File sourceDir, targetDir, failedDir;
    private String name;
    private Config config;
    private IntegerProperty filesPending, filesHandled, filesFailed;
    private List<ConvTask> pendingTasks;
    private WatchService watchService;

    public Batch(File sourceDir, File targetDir, String name, Config config) throws BEException {
        this.sourceDir = sourceDir;
        this.targetDir = targetDir;
        this.name = name;
        this.config = config;
        this.failedDir = getFailedDir(targetDir);
        this.filesPending = new SimpleIntegerProperty(0);
        this.filesHandled = new SimpleIntegerProperty(0);
        this.filesFailed = new SimpleIntegerProperty(0);
        this.pendingTasks = new ArrayList();
        try {
            this.watchService = FileSystems.getDefault().newWatchService();
        } catch (IOException ex) {
            throw new BEException("Something went wrong, creating a WatcherService", ex);
        }
    }

    private File getFailedDir(File targetDir) {
        File file = new File(targetDir.getAbsolutePath() + "\\failed");
        file.mkdir();
        return file;
    }

    public File getSourceDir() {
        return sourceDir;
    }

    public File getTargetDir() {
        return targetDir;
    }

    public File getFailedDir() {
        return failedDir;
    }

    public String getName() {
        return name;
    }

    public Config getConfig() {
        return config;
    }

    public IntegerProperty getFilesPending() {
        return filesPending;
    }

    public IntegerProperty getFilesHandled() {
        return filesHandled;
    }

    public IntegerProperty getFilesFailed() {
        return filesFailed;
    }

    public List<ConvTask> getPendingTasks() {
        return pendingTasks;
    }

    public WatchService getWatchService() {
        return watchService;
    }

    public void removeFromPending(ConvTask task) {
        pendingTasks.remove(task);
    }
    
    public void addToPending(ConvTask task) {
        pendingTasks.add(task);
    }
    
    
    
}
