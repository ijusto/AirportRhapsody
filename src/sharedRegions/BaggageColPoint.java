package sharedRegions;

import commonInfrastructures.MemFIFO;
import entities.*;

import java.util.Map;

/**
 * ...
 *
 * @author Inês Justo
 * @author Miguel Lopes
 */

public class BaggageColPoint {

    /*
     *   Map to store
     */

    private Map<Integer, MemFIFO<Bag>> treadmill;

    /*
     *
     */
    private GenReposInfo repos;


    private boolean collected;

    /*
     *
     */

    public BaggageColPoint(GenReposInfo repos){
        this.repos = repos;

        this.collected = false;
    }


    /* ************************************************Passenger***************************************************** */

    /**
     *  ... (raised by the Passenger).
     *
     */

    public void goCollectABag(){

        Passenger passenger = (Passenger) Thread.currentThread();
        assert(passenger.getSt() == PassengerStates.AT_THE_LUGGAGE_COLLECTION_POINT);

    }

    /**
     *  ... (raised by the Passenger).
     *
     */

    public void reportMissingBags(){

        Passenger passenger = (Passenger) Thread.currentThread();
        assert(passenger.getSt() == PassengerStates.AT_THE_LUGGAGE_COLLECTION_POINT);
        passenger.setSt(PassengerStates.AT_THE_BAGGAGE_RECLAIM_OFFICE);

    }


    /* **************************************************Porter****************************************************** */

    /**
     *  ... (raised by the Porter).
     *
     */
    public void carryItToAppropriateStore(Bag bag){

        Porter porter = (Porter) Thread.currentThread();
        porter.setStat(PorterStates.AT_THE_LUGGAGE_BELT_CONVEYOR);
        /* TODO: turn collected to true on carryItTo... when no more bags at phold*/
    }

    public Map<Integer, MemFIFO<Bag>> getTreadmill() {
        return treadmill;
    }

    public void setTreadmill(Map<Integer, MemFIFO<Bag>> treadmill) {
        this.treadmill = treadmill;
    }

    /*
     *
     */

    public boolean isCollected() {
        return collected;
    }

}
