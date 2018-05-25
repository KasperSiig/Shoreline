package shoreline.dal.Writers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import shoreline.exceptions.DALException;

/**
 * Writes a given string, to a given output
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class StringToFile implements OutputWriter<String> {

    @Override
    public void write(File file, String output) throws DALException {

        try (FileWriter fileWriter = new FileWriter(file, true)) {
            if (!file.isFile()) {
                file.createNewFile();
            }
            fileWriter.write(output);
            fileWriter.flush();
            fileWriter.close();

        } catch (IOException ex) {
            throw new DALException("There was a problem writing output to file", ex);
        }
    }

}
