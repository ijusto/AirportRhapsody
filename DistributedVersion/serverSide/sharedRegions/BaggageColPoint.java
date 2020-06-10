package serverSide.sharedRegions;

import comInf.SimulPar;
import clientSide.sharedRegionsStubs.GenReposInfoStub;
import comInf.Bag;
import comInf.MemException;
import comInf.MemFIFO;
import clientSide.entities.*;

import java.util.Map;

/**
 *   Baggage Collection Point.
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class BaggageColPoint {

    /*
     *   Map to store
     */

    private Map<Integer, MemFIFO<Bag>> treadmill;

    /*
     *   General Repository of Information Stub.
     */

    private GenReposInfoStub reposStub;

    /**
     *   Signaling the empty state of the plane's hold.
     */

    private boolean pHoldEmpty;
    

    /**
     *   Instantiation of the Baggage Collection Point.
     *
     *     @param reposStub general repository of information Stub
     */

    public BaggageColPoint(GenReposInfoStub reposStub){
        this.reposStub = reposStub;
        this.pHoldEmpty = true;
    }


    /* ************************************************Passenger***************************************************** */

    /**
     *   Operation of trying to collect a bag (raised by the Passenger).
     *   The passenger is waken up by the operations carryItToAppropriateStore and noMoreBagsToCollect of the porter when
     *   he places on the conveyor belt a bag she owns, the former, or when he signals that there are no more pieces
     *   of luggage in the plane hold, the latter, and makes a transition when either she has in her possession all the
     *   bags she owns, or was signaled that there are no more bags in the plane hold
     *
     */

    public synchronized boolean goCollectABag(){

        Passenger passenger = (Passenger) Thread.currentThread();
        assert(passenger.getSt() == PassengerStates.AT_THE_DISEMBARKING_ZONE);
        passenger.setSt(PassengerStates.AT_THE_LUGGAGE_COLLECTION_POINT);

        // update logger
        reposStub.updatePassSt(passenger.getPassengerID(),PassengerStates.AT_THE_LUGGAGE_COLLECTION_POINT.ordinal());

        /*
          Blocked Entity: Passenger
          Freeing Entity: Porter

          Freeing Method: carryItToAppropriateStore()
          Freeing Condition: porter bring their bag
          Blocked Entity Reactions: -> if all bags collected: goHome() else goCollectABag()

          Freeing Method: noMoreBagsToCollect()
          Freeing Condition: no more pieces of luggage
          Blocked Entity Reaction: reportMissingBags()
        */
        reposStub.printLog();

        do {

            if(this.pHoldEmpty() && this.treadmill.get(passenger.getPassengerID()).isEmpty()) {
                return false;
            }

            if(!this.treadmill.get(passenger.getPassengerID()).isEmpty()){
                try {
                    this.treadmill.get(passenger.getPassengerID()).read();
                    passenger.setNA(passenger.getNA() + 1);

                    reposStub.updatesPassNA(passenger.getPassengerID(), passenger.getNA());
                    reposStub.pGetsABag();

                    return true;
                } catch (MemException e) {
                    e.printStackTrace();
                }
            } else if(this.pHoldEmpty()){
                return false;
            }

            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } while(true);

    }


    /* **************************************************Porter****************************************************** */

    /**
     *  Operation of carrying a bag from the plane's hold to the baggage collection point (raised by the Porter).
     */

    public synchronized void carryItToAppropriateStore(Bag bag){
        Porter porter = (Porter) Thread.currentThread();
        assert(porter.getStat() == PorterStates.AT_THE_PLANES_HOLD);
        assert(this.treadmill.containsKey(bag.getIdOwner()));
        porter.setStat(PorterStates.AT_THE_LUGGAGE_BELT_CONVEYOR);
        reposStub.updatePorterStat(PorterStates.AT_THE_LUGGAGE_BELT_CONVEYOR.ordinal());

        try {
            this.treadmill.get(bag.getIdOwner()).write(bag);
            reposStub.incBaggageCB();
            notifyAll();  // wake up Passengers in goCollectABag()
        } catch (MemException e) {
            e.printStackTrace();
        }

        reposStub.printLog();
    }

    /**
     *
     */

    public synchronized void resetBaggageColPoint(){
        this.pHoldEmpty = true;
        this.treadmill = null;
    }

    /**
     *   Called by Porter in noMoreBagsToCollect.
     */

    public synchronized void noMoreBags() {
        // wake up Passengers in goCollectABag()
        notifyAll();
    }

    /* ************************************************* Getters ******************************************************/


    /**
     *   Signaling the empty state of the plane's hold.
     *
     *    @return
     */

    public synchronized boolean pHoldEmpty() {
        return pHoldEmpty;
    }

    /* ************************************************* Setters ******************************************************/

    /**
     *   ...
     *
     *    @param nBagsPerPass ...
     */

    public synchronized void setTreadmill(int[] nBagsPerPass) {
        try {
            for(int nPass = 0; nPass < SimulPar.N_PASS_PER_FLIGHT; nPass++){
                MemFIFO<Bag> bagPassFIFO =  new MemFIFO<>(new Bag [nBagsPerPass[nPass]]);        // FIFO instantiation
                treadmill.put(nPass, bagPassFIFO);
            }

        } catch (MemException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */

    public synchronized void setPHoldEmpty(boolean pHoldEmpty){
        this.pHoldEmpty = pHoldEmpty;
    }

}
