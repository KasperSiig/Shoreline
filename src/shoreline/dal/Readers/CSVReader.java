package shoreline.dal.Readers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.Stream;
import shoreline.be.CSVSheet;
import shoreline.dal.TitleStrats.CSVTitleStrat;
import shoreline.dal.TitleStrats.TitleImpl;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class CSVReader implements InputReader<CSVSheet> {

    private static final String DELIMITER = ",";

    @Override
    public CSVSheet read(File file) throws DALException {
        CSVSheet sheet = null;
        try (Scanner scanner = new Scanner(file)) {

            // Get titleIndexMap
            TitleImpl impl = new TitleImpl(new CSVTitleStrat());
            HashMap<String, Integer> headerMap = impl.getTitles(file);

            // Initialize inputArray
            int count = countLines(file);
            String[][] input = new String[count - 1][headerMap.size()];

            // copy from input file to inputArray
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
        }
        return sheet;
    }

    /**
     * Get number of lines in file
     *
     * @param file File to count lines from
     * @return number of lines in file
     */
    private int countLines(File file) throws DALException {
        int count = 0;
        try (Stream<String> lines = Files.lines(file.toPath())) {
            count = (int) lines.count();
        } catch (IOException ex) {
            throw new DALException("Error counting lines from CSV file", ex);
        }
        return count;
    }

    /**
     * Use regex to split line, as to not split on fields containing delimiter
     *
     * @param line Line to be split
     * @param delimiter Delimiter to split by
     * @return Array containing the split up line
     */
    private String[] splitLine(String line, String delimiter) {
        String[] splitLine = line.split(delimiter + "(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        for (int i = 0; i < splitLine.length; i++) {
            if (splitLine[i].contains("\"")) {
                splitLine[i] = splitLine[i].substring(1, splitLine[i].length() - 1);
            }
        }
        return splitLine;
    }

}
