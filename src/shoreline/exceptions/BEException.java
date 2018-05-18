/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline.exceptions;

/**
 *
 * @author Kasper Siig
 */
public class BEException extends Exception {

    public BEException(String string) {
        super(string);
    }

    public BEException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public BEException(Throwable thrwbl) {
        super(thrwbl);
    }
    
}
