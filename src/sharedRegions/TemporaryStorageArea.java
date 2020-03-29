package sharedRegions;
import commonInfrastructures.MemException;
import commonInfrastructures.MemStack;
import entities.Bag;
import entities.PorterStates;
import entities.Porter;

/**
 *   ...
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class TemporaryStorageArea {

    /*
     *   Stack of bags on the Temporary Storage Area.
     */

    private MemStack<Bag> tmpStorageStack;

    /*
     *   General Repository of Information.
     */

    private GenReposInfo repos;

    /**
     *   Instantiation of the Temporary Storage Area.
     *
     *     @param repos general repository of information
     */

    public TemporaryStorageArea(GenReposInfo repos){
        this.repos = repos;
    }

    /**
     *  Operation of carrying a bag from the plane's hold to the temporary storage area (raised by the Porter).
     */

    public void carryItToAppropriateStore(Bag bag){

        Porter porter = (Porter) Thread.currentThread();
        assert(porter.getStat() == PorterStates.AT_THE_PLANES_HOLD);
        assert(bag != null);
        porter.setStat(PorterStates.AT_THE_STOREROOM);
        repos.updatePorterState(PorterStates.AT_THE_STOREROOM);

        try {
            tmpStorageStack.write(bag);
        } catch (MemException e) {
            e.printStackTrace();
        }
    }
}
