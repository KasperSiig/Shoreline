package shoreline.exceptions;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 */
public class GUIException extends Exception {

    public GUIException(String string) {
        super(string);
    }

    public GUIException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public GUIException(Throwable thrwbl) {
        super(thrwbl);
    }

}
