/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.bll.ConvStrats;

import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import org.json.JSONArray;
import org.json.JSONObject;
import shoreline.be.CSVSheet;
import shoreline.be.ConvTask;
import shoreline.bll.ThreadPool;
import shoreline.dal.Readers.InputReader;
import shoreline.dal.TitleStrats.CSVTitleStrat;
import shoreline.dal.TitleStrats.TitleImpl;
import shoreline.dal.Writers.OutputWriter;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kasper Siig
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
            // XLSX files can contain more sheets, this gets the one at index 0
            System.out.println(sheet);
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

    private void writeJson(ConvTask task, OutputWriter writer) throws DALException {
        JSONArray jAr = task.getjAr();
        writer.write(task.getTarget(), "[");
        for (int i = task.getProgress(); i < sheet.getSize(); i++) {
            if (task.getStatus().getValue().equals(ConvTask.Status.Running.getValue())) {
                JSONObject jOb = createJSONObject(i, task);
                jAr.put(jOb);
                task.setProgress(i + 1);
                task.setjAr(jAr);
                String seperator = ",";
                if (i == sheet.getSize() - 1) {
                    seperator = "\n";
                }
                writer.write(task.getTarget(), "\n" + jOb.toString() + seperator);
            }
        }
        writer.write(task.getTarget(), "]");
    }

    private JSONObject createJSONObject(int i, ConvTask task) {
        JSONObject jOb = new JSONObject();
        JSONObject planning = new JSONObject();

        task.getConfig().getHeaderMap().forEach((key, value) -> {
            switch (key) {
                case "earliestStartDate":
                case "latestFinishDate":
                case "latestStartDate":
                    planning.put(key, sheet.getSheetData(i, value));
                    break;
                case "estimatedTime":
                    planning.put("estimatedTime", sheet.getSheetData(i, value));
                    break;
                default:
                    jOb.put(key, sheet.getSheetData(i, value));
                    break;
            }
        });
        jOb.put("createdOn", Calendar.getInstance().getTime());
        jOb.put("createdBy", "SAP");
        jOb.put("planning", planning);
        return jOb;
    }

}
