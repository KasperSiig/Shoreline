/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.be;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.json.JSONArray;


/**
 *
 * @author Kasper Siig
 */
public class ConvTask {
    
    public enum Status {
        Pending("Pending"),
        Running("Running"),
        Paused("Paused"),
        Finished("Finished"),
        Cancelled("Cancelled");
        
        private String status;

        Status(String status) {
            this.status = status;
        }

        public String getValue() {
            return status;
        }
    }
    
    private String name;
    private File source, target;
    private boolean isRunning;
    private Callable<ConvTask> Callable;
    private Future future;
    private int progress;
    private JSONArray jAr;
    private StringProperty status = new SimpleStringProperty("Ready");
    private Config config;

    public ConvTask(String name, File source, File target, Config config) {
        this.jAr = new JSONArray();
        this.progress = 0;
        this.name = name;
        this.source = source;
        this.target = target;
        this.config = config;
    }

    public String getName() {
        return name;
    }

    public File getSource() {
        return source;
    }

    public File getTarget() {
        return target;
    }

    public Callable<ConvTask> getCallable() {
        return Callable;
    }

    public void setCallable(Callable<ConvTask> task) {
        this.Callable = task;
    }

    public Future getFuture() {
        return future;
    }

    public void setFuture(Future future) {
        this.future = future;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public JSONArray getjAr() {
        return jAr;
    }

    public void setjAr(JSONArray jAr) {
        this.jAr = jAr;
    }

    public StringProperty getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status.setValue(status.getValue());
    }

    public Config getConfig() {
        return config;
    }
}
