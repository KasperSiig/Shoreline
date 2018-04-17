package shoreline.exceptions;

/**
 *
 * @author
 */
public class BLLException extends Exception {

    public BLLException(String string) {
        super(string);
    }

    public BLLException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public BLLException(Throwable thrwbl) {
        super(thrwbl);
    }
    
}
