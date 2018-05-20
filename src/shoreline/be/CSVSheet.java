/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.be;

import java.util.HashMap;

/**
 *
 * @author Kasper Siig
 */
public class CSVSheet {

    private HashMap<String, Integer> headers;
    private String[][] sheet;

    public CSVSheet(HashMap<String, Integer> headers, String[][] sheet) {
        this.headers = headers;
        this.sheet = sheet;
    }

    public HashMap<String, Integer> getHeaders() {
        return headers;
    }

    public String getSheetData(int row, int column) {
        return sheet[row][column];
    }

    public String getSheetData(int row, String column) {
        return sheet[row][headers.get(column)];
    }

    public int getSize() {
        return sheet.length;
    }

    public String[] getRow(int i) {
        return sheet[i];
    }

}
