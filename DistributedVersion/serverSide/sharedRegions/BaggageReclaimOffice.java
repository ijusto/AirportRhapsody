package serverSide.sharedRegions;

import clientSide.entities.Passenger;
import clientSide.entities.PassengerStates;
import clientSide.sharedRegionsStubs.GenReposInfoStub;

/**
 *   Baggage Reclaim Office.
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class BaggageReclaimOffice {

    /*
     *   General Repository of Information Stub.
     */

    private final GenReposInfoStub reposStub;

    /**
     *   Instantiation of the Baggage Collection Point.
     *
     *     @param reposStub general repository of information Stub
     */

    public BaggageReclaimOffice(GenReposInfoStub reposStub){
        this.reposStub = reposStub;
    }

    /*
     *   Operation of reporting a bag missing. (raised by the Passenger).
     */

    public synchronized void reportMissingBags(){
        Passenger passenger = (Passenger) Thread.currentThread();
        assert(passenger.getSt() == PassengerStates.AT_THE_LUGGAGE_COLLECTION_POINT);
        passenger.setSt(PassengerStates.AT_THE_BAGGAGE_RECLAIM_OFFICE);

        reposStub.updatePassSt(passenger.getPassengerID(), PassengerStates.AT_THE_BAGGAGE_RECLAIM_OFFICE.ordinal());
        reposStub.missingBagReported();

        reposStub.printLog();
    }
}
