package shoreline.gui.model;

import java.util.ArrayList;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.BorderPane;
import shoreline.be.ConvTask;
import shoreline.bll.LogicManager;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.GUIException;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class TaskModel {

    private BorderPane borderPane;
    private LogicManager logic;

    private ObservableList<ConvTask> pendingTasks;
    private ObservableList<ConvTask> finishedTasks;
    private ObservableList<ConvTask> cancelledTasks;

    public TaskModel(BorderPane borderPane, LogicManager logic) {
        this.borderPane = borderPane;
        this.logic = logic;

        this.pendingTasks = FXCollections.observableArrayList();
        this.finishedTasks = FXCollections.observableArrayList();
        this.cancelledTasks = FXCollections.observableArrayList();
        addListeners();
    }

    /**
     * Adds a task to the list of tasks
     *
     * @param task Task to be added
     */
    public void addToPendingTasks(ConvTask task) {
        pendingTasks.add(task);
    }

    /**
     * Removes task from List of pending tasks
     *
     * @param task Task to be removed
     */
    public void removeFromPendingTasks(ConvTask task) {
        pendingTasks.remove(task);
    }

    /**
     * @return ObersableList containing ConvTasks
     */
//    public ObservableList<ConvTask> getTaskList() {
//        return pendingTasks;
//    }
    public ObservableList<ConvTask> getPendingTasks() {
        return pendingTasks;
    }

    /**
     * Adds a task to the list of tasks
     *
     * @param task Task to be added
     */
    public void addToFinishedTasks(ConvTask task) {
        finishedTasks.add(task);
        System.out.println("shoreline.gui.model.MainModel.addToFinishedTasks()");
        System.out.println("finishedTasks = " + finishedTasks.size() + "\n");
    }

    /**
     * Removes task from List of pending tasks
     *
     * @param task Task to be removed
     */
    public void removeFromFinishedTasks(ConvTask task) {
        finishedTasks.remove(task);
    }

    /**
     * @return ObersableList containing ConvTasks
     */
    public ObservableList<ConvTask> getFinishedTasks() {
        return finishedTasks;
    }

    /**
     * Adds a task to the list of tasks
     *
     * @param task Task to be added
     */
    public void addToCancelledTasks(ConvTask task) {
        cancelledTasks.add(task);
    }

    /**
     * Removes task from List of cancelled tasks
     *
     * @param task Task to be removed
     */
    public void removeFromCancelledTasks(ConvTask task) {
        cancelledTasks.remove(task);
    }

    /**
     * @return ObersableList containing ConvTasks
     */
    public ObservableList<ConvTask> getCancelledTasks() {
        return cancelledTasks;
    }

    /**
     * Starts a task in ThreadPool
     *
     * @param task Task to be converted
     */
    public void start(ConvTask task) {
        logic.startTask(task);
    }

    /**
     * Adds listeners to the ObservableLists
     */
    private void addListeners() {
        pendingTasks.addListener((ListChangeListener.Change<? extends ConvTask> c) -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    ConvTask task = pendingTasks.get(pendingTasks.size() - 1);
                    task.getStatus().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                        if (newValue.equals(ConvTask.Status.Running.getValue())) {
                        } else if (newValue.equals(ConvTask.Status.Finished.getValue()) && !finishedTasks.contains(task)) {
                            removeFromPendingTasks(task);
                            addToFinishedTasks(task);
                        } else if (newValue.equals(ConvTask.Status.Cancelled.getValue()) && !cancelledTasks.contains(task)) {
                            removeFromPendingTasks(task);
                            addToCancelledTasks(task);
                        }
                    });
                }
            }
        });
    }

    /**
     * Adds the conversion task to the ConvTask, making it ready for conversion
     *
     * @param task Task to get ready
     * @throws GUIException
     */
    public void addCallable(ConvTask task) throws GUIException {
        try {
            logic.addCallableToTask(task);
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }
}
