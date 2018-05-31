package shoreline.dal.Readers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class XLSXReader implements InputReader<XSSFWorkbook> {

    @Override
    public XSSFWorkbook read(File file) throws DALException {
        XSSFWorkbook wb;
        try (FileInputStream fin = new FileInputStream(file)) {
            wb = new XSSFWorkbook(fin);
        } catch (FileNotFoundException ex) {
            throw new DALException("Could not find file to read from", ex);
        } catch (IOException ex) {
            throw new DALException("Could not read from file", ex);
        }
        return wb;
    }

}
