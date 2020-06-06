package serverSide.sharedRegions;
import comInf.MemException;
import comInf.MemStack;
import comInf.Bag;
import clientSide.PorterStates;
import clientSide.entities.Porter;
import clientSide.SimulPar;

/**
 *   Temporary Storage Area.
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

    public TemporaryStorageArea(/*GenReposInfo repos*/) throws MemException {
        this.repos = repos;
        // stack of the store room instantiation
        tmpStorageStack = new MemStack<>(new Bag [SimulPar.N_PASS_PER_FLIGHT * SimulPar.N_BAGS_PER_PASS]);
    }

    /**
     *   Operation of carrying a bag from the plane's hold to the temporary storage area (raised by the Porter).
     */

    public synchronized void carryItToAppropriateStore(Bag bag){
        Porter porter = (Porter) Thread.currentThread();
        assert porter.getStat() == PorterStates.AT_THE_PLANES_HOLD;
        assert bag != null;
        porter.setStat(PorterStates.AT_THE_STOREROOM);
        repos.updatePorterStat(PorterStates.AT_THE_STOREROOM);

        try {
            tmpStorageStack.write(bag);
            repos.saveBagInSR();
        } catch (MemException e) {
            e.printStackTrace();
        }

        repos.printLog();
    }

    /**
     *   Resets the stack of bags when all passengers from a flight leave the airport.
     */

    public synchronized void resetTemporaryStorageArea() {
        // stack of the store room instantiation
        try {
            tmpStorageStack = new MemStack<>(new Bag [SimulPar.N_PASS_PER_FLIGHT * SimulPar.N_BAGS_PER_PASS]);
        } catch (MemException e) {
            e.printStackTrace();
        }
    }
}
