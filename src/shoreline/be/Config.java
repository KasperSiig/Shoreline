/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.be;

import java.util.HashMap;

/**
 *
 * @author madst
 */
public class Config {
    
    private String name, extension;
    private HashMap<String, String> headerMap;
    private HashMap<String, Integer> cellIndexMap;
    private HashMap<String, String> secondPriority;
    private HashMap<String, String> defaultValues;
    

    public Config(String name, String extension, HashMap<String, String> headerMap) {
        this.name = name;
        this.extension = extension;
        this.headerMap = headerMap;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public HashMap<String, String> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(HashMap map) {
        this.headerMap = map;
    }

    public HashMap<String, Integer> getCellIndexMap() {
        return cellIndexMap;
    }

    public void setCellIndexMap(HashMap<String, Integer> cellIndexMap) {
        this.cellIndexMap = cellIndexMap;
    }
    
    @Override
    public String toString() {
        return name;
    }

    public HashMap<String, String> getSecondPriority() {
        System.out.println(secondPriority);
        return secondPriority;
    }

    public void setSecondPriority(HashMap<String, String> SecondPriority) {
        this.secondPriority = SecondPriority;
    }

    public HashMap<String, String> getDefaultValues() {
        System.out.println(defaultValues);
        return defaultValues;
    }

    public void setDefaultValues(HashMap<String, String> defaultValues) {
        this.defaultValues = defaultValues;
    }
}
