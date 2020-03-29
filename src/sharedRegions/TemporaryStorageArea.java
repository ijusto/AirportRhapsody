package sharedRegions;
import commonInfrastructures.MemException;
import commonInfrastructures.MemStack;
import entities.Bag;
import entities.PorterStates;
import entities.Porter;

/**
 * ...
 *
 * @author Inês Justo
 * @author Miguel Lopes
 */

public class TemporaryStorageArea {

    private MemStack<Bag> tmpStorageStack;

    
    public TemporaryStorageArea(){

    }

    /**
     *  ... (raised by the Porter).
     *
     */

    public void carryItToAppropriateStore(Bag bag){

        Porter porter = (Porter) Thread.currentThread();
        assert(porter.getStat() == PorterStates.AT_THE_PLANES_HOLD);
        porter.setStat(PorterStates.AT_THE_STOREROOM);

        notifyAll();

        try {
            tmpStorageStack.write(bag);
            wait();
        } catch (MemException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
