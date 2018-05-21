package shoreline.be;

import java.util.HashMap;

/**
 * Holds all data in a given CSV File
 * 
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class CSVSheet {

    private HashMap<String, Integer> titleIndexMap;
    private String[][] sheet;

    /**
     * Constructor for CSVSheet
     * 
     * @param titleIndexMap HashMap of h
     * @param sheet 
     */
    public CSVSheet(HashMap<String, Integer> titleIndexMap, String[][] sheet) {
        this.titleIndexMap = titleIndexMap;
        this.sheet = sheet;
    }

    /**
     * @return HashMap with indexes of different titles in sheet
     */
    public HashMap<String, Integer> getTitleIndexMap() {
        return titleIndexMap;
    }

    /**
     * @param row Row to get data from
     * @param column Column to get data from
     * @return String from given row and column
     */
    public String getSheetData(int row, int column) {
        return sheet[row][column];
    }

    /**
     * @param row Row to get data from
     * @param column Column header to get data from
     * @return String from given row and column header
     */
    public String getSheetData(int row, String column) {
        return sheet[row][titleIndexMap.get(column)];
    }

    /**
     * @return Row count of CSVSheet
     */
    public int getRowCount() {
        return sheet.length;
    }

}
