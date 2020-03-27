package sharedRegions;
import commonInfrastructures.Bag;
import commonInfrastructures.EntitiesStates;
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
        porter.setStat(EntitiesStates.AT_THE_LUGGAGE_BELT_CONVEYOR);

    }
}
