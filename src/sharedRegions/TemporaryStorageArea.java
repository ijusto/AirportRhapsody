package sharedRegions;
import commonInfrastructures.Bag;
import entities.PorterStates;
import entities.Porter;

/**
 * ...
 *
 * @author Inês Justo
 * @author Miguel Lopes
 */

public class TemporaryStorageArea {

    TemporaryStorageArea(){

    }

    /**
     *  ... (raised by the Porter).
     *
     */

    public void carryItToAppropriateStore(Bag bag){

        Porter porter = (Porter) Thread.currentThread();
        porter.setStat(PorterStates.AT_THE_LUGGAGE_BELT_CONVEYOR);

    }
}
