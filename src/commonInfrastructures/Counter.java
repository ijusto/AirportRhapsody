package commonInfrastructures;

/**
 *   Counter.
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class Counter {

    /**
     *   Value of the counter.
     */

    private int value;

    /**
     *   Limit of incrementation of the counter.
     */

    private int limit;

    /**
     *   Object used for synchronization.
     */

    private static final Object lock = new Object();

    /**
     *   Instantiation of the Counter.
     *
     *     @param limit limit of incrementation of the counter.
     */

    public Counter(int limit){
        value = 0;
        this.limit = limit;
    }

    /**
     *   Operation of incrementing the counter.
     *
     *    @return <li>true, if the value of the counter after incrementing is the limit.</li>
     *            <li>false, otherwise.</li>
     */

    public boolean increaseCounter() {
        synchronized (lock) {
            value++;
            return value == limit;
        }
    }

    /**
     *   Getter for the value of the counter.
     *    @return the value of the counter.
     */

    public int getValue(){
        synchronized (lock) {
            return value;
        }
    }

    /**
     *   Sets the value of the counter to zero.
     */

    public void reset(){
        synchronized (lock) { // Locks on the private Object
            value = 0;
        }
    }
}
