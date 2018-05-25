package shoreline.dal.TitleStrats;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class XLSXTitleStrat implements TitleStrategy {


    @Override
    public HashMap<String, Integer> getTitles(File file) throws DALException {
        HashMap<String, Integer> titleIndexMap = new HashMap();
        try (FileInputStream fin = new FileInputStream(file)){
            
            XSSFWorkbook wb = new XSSFWorkbook(fin);
            XSSFSheet sheet1 = wb.getSheetAt(0);
            
            // Puts the headers into headerMap, appending an int if already exists
            int i = 0;
            while (sheet1.getRow(0).getCell(i) != null) {
                String tempName = sheet1.getRow(0).getCell(i).getStringCellValue();
                if (!titleIndexMap.containsKey(tempName)) {
                    titleIndexMap.put(tempName, i);
                } else {
                    int j = 0;
                    boolean done = false;
                    while (!done) {
                        if (!titleIndexMap.containsKey(tempName + ++j)) {
                            titleIndexMap.put(tempName + j, i);
                            done = true;
                        }
                    }
                }
                i++;
            }
            return titleIndexMap;
        } catch (FileNotFoundException ex) {
            throw new DALException(ex);
        } catch (IOException ex) {
            throw new DALException(ex);
        }
    }

}
