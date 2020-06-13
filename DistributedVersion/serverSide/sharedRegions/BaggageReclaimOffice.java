package serverSide.sharedRegions;

import clientSide.entities.Passenger;
import clientSide.entities.PassengerStates;
import clientSide.sharedRegionsStubs.GenReposInfoStub;
import comInf.CommonProvider;
import comInf.PassengerInterface;

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

    public synchronized void reportMissingBags(int id){
        CommonProvider passenger = (CommonProvider) Thread.currentThread();
        assert(passenger.getPassStat(id) == PassengerStates.AT_THE_LUGGAGE_COLLECTION_POINT);
        passenger.setStatPass(id, PassengerStates.AT_THE_BAGGAGE_RECLAIM_OFFICE);

        reposStub.updatePassSt(id, PassengerStates.AT_THE_BAGGAGE_RECLAIM_OFFICE.ordinal());
        reposStub.missingBagReported();

        reposStub.printLog();
    }
}
