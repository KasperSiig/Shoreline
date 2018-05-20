/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.dal.TitleStrats;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kasper Siig
 */
public class XLSXTitleStrat implements TitleStrategy {

    private HashMap<String, Integer> cellIndexMap;
    private FileInputStream fin;
    private XSSFWorkbook wb;
    private XSSFSheet sheet1;

    public XLSXTitleStrat() {
        this.cellIndexMap = new HashMap();
    }

    @Override
    public HashMap<String, Integer> getTitles(File file) throws DALException {
        try {
            fin = new FileInputStream(file);
            wb = new XSSFWorkbook(fin);
            sheet1 = wb.getSheetAt(0);

            int i = 0;
            while (sheet1.getRow(0).getCell(i) != null) {
                String tempName = sheet1.getRow(0).getCell(i).getStringCellValue();
                if (!cellIndexMap.containsKey(tempName)) {
                    cellIndexMap.put(tempName, i);
                } else {
                    int j = 0;
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
