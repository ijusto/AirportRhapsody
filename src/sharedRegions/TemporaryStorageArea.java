package sharedRegions;
import commonInfrastructures.MemException;
import commonInfrastructures.MemStack;
import entities.Bag;
import entities.PorterStates;
import entities.Porter;
import genclass.GenericIO;
import main.SimulationParameters;

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
        tmpStorageStack = new MemStack<>(new Bag [SimulationParameters.N_PASS_PER_FLIGHT * SimulationParameters.N_BAGS_PER_PASS]);     // stack instantiation
    }

    /**
     *  Operation of carrying a bag from the plane's hold to the temporary storage area (raised by the Porter).
     */

    public synchronized void carryItToAppropriateStore(Bag bag){
        GenericIO.writeString("\ncarryItToAppropriateStore");

        Porter porter = (Porter) Thread.currentThread();
        assert(porter.getStat() == PorterStates.AT_THE_PLANES_HOLD);
        assert(bag != null);
        porter.setStat(PorterStates.AT_THE_STOREROOM);
        repos.updatePorterState(PorterStates.AT_THE_STOREROOM);

        try {
            tmpStorageStack.write(bag);
            repos.updateStoredBaggageStorageRoom();
        } catch (MemException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     *   @throws MemException
     */

    public synchronized void resetTemporaryStorageArea() throws MemException {
        GenericIO.writeString("\nresetTemporaryStorageArea");
        tmpStorageStack = new MemStack<>(new Bag [SimulationParameters.N_PASS_PER_FLIGHT * SimulationParameters.N_BAGS_PER_PASS]);     // stack instantiation
    }
}
