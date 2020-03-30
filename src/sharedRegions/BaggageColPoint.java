package sharedRegions;

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

    /**
     *
     */
    private int passIdBagCollected;

    /**
     *
     */
    private int nBagsInTreadmill;

    /*
     *
     */
    private GenReposInfo repos;


    private boolean allBagsCollects;

    /**
     *   Instantiation of the Baggage Collection Point.
     *
     *     @param repos general repository of information
     */

    public BaggageColPoint(GenReposInfo repos){
        this.repos = repos;
        this.nBagsInTreadmill = 0;
        this.allBagsCollects = false;
        this.passIdBagCollected = -1;
    }


    /* ************************************************Passenger***************************************************** */

    /**
     *  ... (raised by the Passenger).
     *
     */

    public synchronized boolean goCollectABag(){
        Passenger passenger = (Passenger) Thread.currentThread();
        assert(passenger.getSt() == PassengerStates.AT_THE_DISEMBARKING_ZONE);
        passenger.setSt(PassengerStates.AT_THE_LUGGAGE_COLLECTION_POINT);
        repos.updatePassengerState(passenger.getID(),PassengerStates.AT_THE_LUGGAGE_COLLECTION_POINT);

        /*
          Blocked Entity: Passenger
          Freeing Entity: Porter

          Freeing Method: carryItToAppropriateStore()
          Freeing Condition: porter bring their bag
          Blocked Entity Reactions: -> if all bags collected: goHome() else goCollectABag()

          Freeing Method: tryToCollectABag()
          Freeing Condition: no more pieces of luggage
          Blocked Entity Reaction: reportMissingBags()
        */

        boolean carryWake = false;
        boolean tryCollWake = false;

        while(!carryWake && !tryCollWake){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(passenger.getId() == this.passIdBagCollected){
                carryWake = true;
            }
            if(this.areAllBagsCollects() && this.nBagsInTreadmill == 0){
                tryCollWake = true;
            }
        }
        this.passIdBagCollected = -1;
        if(passenger.getNA() != passenger.getNR()) {
            if (this.treadmill.containsKey(passenger.getID())) {
                try {
                    this.treadmill.get(passenger.getID()).read();
                    passenger.setNA(passenger.getNA() + 1);
                    repos.baggageCollected(passenger.getID(), passenger);
                    repos.updateStoredBaggageConveyorBeltDec();
                    return true;
                } catch (MemException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }


    /* **************************************************Porter****************************************************** */

    /**
     *  Operation of carrying a bag from the plane's hold to the baggage colletion point (raised by the Porter).
     */

    public synchronized void carryItToAppropriateStore(Bag bag){

        Porter porter = (Porter) Thread.currentThread();
        assert(porter.getStat() == PorterStates.AT_THE_PLANES_HOLD);
        porter.setStat(PorterStates.AT_THE_LUGGAGE_BELT_CONVEYOR);
        repos.updatePorterState(PorterStates.AT_THE_LUGGAGE_BELT_CONVEYOR);

        try {
            this.treadmill.get(bag.getIdOwner()).write(bag);
            this.nBagsInTreadmill += 1;
            this.passIdBagCollected = bag.getIdOwner();
            notifyAll();  // wake up Passengers in goCollectABag()
            repos.updateStoredBaggageConveyorBeltInc();

        } catch (MemException e) {
            e.printStackTrace();
        }
    }

    /* ******************************************** Getters and Setters ***********************************************/

    /*
     *
     */

    public Map<Integer, MemFIFO<Bag>> getTreadmill() {
        return treadmill;
    }

    /**
     *   ...
     *
     *   @param treadmill ...
     */

    public void setTreadmill(Map<Integer, MemFIFO<Bag>> treadmill) {
        this.treadmill = treadmill;
    }

    /*
     *
     */

    public boolean areAllBagsCollects() {
        return allBagsCollects;
    }

    public void setAllBagsCollected(boolean allBagsCollects) {
        this.allBagsCollects = allBagsCollects;
    }

}
