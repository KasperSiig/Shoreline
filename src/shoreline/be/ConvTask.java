package shoreline.be;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class ConvTask {

    /**
     * Represents the different statuses a ConvTask can have
     */
    public enum Status {
        Pending("Pending"),
        Running("Running"),
        Paused("Paused"),
        Finished("Finished"),
        Cancelled("Cancelled");

        private String status;

        /**
         * Constructor for Status
         * @param status String value representing Status enum
         */
        Status(String status) {
            this.status = status;
        }

        public String getValue() {
            return status;
        }
    }

    private String name;
    private File source, target;
    private Callable<ConvTask> Callable;
    private Future future;
    private int progress;
    private StringProperty status = new SimpleStringProperty("Ready");
    private Config config;
    private Batch batch = null;

    /**
     * Constructor for ConvTask
     * 
     * @param name Name of ConvTask
     * @param source Source destination of conversion
     * @param target Target destination of conversion
     * @param config Config to be used when converting
     */
    public ConvTask(String name, File source, File target, Config config) {
        this.progress = 0;
        this.name = name;
        this.source = source;
        this.target = target;
        this.config = config;
    }

    /**
     * @return Name of ConvTask
     */
    public String getName() {
        return name;
    }

    /**
     * @return Source File of ConvTask
     */
    public File getSource() {
        return source;
    }

    /**
     * @return Target File of ConvTask
     */
    public File getTarget() {
        return target;
    }

    /**
     * Sets the target file of ConvTask
     * @param target The File to be set
     */
    public void setTarget(File target) {
        this.target = target;
    }

    /**
     * @return Callable containing conversion
     */
    public Callable<ConvTask> getCallable() {
        return Callable;
    }

    /**
     * Sets the callable in ConvTask
     * 
     * @param callable Callable to be set
     */
    public void setCallable(Callable<ConvTask> callable) {
        this.Callable = callable;
    }

    /**
     * @return Future containing Callable
     */
    public Future getFuture() {
        return future;
    }

    /**
     * Sets the future in ConvTask
     * 
     * @param future Future to be set
     */
    public void setFuture(Future future) {
        this.future = future;
    }

    /**
     * @return Progress of ConvTask
     */
    public int getProgress() {
        return progress;
    }

    /**
     * Sets the progress of ConvTask
     * @param progress new progress to be set
     */
    public void setProgress(int progress) {
        this.progress = progress;
    }

    /**
     * @return Current status of ConvTask
     */
    public StringProperty getStatus() {
        return status;
    }

    /**
     * Sets the Status of ConvTask
     * @param status Status to be set
     */
    public void setStatus(Status status) {
        this.status.setValue(status.getValue());
    }

    /**
     * @return Chosen config for ConvTask
     */
    public Config getConfig() {
        return config;
    }

    /**
     * @return Reference to what Batch the ConvTask is in
     */
    public Batch getBatch() {
        return batch;
    }

    /**
     * Sets reference to what Batch ConvTask is in
     * @param batch Batch reference to be set
     */
    public void setBatch(Batch batch) {
        this.batch = batch;
    }
}
