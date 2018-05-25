package shoreline.dal.ObjectPool;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import shoreline.exceptions.DALException;

/**
 *
 * @author Kenneth R. Pedersen, Mads H. Thyssen & Kasper Siig
 * @param <T> The type of Object to be pooled
 */
public abstract class ObjectPool<T> {
    private long expirationTime;
    
    // Contains all the unavailable Objects
    private ConcurrentHashMap<T, Long> locked;
    
    // Contains all the available Objects
    private ConcurrentHashMap<T, Long> unlocked;
    
    protected ObjectPool() {
        expirationTime = 30000; // 30 Seconds
        locked = new ConcurrentHashMap();
        unlocked = new ConcurrentHashMap();
    }
    
    /**
     * Creates a new Object
     * 
     * @return The created Object
     * @throws DALException 
     */
    protected abstract T create() throws DALException;

    /**
     * Validates whether the Object is open for use or not
     * 
     * @param o The Objetc to check
     * @return Whether it is available or not
     * @throws DALException 
     */
    public abstract boolean validate(T o) throws DALException;

    /**
     * Invalidates the Object
     * 
     * @param o Object to be invalidated
     * @throws DALException 
     */
    public abstract void expire(T o) throws DALException;
    
    /**
     * Returns an Object, either an available one, or creates a new one.
     * 
     * @return Available Object
     * @throws DALException 
     */
    public synchronized T checkOut() throws DALException {
        long now = System.currentTimeMillis();
        T t;
        // Checks if there are any available Objects.
        if (unlocked.size() > 0) {
            Enumeration<T> keys = unlocked.keys();
            while (keys.hasMoreElements()) {
                t = keys.nextElement();
                // Checks if the Object has expired
                if ((now - unlocked.get(t)) > expirationTime) {
                    unlocked.remove(t);
                    expire(t);
                } else if (validate(t)) {
                    // Makes the Object unavailable, and returns it
                    unlocked.remove(t);
                    locked.put(t, now);
                    return t;
                } else {
                    // Failsafe
                    unlocked.remove(t);
                    expire(t);
                }
            }
        }
        // If no Object available, create new one and return
        t = create();
        locked.put(t, now);
        return t;
    }
    
    /**
     * Sets the Object as available
     * 
     * @param t 
     */
    public synchronized void checkIn(T t) {
        locked.remove(t);
        unlocked.put(t, System.currentTimeMillis());
    }
}
