package sharedRegions;

import entities.Passenger;
import entities.PassengerStates;

/**
 *   ...
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class BaggageReclaimOffice {

    /*
     *   General Repository of Information.
     */

    private GenReposInfo repos;

    /**
     *   Instantiation of the Baggage Collection Point.
     *
     *     @param repos general repository of information
     */

    public BaggageReclaimOffice(GenReposInfo repos){
        this.repos = repos;
    }

    /*
     *   ... (raised by the Passenger).
     */

    public synchronized void reportMissingBags(){
        System.out.print("\nreportMissingBags");

        Passenger passenger = (Passenger) Thread.currentThread();
        assert(passenger.getSt() == PassengerStates.AT_THE_LUGGAGE_COLLECTION_POINT);
        passenger.setSt(PassengerStates.AT_THE_BAGGAGE_RECLAIM_OFFICE);
        repos.updatePassSt(passenger.getPassengerID(), PassengerStates.AT_THE_BAGGAGE_RECLAIM_OFFICE);
        repos.missingBagReported();

    }
}
