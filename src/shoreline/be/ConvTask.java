/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.be;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author Kasper Siig
 */
public class ConvTask {
    private HashMap<String, Integer> cellIndexMap;
    private HashMap<String, String> mapper;
    private String name, fileExt;
    private File source, target;
    private boolean isRunning;

    public ConvTask(HashMap<String, Integer> cellIndexMap, HashMap<String, String> mapper, String name, File source, File target) {
        this.cellIndexMap = cellIndexMap;
        this.mapper = mapper;
        this.name = name;
        this.fileExt = fileExt;
        this.source = source;
        this.target = target;
        isRunning = false;
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

    public String getFileExt() {
        return fileExt;
    }

    public File getSource() {
        return source;
    }

    public File getTarget() {
        return target;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setIsRunning() {
        this.isRunning = !isRunning;
    }
}
