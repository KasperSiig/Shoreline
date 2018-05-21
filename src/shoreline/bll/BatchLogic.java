/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import shoreline.be.Batch;
import shoreline.be.ConvTask;
import shoreline.exceptions.BLLException;

/**
 *
 * @author Kasper Siig
 */
public class BatchLogic {
    private LogicManager logicManager;
    
    private List<Batch> batches;
    

    public BatchLogic(LogicManager logicManager) {
        this.logicManager = logicManager;
        this.batches = new ArrayList();
    }
    
     public List<Batch> getBatches() {
        return batches;
    }

    public void addToBatchList(Batch batch) throws BLLException {
        batches.add(batch);
        createTasks(batch);
        runBatch(batch);
        checkBatch(batch);
    }

    public void removeFromBatchList(Batch batch) {
        batches.remove(batch);
    }

    public void runBatch(Batch batch) throws BLLException {
        List<ConvTask> tasks = new ArrayList(batch.getPendingTasks());
        for (ConvTask task : tasks) {
            logicManager.getTaskLogic().addCallableToTask(task);
            logicManager.getTaskLogic().startTask(task);
            batch.removeFromPending(task);
        }
    }

    private void createTasks(Batch batch) {
        List<File> filesInBatch = getFilesInBatch(batch);
        filesInBatch.forEach((file) -> {
            addToBatchPending(batch, file);
        });
    }

    private void addToBatchPending(Batch batch, File file) {
        String name = file.getName();
        File targetDir = batch.getTargetDir();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());

        File tempFile = new File(targetDir + "\\" + date + " - " + name + ".json");

        ConvTask task = new ConvTask(name, file, tempFile, batch.getConfig());
        task.setBatch(batch);
        batch.addToPending(task);
    }

    public List<File> getFilesInBatch(Batch batch) {
        File directory = batch.getSourceDir();

        List<File> files = Arrays.asList(directory.listFiles());
        List<File> returnList = new ArrayList();
        for (File file : files) {
            if (file.getAbsolutePath().endsWith(batch.getConfig().getExtension())) {
                returnList.add(file);
            }
        }
        return returnList;
    }

    private void checkBatch(Batch batch) {
        ThreadPool tp = ThreadPool.getInstance();
        Callable callable = (Callable) () -> {
            try {
                WatchService watcher = FileSystems.getDefault().newWatchService();
                Path dir = Paths.get(batch.getSourceDir().getAbsolutePath());
                dir.register(watcher, ENTRY_CREATE);
                while (true) {
                    WatchKey key = null;
                    try {
                        key = watcher.take();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(LogicManager.class.getName()).log(Level.INFO, "message");
                    }
                    for (WatchEvent<?> event : key.pollEvents()) {
                        WatchEvent.Kind<?> kind = event.kind();

                        @SuppressWarnings("unchecked")
                        WatchEvent<Path> ev = (WatchEvent<Path>) event;
                        Path fileName = ev.context();

                        String extension = batch.getConfig().getExtension();
                        if (fileName.toString().endsWith(extension)) {
                            addToBatchPending(batch, new File(batch.getSourceDir() + "\\" + fileName.toString()));
                            runBatch(batch);
                        }

                    }
                    boolean valid = key.reset();
                    if (!valid) {
                        break;
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(LogicManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BLLException ex) {
                Logger.getLogger(LogicManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        };
        tp.addCallableToPool(callable);

    }
}
