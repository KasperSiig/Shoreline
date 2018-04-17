package shoreline.exceptions;

/**
 *
 * @author
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
