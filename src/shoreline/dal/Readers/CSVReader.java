/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.dal.Readers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import shoreline.be.CSVSheet;
import shoreline.dal.TitleStrats.CSVTitleStrat;
import shoreline.dal.TitleStrats.TitleImpl;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kasper Siig
 */
public class CSVReader implements InputReader<CSVSheet> {

    private static final String DELIMITER = ",";

    @Override
    public CSVSheet read(File file) throws DALException {
        CSVSheet sheet = null;
        try {
            Thread.sleep(50);
            Scanner scanner = new Scanner(file);
            TitleImpl impl = new TitleImpl(new CSVTitleStrat());
            HashMap<String, Integer> headerMap = impl.getTitles(file);
            System.out.println("before count");
            int count = countLines(file);
            System.out.println("after count");
            String[][] input = new String[count - 1][headerMap.size()];
            int i = 0;
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String nextLine = scanner.nextLine();
                String[] temp = splitLine(nextLine, DELIMITER);
                System.arraycopy(temp, 0, input[i], 0, temp.length);
                i++;
            }

            sheet = new CSVSheet(headerMap, input);
        } catch (FileNotFoundException ex) {
            throw new DALException("Error reading CSV File", ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(CSVReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sheet;
    }

    private int countLines(File file) {
        int count = 0;
        try (Stream<String> lines = Files.lines(file.toPath())) {
            count = (int) lines.count();
        } catch (IOException ex) {
            Logger.getLogger(CSVReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
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
