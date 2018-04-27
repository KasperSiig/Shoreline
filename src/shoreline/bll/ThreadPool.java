/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.bll;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import shoreline.be.ConvTask;

/**
 *
 * @author Kasper Siig
 */
public class ThreadPool {

    // Start Singleton
    private static ThreadPool instance;

    public static ThreadPool getInstance() {
        if (instance == null) {
            instance = new ThreadPool();
        }
        return instance;
    }
    // End Singleton
    private ExecutorService threadPool;
    private List<ConvTask> pending;
    private List<ConvTask> running;
    private List<ConvTask> finished;

    private ThreadPool() {
        pending = new ArrayList();
        running = new ArrayList();
        finished = new ArrayList();
        this.threadPool = Executors.newFixedThreadPool(1);
    }

    public void addToPending(ConvTask task) {
        pending.add(task);
    }

    public void removeFromPending(ConvTask task) {
        pending.remove(task);
    }

    public void addToFinished(ConvTask task) {
        finished.add(task);
    }

    public void removeFromFinished(ConvTask task) {
        finished.remove(task);
    }
    
    public void startTask(ConvTask task) {
        System.out.println("thread started");
        Future future = threadPool.submit(task.getTask());
        task.setFuture(future);
        pending.remove(task);
        running.add(task);
    }

    public void removeFromRunning(ConvTask task) {
        running.remove(task);
    }

    public boolean cancelTask(ConvTask task) {
        if (running.contains(task)) {
            running.remove(task);
            return task.getFuture().cancel(true);
        }
        return false;
    }
    
    public void closeThreadPool() {
        threadPool.shutdown();
    }
    

}
