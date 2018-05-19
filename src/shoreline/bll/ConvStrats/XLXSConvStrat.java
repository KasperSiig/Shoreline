package shoreline.bll.ConvStrats;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Callable;
import javafx.application.Platform;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import shoreline.be.ConvTask;
import shoreline.bll.ThreadPool;
import shoreline.dal.Readers.InputReader;
import shoreline.dal.TitleStrats.TitleImpl;
import shoreline.dal.TitleStrats.XLSXTitleStrat;
import shoreline.dal.Writers.OutputWriter;
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

    @Override
    public void addCallable(ConvTask task, InputReader reader, OutputWriter writer) {
        // Gets the instance of the ThreadPool object
        ThreadPool threadPool = ThreadPool.getInstance();
        threadPool.addToPending(task);

        // Creates the Callable, that's going to be added to the ConvTask
        Callable call = (Callable) () -> {
            TitleImpl impl = new TitleImpl(new XLSXTitleStrat());
            HashMap<String, Integer> cellIndexMap = impl.getTitles(task.getSource());
            task.getConfig().setCellIndexMap(cellIndexMap);
            wb = (XSSFWorkbook) reader.read(task.getSource());
            // XLSX files can contain more sheets, this gets the one at index 0
            sheet1 = wb.getSheetAt(0);
            writeJson(task, writer);
            if (task.getStatus().getValue().equals(ConvTask.Status.Running.getValue())) {
                Platform.runLater(() -> {
                    task.setStatus(ConvTask.Status.Finished);
                });
                threadPool.removeFromRunning(task);
                
                threadPool.addToFinished(task);
            }
            return null;
        };

        // Sets the Callable in the ConvTask, so it can be used in the ThreadPool later.
        task.setCallable(call);
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
        return sheet1.getRow(rowNumber).getCell(task.getConfig().getCellIndexMap().get(indexString), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
    }

    /**
     * Puts all the data into a JSONArray containing JSONObjects, and then
     * writes it to a .json file.
     *
     * @param task The task containing what needs to be converted
     * @throws DALException
     */
    private void writeJson(ConvTask task, OutputWriter writer) throws DALException {

        JSONArray jAr = task.getjAr();

        // Is being used to keep track of what row to pull data from
        int i = task.getProgress() + 1;
        while (sheet1.getRow(i) != null
                && task.getStatus().getValue().equals(ConvTask.Status.Running.getValue())) {

            JSONObject jOb = createJSONObject(i, task);
            jAr.put(jOb);
            task.setProgress(i);
            task.setjAr(jAr);
            writer.write(task.getTarget(), jAr.toString(4));
            i++;
        }
    }

    private JSONObject createJSONObject(int i, ConvTask task) {
        JSONObject jOb = new JSONObject();
        JSONObject planning = new JSONObject();

        task.getConfig().getHeaderMap().forEach((key, value) -> {
            switch (key) {
                case "earliestStartDate":
                case "latestFinishDate":
                case "latestStartDate":
                    planning.put(key, getSheetdata(value, i, task));
                    break;
                case "estimatedTime":
                    planning.put("estimatedTime", getSheetdata(value, i, task));
                    break;
                default:
                    jOb.put(key, getSheetdata(value, i, task));
                    break;
            }
        });
        jOb.put("createdOn", Calendar.getInstance().getTime());
        jOb.put("createdBy", "SAP");
        jOb.put("planning", planning);
        return jOb;
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
