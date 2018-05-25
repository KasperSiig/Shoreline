package shoreline.dal.TitleStrats;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class CSVTitleStrat implements TitleStrategy {

    private static final String DELIMITER = ",";

    @Override
    public HashMap<String, Integer> getTitles(File file) throws DALException {
        HashMap<String, Integer> headerMap = new HashMap();
        try (Scanner scanner = new Scanner(file)) {
            Thread.sleep(50);

            String firstLine = scanner.nextLine();
            String[] headers = splitLine(firstLine, DELIMITER);

            // Puts the headers into headerMap, appending an int if already exists
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
        } catch (FileNotFoundException | InterruptedException ex) {
            throw new DALException("Could not get titles from CSV sheet", ex);
        }
        return headerMap;
    }

    /**
     * Use regex to split line, as to not split on fields containing delimiter
     *
     * @param line Line to be split
     * @param delimiter Delimiter to split by
     * @return Array containing the split up line
     */
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
