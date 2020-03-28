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

    /* TODO:
        add CAM (memoria associativa para guardar as malas - tapete rolante
    *
    * */

    private Map<Integer, MemFIFO<Bag>> treadmill;

    /*
     *
     */
    private GenReposInfo repos;

    /*
     *
     */

    public BaggageColPoint(GenReposInfo repos){
        this.repos = repos;
    }

    /**
     *  ... (raised by the Passenger).
     *
     */

    public void goCollectABag(){

        Passenger passenger = (Passenger) Thread.currentThread();
        passenger.setSt(PassengerStates.AT_THE_LUGGAGE_COLLECTION_POINT);

    }

    /**
     *  ... (raised by the Passenger).
     *
     */

    public void reportMissingBags(){

        Passenger passenger = (Passenger) Thread.currentThread();
        passenger.setSt(PassengerStates.AT_THE_BAGGAGE_RECLAIM_OFFICE);

    }

    /**
     *  ... (raised by the Passenger).
     *
     */

    public void goHome(){

        Passenger passenger = (Passenger) Thread.currentThread();
        passenger.setSt(PassengerStates.EXITING_THE_ARRIVAL_TERMINAL);

    }

    /**
     *  ... (raised by the Porter).
     *
     */
    public void carryItToAppropriateStore(Bag bag){

        Porter porter = (Porter) Thread.currentThread();
        porter.setStat(PorterStates.AT_THE_LUGGAGE_BELT_CONVEYOR);

    }

    public Map<Integer, MemFIFO<Bag>> getTreadmill() {
        return treadmill;
    }

    public void setTreadmill(Map<Integer, MemFIFO<Bag>> treadmill) {
        this.treadmill = treadmill;
    }
}
