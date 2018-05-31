package shoreline.gui.model;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import shoreline.be.ConvTask;
import shoreline.bll.LogicManager;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.GUIException;
import shoreline.gui.controller.TaskView;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class TaskModel {

    private LogicManager logic;

    private ObservableList<TaskView> pending;
    private ObservableList<TaskView> finished;
    private ObservableList<TaskView> cancelled;

    public TaskModel(LogicManager logic) {
        this.logic = logic;

        this.pending = FXCollections.observableArrayList();
        this.finished = FXCollections.observableArrayList();
        this.cancelled = FXCollections.observableArrayList();
        addListeners();
    }

    /**
     * Adds a task to the list of tasks
     *
     * @param task Task to be added
     */
    public void addToPending(TaskView task) {
        pending.add(task);
    }

    /**
     * Removes task from List of pending tasks
     *
     * @param task Task to be removed
     */
    public void removeFromPending(TaskView task) {
        pending.remove(task);
    }

    /**
     * @return ObersableList containing ConvTasks
     */
    public ObservableList<TaskView> getPending() {
        return pending;
    }

    /**
     * Adds a task to the list of tasks
     *
     * @param task Task to be added
     */
    public void addToFinished(TaskView task) {
        finished.add(task);
    }

    /**
     * Removes task from List of pending tasks
     *
     * @param task Task to be removed
     */
    public void removeFromFinished(TaskView task) {
        finished.remove(task);
    }

    /**
     * @return ObersableList containing ConvTasks
     */
    public ObservableList<TaskView> getFinished() {
        return finished;
    }

    /**
     * Adds a task to the list of tasks
     *
     * @param task Task to be added
     */
    public void addToCancelled(TaskView task) {
        cancelled.add(task);
    }

    /**
     * Removes task from List of cancelled tasks
     *
     * @param task Task to be removed
     */
    public void removeFromCancelled(TaskView task) {
        cancelled.remove(task);
    }

    /**
     * @return ObersableList containing ConvTasks
     */
    public ObservableList<TaskView> getCancelled() {
        return cancelled;
    }

    /**
     * Starts a task in ThreadPool
     *
     * @param task Task to be converted
     */
    public void start(ConvTask task) {
        logic.getTaskLogic().startTask(task);
    }

    /**
     * Adds listeners to the ObservableLists
     */
    private void addListeners() {
        pending.addListener((ListChangeListener.Change<? extends TaskView> c) -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    TaskView taskView = pending.get(pending.size() - 1);
                    taskView.getTask().getStatus().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                        if (newValue.equals(ConvTask.Status.Running.getValue())) {
                            if (finished.contains(taskView)) {
                                removeFromFinished(taskView);
                                addToPending(taskView);
                            } else if (cancelled.contains(taskView)) {
                                removeFromCancelled(taskView);
                                addToPending(taskView);
                            }
                        } else if (newValue.equals(ConvTask.Status.Finished.getValue()) && !finished.contains(taskView)) {
                            removeFromPending(taskView);
                            addToFinished(taskView);
                        } else if (newValue.equals(ConvTask.Status.Cancelled.getValue()) && !cancelled.contains(taskView)) {
                            removeFromPending(taskView);
                            addToCancelled(taskView);
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
            logic.getTaskLogic().addCallableToTask(task);
        } catch (BLLException ex) {
            throw new GUIException(ex);
        }
    }
}
