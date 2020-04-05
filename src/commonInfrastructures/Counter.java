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
     *   Type of counter (up or down). If true, it is an up counter, otherwise it is a down counter.
     */

    private boolean inc;

    /**
     *   Object used for synchronization.
     */

    private static final Object lock = new Object();

    /**
     *   Instantiation of the Counter.
     *
     *     @param limit limit of incrementation of the counter.
     */

    public Counter(int limit, boolean inc){
        value = 0;
        this.limit = limit;
        this.inc = inc;
    }

    /**
     *   Operation of incrementing/decrementing the counter.
     *
     *    @return <li>true, if the value of the counter after the operation is the limit.</li>
     *            <li>false, otherwise.</li>
     */

    public boolean incDecCounter() {
        synchronized (lock) {
            if(this.inc) {
                value++;
            } else {
                value--;
            }
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
     *   Setter for the value of the counter.
     *    @return the value of the counter.
     */

    public void setValue(int value){
        synchronized (lock) {
            this.value = value;
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
