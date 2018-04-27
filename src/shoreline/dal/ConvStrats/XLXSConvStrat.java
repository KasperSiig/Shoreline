/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import shoreline.be.ConvTask;
import shoreline.bll.ThreadPool;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kasper Siig
 */
public class XLXSConvStrat implements ConvStrategy {

    private XSSFSheet sheet1;
    private FileInputStream fin;
    private XSSFWorkbook wb;

    @Override
    public JSONArray convertAndWrite(ConvTask task) throws DALException {
//        System.out.println("start convert");
        ThreadPool threadPool = ThreadPool.getInstance();
        threadPool.addToPending(task);
        
        Callable call = (Callable) () -> {
            try {
                threadPool.removeFromPending(task);
                fin = new FileInputStream(task.getSource());
                wb = new XSSFWorkbook(fin);
                sheet1 = wb.getSheetAt(0);
                writeJson(task);
                threadPool.removeFromRunning(task);
                threadPool.addToFinished(task);
            } catch (FileNotFoundException ex) {
                throw new DALException("File was not found.", ex);
            } catch (IOException ex) {
                throw new DALException("Something went wrong.", ex);
            }
            return null;
        };
        task.setTask(call);
//        System.out.println(task.getTask());
        return null;
    }

    private String getSheetdata(String indexString, int rowNumber, ConvTask task) {
        if (indexString.equals("")) {
            return "";
        }
//        System.out.println(indexString);
//        System.out.println(sheet1.getRow(rowNumber).getCell(task.getCellIndexMap().get(indexString), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
        return sheet1.getRow(rowNumber).getCell(task.getCellIndexMap().get(indexString), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
    }

    private void writeJson(ConvTask task) throws DALException {

        JSONArray jAr = new JSONArray();

        int i = 1;
        while (sheet1.getRow(i) != null) {
            JSONObject jOb = new JSONObject();
            final int pos = i;
//            System.out.println(pos);
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

            jAr.put(jOb);
            i++;
        }

//        System.out.println(i);
//        System.out.println(jAr.toString(4));

        try {

            // Writing to a file  
            File file = task.getTarget();
//            System.out.println(file.getAbsoluteFile());
            file.createNewFile();
            try (
                    FileWriter fileWriter = new FileWriter(file)) {
//                System.out.println("Writing JSON object to file");
//                System.out.println("-----------------------");

                fileWriter.write(jAr.toString(4));
                fileWriter.flush();
                fileWriter.close();
            }

        } catch (IOException ex) {
            throw new DALException("Error writing JSON File", ex);
        }
    }

    private Date getDate(String sheetdata) throws DALException {
        try {
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
//            System.out.println(sheetdata);
            Date date = format.parse(sheetdata);
            return date;
        } catch (ParseException ex) {
            throw new DALException("Parsing date went wrong.", ex);
        }
    }

}
