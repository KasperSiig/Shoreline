package shoreline.exceptions;

/**
 *
 * @author
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
