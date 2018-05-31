package shoreline.bll.ConvStrats;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.Callable;
import javafx.application.Platform;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import shoreline.be.Config;
import shoreline.be.ConvTask;
import shoreline.bll.ThreadPool;
import shoreline.dal.Readers.InputReader;
import shoreline.dal.TitleStrats.TitleImpl;
import shoreline.dal.TitleStrats.XLSXTitleStrat;
import shoreline.dal.Writers.OutputWriter;
import shoreline.exceptions.BLLException;
import shoreline.exceptions.DALException;

/**
 * Conversion strategy for XLSX files
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class XLXSConvStrat implements ConvStrategy {

    // The Workbook containing all sheets in XLSX file
    private XSSFWorkbook wb;

    // The Sheet holding the data from the XLSX file
    private XSSFSheet sheet1;

    @Override
    public void addCallable(ConvTask task, InputReader reader, OutputWriter writer) throws BLLException {
        // Gets the instance of the ThreadPool object
        ThreadPool threadPool = ThreadPool.getInstance();
        // Creates the Callable, that's going to be added to the ConvTask
        Callable call = (Callable) () -> {
            // Gets titleIndexMap with XLSXTitleStrat, and sets it in the config
            TitleImpl impl = new TitleImpl(new XLSXTitleStrat());
            HashMap<String, Integer> titleIndexMap = impl.getTitles(task.getSource());
            task.getConfig().setTitleIndexMap(titleIndexMap);

            wb = (XSSFWorkbook) reader.read(task.getSource());
            // XLSX files can contain more sheets, this gets the one at index 0
            sheet1 = wb.getSheetAt(0);
            writeJson(task, writer);
            /* The callable will reach this even if the task is paused, so a check is
               to verify that it is done */
            if (task.getStatus().getValue().equals(ConvTask.Status.Running.getValue())) {
                Platform.runLater(() -> {
                    task.setStatus(ConvTask.Status.Finished);
                });
                threadPool.removeFromRunning(task);
                threadPool.addToFinished(task);
                task.setProgress(0);
                // Checks to see if the task is part of a batch
                if (task.getBatch() != null) {
                    Platform.runLater(() -> {
                        task.getBatch().decrement(task.getBatch().getFilesPending());
                        task.getBatch().increment(task.getBatch().getFilesHandled());
                    });
                }
            }
            return null;
        };

        // Sets the Callable in the ConvTask, so it can be used in the ThreadPool later.
        task.setCallable(call);
    }

    /**
     * Gets data from the XLSX file
     *
     * @param header The template header to link to
     * @param rowNumber The row number to get data from
     * @param task ConvTask to get headers and config from
     * @return Contents of a cell from XLSX file
     */
    private String getSheetdata(String header, int rowNumber, ConvTask task) {
        Config config = task.getConfig();

        HashMap<String, Integer> titleIndexMap = config.getTitleIndexMap();
        HashMap<String, String> primaryHeaders = config.getPrimaryHeaders();
        HashMap<String, String> secondaryHeaders = config.getSecondaryHeaders();
        HashMap<String, String> defaultValues = config.getDefaultValues();

        String rtn = "";

        // If header exists in primaryHeaders
        if (primaryHeaders.get(header) != null) {
            rtn = getCellData(rowNumber, titleIndexMap, primaryHeaders.get(header));
            // If given cell is not empty
            if (!rtn.isEmpty()) {
                return rtn;
            }
        }

        if (secondaryHeaders.get(header) != null) {
            rtn = getCellData(rowNumber, titleIndexMap, secondaryHeaders.get(header));
            if (!rtn.isEmpty()) {
                return rtn;
            }
        }

        if (defaultValues.get(header) != null) {
            return defaultValues.get(header);
        }

        return rtn;
    }

    /**
     * Get stringValue from specific cell
     *
     * @param rowNumber Row to get data from
     * @param titelIndexMap HashMap of titleIndexes
     * @param indexString Column header to get data from
     * @return StringValue of the specific cell, blank if null
     */
    private String getCellData(int rowNumber, HashMap<String, Integer> titelIndexMap, String indexString) {
        return sheet1.getRow(rowNumber).getCell(titelIndexMap.get(indexString), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
    }

    /**
     * Writes the XLSX file to given output
     *
     * @param task ConvTask containing information about conversion
     * @param writer How the information should be written
     * @throws DALException If there was a problem writing to the file
     */
    private void writeJson(ConvTask task, OutputWriter writer) throws BLLException {
        try {
            // If task is starting, and not continuing from a pause, create new file
            if (task.getProgress() == 0) {

                task.setTarget(checkForExistingFile(task.getTarget()));
                writer.write(task.getTarget(), "[");

            }
            // Is being used to keep track of what row to pull data from
            int i = task.getProgress() + 1;
            while (sheet1.getRow(i) != null
                    && task.getStatus().getValue().equals(ConvTask.Status.Running.getValue())) {
                JSONObject jOb = createJSONObject(i, task);
                task.setProgress(i);
                String seperator = "\n,";
                if (i == 1) {
                    seperator = "\n";
                }
                writer.write(task.getTarget(), seperator + jOb.toString(4));
                i++;
            }
            // If the last row has been written, close the JSONArray
            if (i == sheet1.getLastRowNum() + 1) {
                writer.write(task.getTarget(), "\n]");
            }
        } catch (DALException ex) {
            throw new BLLException(ex);
        }
    }

    /**
     * Creates new JSONObject
     *
     * @param i What row to pull data from in XLSX file
     * @param task ConvTask containing information about conversion
     * @return JSONObject containing information, taken from XLSX file
     */
    private JSONObject createJSONObject(int i, ConvTask task) {
        JSONObject jOb = new JSONObject();
        JSONObject planning = new JSONObject();

        task.getConfig().getOutputHeaders().forEach((string) -> {
            switch (string) {
                case "earliestStartDate":
                case "latestFinishDate":
                case "latestStartDate":
                    planning.put(string, getSheetdata(string, i, task));
                    break;
                case "estimatedTime":
                    planning.put("estimatedTime", getSheetdata(string, i, task));
                    break;
                default:
                    jOb.put(string, getSheetdata(string, i, task));
                    break;
            }
        });

        jOb.put("createdOn", Calendar.getInstance().getTime());
        jOb.put("planning", planning);
        return jOb;
    }

    /**
     * Check if filename already exists. If it does, append (i) to the end.
     *
     * @param file File to check for existence
     * @return File containing available filename
     */
    private File checkForExistingFile(File file) {
        int i = 1;
        File tempFile = file;
        while (true) {
            if (!tempFile.isFile()) {
                return tempFile;
            }
            String extension = "";

            int count = file.getAbsolutePath().lastIndexOf('.');
            if (i > 0) {
                extension = file.getAbsolutePath().substring(count);
            }
            String append = " (" + i + ")";
            String withoutExt = file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - extension.length());
            tempFile = new File(withoutExt + append + extension);
            i++;
        }
    }

}
