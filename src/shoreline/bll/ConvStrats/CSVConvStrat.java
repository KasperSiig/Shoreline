package shoreline.bll.ConvStrats;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.Callable;
import javafx.application.Platform;
import org.json.JSONObject;
import shoreline.be.CSVSheet;
import shoreline.be.Config;
import shoreline.be.ConvTask;
import shoreline.bll.ThreadPool;
import shoreline.dal.Readers.InputReader;
import shoreline.dal.Writers.OutputWriter;
import shoreline.exceptions.DALException;

/**
 * Handles conversion for CSV files
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class CSVConvStrat implements ConvStrategy {

    private CSVSheet sheet;

    @Override
    public void addCallable(ConvTask task, InputReader reader, OutputWriter writer) {
        ThreadPool threadPool = ThreadPool.getInstance();
        threadPool.addToPending(task);
        // Creates the Callable, that's going to be added to the ConvTask
        Callable call = (Callable) () -> {
            sheet = (CSVSheet) reader.read(task.getSource());
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
     * Handles the overall writing of input to .json file.
     *
     * @param task
     * @param writer
     * @throws DALException
     */
    private void writeJson(ConvTask task, OutputWriter writer) throws DALException {
        // If task is started from beginning, and not being resumed, create the new file
        if (task.getProgress() == 0) {
            task.setTarget(checkForExistingFile(task.getTarget()));
            /* Input is being written to JSONObjects, and then stored in a JSONArray.
               This starts the JSONArray in the output file */
            writer.write(task.getTarget(), "[");
        }
        for (int i = task.getProgress(); i < sheet.getRowCount(); i++) {
            if (task.getStatus().getValue().equals(ConvTask.Status.Running.getValue())) {
                JSONObject jOb = createJSONObject(task);

                /* JSONObjects need to be seperated with a comma.
                   '\n' is purely for aesthetic reasons */
                String seperator = "\n,";
                if (i == 0) {
                    seperator = "\n";
                }

                writer.write(task.getTarget(), seperator + jOb.toString());
                task.setProgress(i + 1);
            } else {
                break;
            }

            // If the last object has been written, end the JSONArray
            if (i == sheet.getRowCount() - 1) {
                writer.write(task.getTarget(), "\n]");
            }
        }

    }

    /**
     * Creates JSONObject from a headers, defined in ConvTask
     *
     * @param task Task to get headers from
     * @return JSONObject created from a row in CSV file
     */
    private JSONObject createJSONObject(ConvTask task) {
        JSONObject jOb = new JSONObject();
        JSONObject planning = new JSONObject();

        task.getConfig().getPrimaryHeaders().forEach((key, value) -> {
            // Checks the key, since certain values needs to be put into its own JSONObject
            switch (key) {
                case "earliestStartDate":
                case "latestFinishDate":
                case "latestStartDate":
                case "estimatedTime":
                    planning.put(key, getSheetdata(key, task.getProgress(), task.getConfig()));
                    break;
                default:
                    jOb.put(key, getSheetdata(key, task.getProgress(), task.getConfig()));
                    break;
            }
        });
        jOb.put("createdOn", Calendar.getInstance().getTime());
        jOb.put("createdBy", "SAP");
        jOb.put("planning", planning);
        return jOb;
    }

    /**
     * Gets the value needed for creating JSONObject, from either primaryHeaders,
     * secondaryHeaders or defaultValues
     * 
     * @param header Header in CSV sheet to get data from
     * @param rowNumber Row number to get data
     * @param config Config to get HashMaps of headers from
     * @return String needed for creating JSONObject
     */
    private String getSheetdata(String header, int rowNumber, Config config) {
        
        HashMap<String, String> headers = config.getPrimaryHeaders();
        HashMap<String, String> second = config.getSecondaryHeaders();
        HashMap<String, String> defaultValues = config.getDefaultValues();
        String rtn;
        // Checks if header in HashMap, otherwise there will be nothing to get data from
        if (headers.get(header) != null) {
            rtn = sheet.getSheetData(rowNumber, headers.get(header));
            // If cell contains anything, return the value
            if (!rtn.isEmpty()) {
                return rtn;
            }
        }
        
        if (second.get(header) != null) {
            rtn = sheet.getSheetData(rowNumber, second.get(header));
            if (!rtn.isEmpty()) {
                return rtn;
            }
        }
        
        if (defaultValues.get(header) != null) {
            return defaultValues.get(header);
        }
        
        return "";
    }

    /**
     * Checks if file already exists. If it does, appends (i) to filename
     * @param file File to check for existence
     * @return File including filename not occupied
     */
    private File checkForExistingFile(File file) {
        int i = 1;
        File tempFile = file;
        /* Runs in a while(true), since it will break at some point, when an
           available file name has been found */
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
