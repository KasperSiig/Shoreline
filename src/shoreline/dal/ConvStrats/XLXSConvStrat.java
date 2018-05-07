package shoreline.dal.ConvStrats;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import shoreline.be.ConvTask;
import shoreline.bll.ThreadPool;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kasper Siig
 */
public class XLXSConvStrat implements ConvStrategy {

    // The Workbook containing all sheets in XLSX file
    private XSSFWorkbook wb;

    // The Sheet holding the data from the XLSX file
    private XSSFSheet sheet1;

    private FileInputStream fin;

    @Override
    public JSONArray addCallableToTask(ConvTask task) throws DALException {
        // Gets the instance of the ThreadPool object
        ThreadPool threadPool = ThreadPool.getInstance();
        threadPool.addToPending(task);

        // Creates the Callable, that's going to be added to the ConvTask
        Callable call = (Callable) () -> {
            try {
                Platform.runLater(() -> {
                    task.setStatus(ConvTask.Status.Running);
                });
                threadPool.removeFromPending(task);
                fin = new FileInputStream(task.getSource());
                wb = new XSSFWorkbook(fin);
                // XLSX files can contain more sheets, this gets the one at index 0
                sheet1 = wb.getSheetAt(0);
                writeJson(task);
                Platform.runLater(() -> {
                    task.setStatus(ConvTask.Status.Finished);
                });
                threadPool.removeFromRunning(task);

                threadPool.addToFinished(task);

            } catch (FileNotFoundException ex) {
                throw new DALException("File was not found.", ex);
            } catch (IOException ex) {
                throw new DALException("Error reading file", ex);
            }
            return null;
        };

        // Sets the Callable in the ConvTask, so it can be used in the ThreadPool later.
        task.setCallable(call);
        return null;
    }

    /**
     * Gets data from the XLSX file
     *
     * @param indexString Name of the column to get data from
     * @param rowNumber The row to get data from
     * @param task Needed to get a cellIndexMap
     * @return Data from XLSX file
     */
    private String getSheetdata(String indexString, int rowNumber, ConvTask task) {
        if (indexString.equals("")) {
            return "";
        }
        return sheet1.getRow(rowNumber).getCell(task.getCellIndexMap().get(indexString), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
    }

    /**
     * Puts all the data into a JSONArray containing JSONObjects, and then
     * writes it to a .json file.
     *
     * @param task The task containing what needs to be converted
     * @throws DALException
     */
    private void writeJson(ConvTask task) throws DALException {

        JSONArray jAr = task.getjAr();

        // Is being used to keep track of what row to pull data from
        int i = task.getProgress();
        while (sheet1.getRow(i) != null) {
            JSONObject jOb = createJSONObject(i, task);
            jAr.put(jOb);
            task.setProgress(i);
            task.setjAr(jAr);
            writeToFile(task, jAr);
            i++;
        }
    }

    private void writeToFile(ConvTask task, JSONArray jAr) throws JSONException, DALException {
        try {
            // Gets the destination to write to 
            File file = task.getTarget();
            file.createNewFile();
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(jAr.toString(4));
                fileWriter.flush();
                fileWriter.close();
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(XLXSConvStrat.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            throw new DALException("Error writing JSON File", ex);
        }
    }

    private JSONObject createJSONObject(int i, ConvTask task) throws JSONException {
        JSONObject jOb = new JSONObject();
        // Needs to be final, to be used inside lambda expression
        final int pos = i;
        JSONObject planning = new JSONObject();
        task.getMapper().forEach((key, value) -> {
            if (key.equals("name")) {
                if (getSheetdata(value, pos, task).equals("")) {
                    jOb.put("name", getSheetdata("Description2", pos, task));
                } else {
                    jOb.put(key, getSheetdata(value, pos, task));
                }
            } else if (key.equals("earliestStartDate") || key.equals("latestFinishDate") || key.equals("latestStartDate")) {
                planning.put(key, getSheetdata(value, pos, task));
            } else if (key.equals("estimatedTime")) {
                planning.put("estimatedTime", getSheetdata(value, pos, task));
            } else {
                jOb.put(key, getSheetdata(value, pos, task));
            }
        });
        jOb.put("createdOn", Calendar.getInstance().getTime());
        jOb.put("createdBy", "SAP");
        jOb.put("planning", planning);
        return jOb;
    }

    private void writeToFile(JSONArray jAr) {

    }

    private Date getDate(String sheetdata) throws DALException {
        try {
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            Date date = format.parse(sheetdata);
            return date;
        } catch (ParseException ex) {
            throw new DALException("Parsing date went wrong.", ex);
        }
    }

}
