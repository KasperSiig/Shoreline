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
    
    private String name,extension;
    private HashMap<String, String> map;

    public Config(String name, String extension, HashMap<String, String> map) {
        this.name = name;
        this.extension = extension;
        this.map = map;
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

    public HashMap getMap() {
        return map;
    }

    public void setMap(HashMap map) {
        this.map = map;
    }
    
}
