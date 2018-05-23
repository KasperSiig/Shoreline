package shoreline.bll;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;
import shoreline.be.Batch;
import shoreline.be.ConvTask;
import shoreline.exceptions.BLLException;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class BatchLogic extends LogicClass {

    private List<Batch> batches;

    /**
     * Constructor for BatchLogic
     *
     * @param logicManager Reference back to the LogicManager
     */
    public BatchLogic(LogicManager logicManager) {
        super(logicManager);
        this.batches = new ArrayList();
    }

    /**
     * @return List of Batches
     */
    public List<Batch> getBatches() {
        return batches;
    }

    /**
     * Adds a Batch to the list of Batches, creates task for files already in
     * source folder, runs the batch, and adds a folder listener
     *
     * @param batch Batch to be added
     * @throws BLLException If there was a problem running the batch
     */
    public void addToBatchList(Batch batch) throws BLLException {
        batches.add(batch);
        addExistingFilesToBatch(batch);
        runBatch(batch);
        addFolderListener(batch);
    }

    /**
     * Removes a Batch from list of batches
     *
     * @param batch Batch to be removed
     */
    public void removeFromBatchList(Batch batch) {
        batches.remove(batch);
    }

    /**
     * Runs all the pending tasks in a Batch
     *
     * @param batch Batch to run
     * @throws BLLException if there was a problem running the tasks
     */
    public void runBatch(Batch batch) throws BLLException {
        System.out.println("runs batch");
        List<ConvTask> tasks = new ArrayList(batch.getPendingTasks());
        System.out.println(tasks);
        for (ConvTask task : tasks) {
            batch.removeFromPending(task);
            logicManager.getTaskLogic().addCallableToTask(task);
            logicManager.getTaskLogic().startTask(task);
        }
    }

    /**
     * Adds existing files to pendingTasks in batch
     *
     * @param batch Batch to add files to
     */
    private void addExistingFilesToBatch(Batch batch) {
        List<File> filesInBatch = getFilesInBatch(batch);
        filesInBatch.forEach((file) -> {
            ConvTask task = createTask(batch, file);
            batch.addToPending(task);
        });
    }

    /**
     * Creates new ConvTask
     *
     * @param batch Batch to add ConvTask to
     * @param file File to create ConvTask from
     */
    private ConvTask createTask(Batch batch, File file) {
        String name = file.getName();
        File targetDir = batch.getTargetDir();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());

        File tempFile = new File(targetDir + "\\" + date + " - " + name + ".json");

        ConvTask task = new ConvTask(name, file, tempFile, batch.getConfig());
        task.setBatch(batch);
        System.out.println("set batch");
        return task;
    }

    /**
     * @param batch Batch to get files from
     * @return List of files already in batch
     */
    public List<File> getFilesInBatch(Batch batch) {
        File directory = batch.getSourceDir();

        List<File> returnList = new ArrayList();
        for (File file : directory.listFiles()) {
            if (file.getAbsolutePath().endsWith(batch.getConfig().getExtension())) {
                returnList.add(file);
            }
        }
        return returnList;
    }

    /**
     * Creates a listener for the source folder in Batch
     *
     * @param batch Batch to add listener to
     */
    private void addFolderListener(Batch batch) {
        ThreadPool tp = ThreadPool.getInstance();
        Callable callable = (Callable) () -> {
            try {
                // Gets a new WatchService and registers the batch to it
                WatchService watcher = FileSystems.getDefault().newWatchService();
                Path dir = Paths.get(batch.getSourceDir().getAbsolutePath());
                dir.register(watcher, ENTRY_CREATE);

                // Checks for any new files
                while (true) {
                    WatchKey key = null;
                    key = watcher.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        
//                        @SuppressWarnings("unchecked")
                        WatchEvent<Path> ev = (WatchEvent<Path>) event;
                        Path fileName = ev.context();

                        String extension = batch.getConfig().getExtension();
                        if (fileName.toString().endsWith(extension)) {
                            System.out.println("found new file");
                            ConvTask task = createTask(batch, new File(batch.getSourceDir() + "\\" + fileName.toString()));
                            batch.addToPending(task);
                            runBatch(batch);
                        }
                    }
                    boolean valid = key.reset();
                    if (!valid) {
                        break;
                    }
                }
            } catch (IOException ex) {
                throw new BLLException("Error adding folder listener", ex);
            }
            return null;
        };
        // Listener is being added to ThreadPool, so it doesn't interfere with main Thread
        tp.addCallableToPool(callable);
    }
}
