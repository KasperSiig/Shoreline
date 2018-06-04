package shoreline.bll;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import shoreline.be.ConvTask;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
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
    private List<ConvTask> running;
    private List<ConvTask> finished;

    /**
     * Instantiates the 3 ArrayLists, and sets the ThreadPool to the max number
     * of processors available.
     */
    private ThreadPool() {
        running = new ArrayList();
        finished = new ArrayList();
        this.threadPool = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());
    }

    /**
     * Adds to the finished list
     *
     * @param task Task to be added
     */
    public void addToFinished(ConvTask task) {
        finished.add(task);
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
        task.setStatus(ConvTask.Status.Running);
        Future future = threadPool.submit(task.getCallable());
        task.setFuture(future);
        running.add(task);
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
            return task.getFuture().cancel(true);
        }
        return false;
    }

    /**
     * Shuts down the ExecutorSerivce, cancelling everything running
     */
    public void shutdownNow() {
        threadPool.shutdownNow();
    }
    
    /**
     * Shuts down the ExecutorService, letting things finish
     */
    public void shutdown() {
        threadPool.shutdown();
    }
    
    /**
     * Gives the ability to submit any given callable to ThreadPool
     * 
     * @param callable Callable to add to ThreadPool
     */
    public Future addCallableToPool(Callable callable) {
        return threadPool.submit(callable);
    }
    

}
