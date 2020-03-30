package sharedRegions;

import commonInfrastructures.MemException;
import commonInfrastructures.MemFIFO;
import entities.*;
import genclass.GenericIO;

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
    private boolean noMoreBags;

    /**
     *
     */
    private int lastBagId;

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
        this.lastBagId = -1;
        this.noMoreBags = false;
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

        while(!this.noMoreBags && this.treadmill.get(passenger.getID()).isEmpty()){
            GenericIO.writeString("\nAre all bags collected: " + this.areAllBagsCollects());
            GenericIO.writeString("\nBags in treadmill: " + this.nBagsInTreadmill);
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        try {
            this.treadmill.get(passenger.getID()).read();
            //GenericIO.writeString("\nREMOVED");
            //System.exit(-1);
            this.nBagsInTreadmill -= 1;
            GenericIO.writeString("\nBags in treadmill: " + this.nBagsInTreadmill);
            passenger.setNA(passenger.getNA() + 1);
            repos.baggageCollected(passenger.getID(), passenger);
            repos.updateStoredBaggageConveyorBeltDec();
            return true;
        } catch (MemException e) {
            return false;
        }
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
            this.lastBagId = bag.getIdOwner();
            this.nBagsInTreadmill += 1;
            GenericIO.writeString("\nBags in treadmill: " + this.nBagsInTreadmill);
            repos.updateStoredBaggageConveyorBeltInc();
            notifyAll();  // wake up Passengers in goCollectABag()
        } catch (MemException e) {
            e.printStackTrace();
        }
        if(this.areAllBagsCollects()){
            this.noMoreBags = true;
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
