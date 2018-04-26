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
    private String name, description, fileExt;
    private File source, target;

    public ConvTask(HashMap<String, Integer> cellIndexMap, HashMap<String, String> mapper, String name, String description, File source, File target) {
        this.cellIndexMap = cellIndexMap;
        this.mapper = mapper;
        this.name = name;
        this.description = description;
        this.fileExt = fileExt;
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

    public String getDescription() {
        return description;
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
}
