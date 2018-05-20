/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.dal.Writers;

import java.io.File;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kasper Siig
 * @param <T>
 */
public interface OutputWriter<T> {
    /**
     * Writes given file to given output.
     * 
     * @param file File to be written to
     * @param output The output to be written.
     * @throws DALException 
     */
    public void write(File file, T output) throws DALException;
}
