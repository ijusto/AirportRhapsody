package commonInfrastructures;

/**
 * ...
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class Counter {

    /**
     *
     */

    private int value;

    /**
     *
     */

    private int limit;

    /**
     *
     */

    private static final Object lock = new Object();

    /**
     *   Instantiation of the Counter.
     *
     *     @param limit
     */

    public Counter(int limit){
        value = 0;
        this.limit = limit;
    }

    /**
     *
     *    @return
     */

    public boolean increaseCounter() {
        synchronized (lock) {
            value++;
            return value == limit;
        }
    }

    /**
     *
     *    @return
     */

    public int getValue(){
        synchronized (lock) {
            return value;
        }
    }

    /**
     *
     */

    public void reset(){
        synchronized (lock) {
            value = 0;
        }
    }
}
