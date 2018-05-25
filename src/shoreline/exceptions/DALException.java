package shoreline.exceptions;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class DALException extends Exception {

    public DALException(String string) {
        super(string);
    }

    public DALException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public DALException(Throwable thrwbl) {
        super(thrwbl);
    }
    
}
