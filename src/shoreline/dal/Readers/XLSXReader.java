/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.dal.Readers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kasper Siig
 */
public class XLSXReader implements InputReader<XSSFWorkbook> {

    @Override
    public XSSFWorkbook read(File file) throws DALException {
        XSSFWorkbook wb;
        FileInputStream fin;
        try {
            fin = new FileInputStream(file);
            wb = new XSSFWorkbook(fin);
            fin.close();
        } catch (FileNotFoundException ex) {
            throw new DALException("Could not find file to read from", ex);
        } catch (IOException ex) {
            throw new DALException("Could not read from file", ex);
        }
        
        return wb;
    }

}
