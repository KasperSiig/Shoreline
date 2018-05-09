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
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
        
        String status;

        Status(String status) {
            this.status = status;
        }

        public String getValue() {
            return status;
        }
    }
    
    private HashMap<String, Integer> cellIndexMap;
    private HashMap<String, String> mapper;
    private String name;
    private File source, target;
    private boolean isRunning;
    private Callable<ConvTask> Callable;
    private Future future;
    private int progress;
    private JSONArray jAr;
    private StringProperty status = new SimpleStringProperty("Ready");

    public ConvTask(HashMap<String, Integer> cellIndexMap, HashMap<String, String> mapper, String name, File source, File target) {
        this.jAr = new JSONArray();
        this.progress = 0;
        this.cellIndexMap = cellIndexMap;
        this.mapper = mapper;
        this.name = name;
        this.source = source;
        this.target = target;
    }

    public HashMap<String, Integer> getCellIndexMap() {
        return cellIndexMap;
    }

    public HashMap<String, String> getMapper() {
        return mapper;
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
    
    
}
