package shoreline.exceptions;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
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
