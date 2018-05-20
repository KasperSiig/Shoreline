/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.dal.TitleStrats;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kasper Siig
 */
public class CSVTitleStrat implements TitleStrategy {
    private static final String DELIMITER = ";";
    
    @Override
    public HashMap<String, Integer> getTitles(File file) throws DALException {
        HashMap<String, Integer> headerMap = null;
        try {
            Scanner scanner = new Scanner(file);
            String firstLine = scanner.nextLine();
            String[] headers = splitLine(firstLine, DELIMITER);
            headerMap= new HashMap();
            for (int i = 0; i < headers.length; i++) {
                String tempName = headers[i];
                if (!headerMap.containsKey(tempName)) {
                    headerMap.put(tempName, i);
                } else {
                    int j = 0;
                    boolean done = false;
                    while (!done) {
                        if (!headerMap.containsKey(tempName + ++j)) {
                            headerMap.put(tempName + j, i);
                            done = true;
                        }
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CSVTitleStrat.class.getName()).log(Level.SEVERE, null, ex);
        }
        return headerMap;
    }
    
    private String[] splitLine(String firstLine, String delimiter) {
        String[] line = firstLine.split(delimiter + "(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        for (int i = 0; i < line.length; i++) {
                if (line[i].contains("\"")) {
                    line[i] = line[i].substring(1, line[i].length() - 1);
                }
            }
        return line;
    }

}
