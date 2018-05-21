package shoreline.be;

import java.util.HashMap;

/**
 * Holds all data about a configuration
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class Config {

    private String name, extension; 
    private HashMap<String, Integer> titleIndexMap;
    private HashMap<String, String> primaryHeaders, secondaryHeaders, defaultValues;

    /**
     * Constructor for Config
     * 
     * @param name Name of configuration
     * @param extension Extension that fits chosen configuration
     * @param primaryHeaders HashMap of primary headers
     * @param secondaryHeaders HashMap of secondary headers
     * @param defaultValues HashMap of default headers
     */
    public Config(String name, String extension, HashMap<String, String> primaryHeaders, 
            HashMap<String, String> secondaryHeaders, HashMap<String, String> defaultValues) {
        this.name = name;
        this.extension = extension;
        this.primaryHeaders = primaryHeaders;
        this.secondaryHeaders = secondaryHeaders;
        this.defaultValues = defaultValues;
    }
    
    /**
     * @return Name of Config
     */
    public String getName() {
        return name;
    }

    /**
     * @return Extension from Config
     */
    public String getExtension() {
        return extension;
    }

    /**
     * @return HashMap of TitleIndexes
     */
    public HashMap<String, Integer> getTitleIndexMap() {
        return titleIndexMap;
    }
    
    /**
     * @return HashMap of Primary Headers
     */
    public HashMap<String, String> getPrimaryHeaders() {
        return primaryHeaders;
    }

    /**
     * @return HashMap of Secondary Headers
     */
    public HashMap<String, String> getSecondaryHeaders() {
        return secondaryHeaders;
    }

    /**
     * @return HashMap of Default Values
     */
    public HashMap<String, String> getDefaultValues() {
        return defaultValues;
    }

    /**
     * Sets the titleIndexMap
     * 
     * @param titleIndexMap HashMap to be set
     */
    public void setTitleIndexMap(HashMap<String, Integer> titleIndexMap) {
        this.titleIndexMap = titleIndexMap;
    }

    @Override
    public String toString() {
        return name;
    }
}
