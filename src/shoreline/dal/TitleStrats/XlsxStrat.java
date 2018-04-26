/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.bll.TitleStrats;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kasper Siig
 */
public class XlsxStrat implements TitleStrategy {

    HashMap<String, Integer> cellIndexMap = new HashMap();
    FileInputStream fin;
    XSSFSheet sheet1;
    
    @Override
    public HashMap<String, Integer> getTitles(File file) throws DALException {
        try {
            fin = new FileInputStream(file);
            XSSFWorkbook wb = new XSSFWorkbook(fin);
            sheet1 = wb.getSheetAt(0);
            
            int i = 0;
            while (sheet1.getRow(0).getCell(i) != null) {
            int j = 0;
            String tempName = sheet1.getRow(0).getCell(i).getStringCellValue();
            if (!cellIndexMap.containsKey(tempName)) {
                cellIndexMap.put(tempName, i);
            } else {
                boolean done = false;
                while (!done) {
                    if (!cellIndexMap.containsKey(tempName + ++j)) {
                        cellIndexMap.put(tempName + j, i);
                        done = true;
                    }
                }
            }
            i++;
        }
            
            
            
            return cellIndexMap;
        } catch (FileNotFoundException ex) {
            throw new DALException(ex);
        } catch (IOException ex) {
            throw new DALException(ex);
        }
    }
    
}
