/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.dal.Readers;

import java.io.File;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kasper Siig
 * @param <T>
 */
public interface InputReader<T> {
    /**
     * Reads from a given file
     * 
     * @param file
     * @return
     * @throws DALException 
     */
    public T read(File file) throws DALException;
}
