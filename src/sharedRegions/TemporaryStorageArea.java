package sharedRegions;
import commonInfrastructures.MemException;
import commonInfrastructures.MemStack;
import entities.Bag;
import entities.PorterStates;
import entities.Porter;
import main.SimulPar;

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

    public TemporaryStorageArea(GenReposInfo repos) throws MemException {
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

        // log passenger state change
        repos.updatePorterStat(PorterStates.AT_THE_STOREROOM);

        try {
            tmpStorageStack.write(bag);

            // log
            repos.saveBagInSR();
        } catch (MemException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     *    @throws MemException
     */

    public synchronized void resetTemporaryStorageArea() throws MemException {
        // stack of the store room instantiation
        tmpStorageStack = new MemStack<>(new Bag [SimulPar.N_PASS_PER_FLIGHT * SimulPar.N_BAGS_PER_PASS]);
    }
}
