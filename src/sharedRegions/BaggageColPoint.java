package sharedRegions;

import commonInfrastructures.Bag;
import commonInfrastructures.MemException;
import commonInfrastructures.MemFIFO;
import entities.*;

import java.util.Map;

/**
 *   ...
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
     *
     */
    private GenReposInfo repos;


    private boolean pHoldEmpty;

    /**
     *   Instantiation of the Baggage Collection Point.
     *
     *     @param repos general repository of information
     */

    public BaggageColPoint(GenReposInfo repos){
        this.repos = repos;
        this.pHoldEmpty = true;
    }


    /* ************************************************Passenger***************************************************** */

    /**
     *  ... (raised by the Passenger).
     *   The passenger is waken up by the operations carryItToAppropriateStore and tryToCollectABag of the porter when
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
        repos.updatePassSt(passenger.getPassengerID(),PassengerStates.AT_THE_LUGGAGE_COLLECTION_POINT);

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
        repos.printLog();

        do {

            //
            if(this.pHoldEmpty() && this.treadmill.get(passenger.getPassengerID()).isEmpty()) {
                return false;
            }

            if(!this.treadmill.get(passenger.getPassengerID()).isEmpty()){
                try {
                    this.treadmill.get(passenger.getPassengerID()).read();
                    passenger.setNA(passenger.getNA() + 1);

                    repos.updatesPassNA(passenger.getPassengerID(), passenger.getNA());
                    repos.pGetsABag();

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
     *  Operation of carrying a bag from the plane's hold to the baggage colletion point (raised by the Porter).
     */

    public synchronized void carryItToAppropriateStore(Bag bag){
        Porter porter = (Porter) Thread.currentThread();
        assert(porter.getStat() == PorterStates.AT_THE_PLANES_HOLD);
        assert(this.treadmill.containsKey(bag.getIdOwner()));
        porter.setStat(PorterStates.AT_THE_LUGGAGE_BELT_CONVEYOR);
        repos.updatePorterStat(PorterStates.AT_THE_LUGGAGE_BELT_CONVEYOR);

        try {
            this.treadmill.get(bag.getIdOwner()).write(bag);
            repos.incBaggageCB();
            notifyAll();  // wake up Passengers in goCollectABag()
        } catch (MemException e) {
            e.printStackTrace();
        }

        repos.printLog();
    }

    public synchronized void resetBaggageColPoint(){
        this.pHoldEmpty = true;
        this.treadmill = null;
    }

    /**
     *   Called by Porter in tryToCollectABag when it isn't successful.
     */

    public synchronized void noMoreBags() {
        // wake up Passengers in goCollectABag()
        notifyAll();
    }

    /* ************************************************* Getters ******************************************************/


    /**
     *   ...
     *    @return allBagsCollects
     */

    public synchronized boolean pHoldEmpty() {
        return pHoldEmpty;
    }

    /* ************************************************* Setters ******************************************************/

    /**
     *   ...
     *
     *    @param treadmill ...
     */

    public synchronized void setTreadmill(Map<Integer, MemFIFO<Bag>> treadmill) {
        this.treadmill = treadmill;
    }

    /**
     *
     */

    public synchronized void setPHoldEmpty(boolean pHoldEmpty){
        this.pHoldEmpty = pHoldEmpty;
    }

}
