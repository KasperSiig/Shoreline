/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.dal.Writers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import shoreline.exceptions.DALException;

/**
 * Writes a given string, to a given output
 * 
 * @author Kasper Siig
 */
public class StringToFile implements OutputWriter<String> {
    
    @Override
    public void write(File file, String output) throws DALException {

        try (FileWriter fileWriter = new FileWriter(file)) {
            file.createNewFile();
            fileWriter.write(output);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException ex) {
            throw new DALException("There was a problem writing output to file", ex);
        }
    }

}
