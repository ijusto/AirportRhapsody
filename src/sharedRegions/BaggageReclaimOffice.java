package sharedRegions;

import entities.Passenger;
import entities.PassengerStates;

/**
 * ...
 *
 * @author Inês Justo
 * @author Miguel Lopes
 */

public class BaggageReclaimOffice {

    /*
     *
     */
    private GenReposInfo repos;

    /*
     *
     */

    public BaggageReclaimOffice(GenReposInfo repos){
        this.repos = repos;
    }

    public void reportMissingBags(){

        Passenger passenger = (Passenger) Thread.currentThread();
        assert(passenger.getSt() == PassengerStates.AT_THE_LUGGAGE_COLLECTION_POINT);
        passenger.setSt(PassengerStates.AT_THE_BAGGAGE_RECLAIM_OFFICE);
        repos.updatePassengerState(passenger.getID(), PassengerStates.AT_THE_BAGGAGE_RECLAIM_OFFICE);

    }
}
