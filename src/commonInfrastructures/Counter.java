package commonInfrastructures;

public class Counter {
    private int value;
    private int limit;
    private static final Object lock = new Object();

    public Counter(int limit){
        value = 0;
        this.limit = limit;
    }

    public boolean increaseCounter() {
        synchronized (lock) {
            value++;
            return value == limit;
        }
    }

    public int getValue(){
        return value;
    }
}
