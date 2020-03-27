package sharedRegions;
import commonInfrastructures.Bag;
import commonInfrastructures.EntitiesStates;
import entities.BusDriver;
import entities.Passenger;
import entities.Porter;

/**
 * ...
 *
 * @author Inês Justo
 * @author Miguel Lopes
 */

public class BaggageCollectionPoint {

    /**
     *  ... (raised by the Passenger).
     *
     */

    public void goCollectABag(){

        Passenger passenger = (Passenger) Thread.currentThread();
        passenger.setSt(EntitiesStates.AT_THE_LUGGAGE_COLLECTION_POINT);

    }

    /**
     *  ... (raised by the Passenger).
     *
     */

    public void reportMissingBags(){

        Passenger passenger = (Passenger) Thread.currentThread();
        passenger.setSt(EntitiesStates.AT_THE_BAGGAGE_RECLAIM_OFFICE);

    }

    /**
     *  ... (raised by the Passenger).
     *
     */

    public void goHome(){

        Passenger passenger = (Passenger) Thread.currentThread();
        passenger.setSt(EntitiesStates.EXITING_THE_ARRIVAL_TERMINAL);

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
