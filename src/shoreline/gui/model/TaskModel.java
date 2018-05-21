package shoreline.gui.model;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.BorderPane;
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

    private BorderPane borderPane;
    private LogicManager logic;

    private ObservableList<TaskView> pendingTasks;
    private ObservableList<TaskView> finishedTasks;
    private ObservableList<TaskView> cancelledTasks;

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
    public void addToPendingTasks(TaskView task) {
        pendingTasks.add(task);
    }

    /**
     * Removes task from List of pending tasks
     *
     * @param task Task to be removed
     */
    public void removeFromPendingTasks(TaskView task) {
        pendingTasks.remove(task);
    }

    /**
     * @return ObersableList containing ConvTasks
     */
    public ObservableList<TaskView> getPendingTasks() {
        return pendingTasks;
    }

    /**
     * Adds a task to the list of tasks
     *
     * @param task Task to be added
     */
    public void addToFinishedTasks(TaskView task) {
        finishedTasks.add(task);
    }

    /**
     * Removes task from List of pending tasks
     *
     * @param task Task to be removed
     */
    public void removeFromFinishedTasks(TaskView task) {
        finishedTasks.remove(task);
    }

    /**
     * @return ObersableList containing ConvTasks
     */
    public ObservableList<TaskView> getFinishedTasks() {
        return finishedTasks;
    }

    /**
     * Adds a task to the list of tasks
     *
     * @param task Task to be added
     */
    public void addToCancelledTasks(TaskView task) {
        cancelledTasks.add(task);
    }

    /**
     * Removes task from List of cancelled tasks
     *
     * @param task Task to be removed
     */
    public void removeFromCancelledTasks(TaskView task) {
        cancelledTasks.remove(task);
    }

    /**
     * @return ObersableList containing ConvTasks
     */
    public ObservableList<TaskView> getCancelledTasks() {
        return cancelledTasks;
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
        pendingTasks.addListener((ListChangeListener.Change<? extends TaskView> c) -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    TaskView taskView = pendingTasks.get(pendingTasks.size() - 1);
                    taskView.getTask().getStatus().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                        if (newValue.equals(ConvTask.Status.Running.getValue())) {
                            if (finishedTasks.contains(taskView)) {
                                removeFromFinishedTasks(taskView);
                                addToPendingTasks(taskView);
                            } else if (cancelledTasks.contains(taskView)) {
                                removeFromCancelledTasks(taskView);
                                addToPendingTasks(taskView);
                            }
                        } else if (newValue.equals(ConvTask.Status.Finished.getValue()) && !finishedTasks.contains(taskView)) {
                            removeFromPendingTasks(taskView);
                            addToFinishedTasks(taskView);
                        } else if (newValue.equals(ConvTask.Status.Cancelled.getValue()) && !cancelledTasks.contains(taskView)) {
                            removeFromPendingTasks(taskView);
                            addToCancelledTasks(taskView);
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
