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
import org.omg.SendingContext.RunTime;
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
    private Long cur;
    private boolean started = false;

    /**
     * Instantiates the 3 ArrayLists, and sets the ThreadPool to the max number
     * of processors available.
     */
    private ThreadPool() {
        pending = new ArrayList();
        running = new ArrayList();
        finished = new ArrayList();
        this.threadPool = Executors.newFixedThreadPool(4);
    }

    /**
     * Adds to the pending tasks list
     *
     * @param task Task to be added
     */
    public void addToPending(ConvTask task) {
        pending.add(task);
    }

    /**
     * Removes from the pending list
     *
     * @param task Task to be removed
     */
    public void removeFromPending(ConvTask task) {
        pending.remove(task);
    }

    /**
     * Adds to the finished list
     *
     * @param task Task to be added
     */
    public void addToFinished(ConvTask task) {
        finished.add(task);
        if (running.isEmpty()) {
            System.out.println(System.currentTimeMillis() - cur);
            started = false;
        }
    }

    /**
     * Remove from finished list
     *
     * @param task Task to be removed
     */
    public void removeFromFinished(ConvTask task) {
        finished.remove(task);
    }

    /**
     * Starts conversion of the task, sets the Future object to the Thread that
     * is running removes from pending list, and adds to running list.
     *
     * @param task
     */
    public void startTask(ConvTask task) {
        if (!started) {
            cur = System.currentTimeMillis();
        }
        task.setStatus(ConvTask.Status.Running);
        Future future = threadPool.submit(task.getCallable());
        task.setFuture(future);
        pending.remove(task);
        running.add(task);
        started = true;
    }

    /**
     * Removes from running list
     *
     * @param task Task to be removed
     */
    public void removeFromRunning(ConvTask task) {
        running.remove(task);
    }

    /**
     * Cancels the task passed as parameter
     *
     * @param task The task to be canceled
     * @return Whether the cancellation was successful or not
     */
    public boolean cancelTask(ConvTask task) {
        task.setStatus(ConvTask.Status.Cancelled);
        if (running.contains(task)) {
            running.remove(task);
            return task.getFuture().cancel(true);
        }
        return false;
    }

    public boolean pauseTask(ConvTask task) {
        task.setStatus(ConvTask.Status.Paused);
        if (running.contains(task)) {
            running.remove(task);
            pending.add(task);
            return task.getFuture().cancel(true);
        }
        return false;
    }

    /**
     * Shuts down the ExecutorSerivce
     */
    public void closeThreadPool() {
        threadPool.shutdown();
    }

}
