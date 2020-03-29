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

    /*
     *
     */
    private GenReposInfo repos;


    private boolean collected;

    /**
     *   Instantiation of the Baggage Collection Point.
     *
     *     @param repos general repository of information
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

    public synchronized boolean goCollectABag(){
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

        Passenger passenger = (Passenger) Thread.currentThread();
        assert(passenger.getSt() == PassengerStates.AT_THE_DISEMBARKING_ZONE);
        passenger.setSt(PassengerStates.AT_THE_LUGGAGE_COLLECTION_POINT);

        boolean bagsForPass = false;

        if(this.treadmill.containsKey(passenger.getID())){
            if(!this.treadmill.get(passenger.getID()).isEmpty()){
                bagsForPass = true;
            }
        }

        while(!this.isCollected() && !bagsForPass) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(passenger.getNA() != passenger.getNR()) {
            if (this.treadmill.containsKey(passenger.getID())) {
                passenger.setNA(passenger.getNA() + 1);
                try {
                    this.treadmill.get(passenger.getID()).read();
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

    public void carryItToAppropriateStore(Bag bag){

        Porter porter = (Porter) Thread.currentThread();
        assert(porter.getStat() == PorterStates.AT_THE_PLANES_HOLD);
        porter.setStat(PorterStates.AT_THE_LUGGAGE_BELT_CONVEYOR);
        repos.updatePorterState(PorterStates.AT_THE_LUGGAGE_BELT_CONVEYOR);

        /* TODO: turn collected to true on carryItTo... when no more bags at phold*/

        notifyAll();  // wake up Passengers in goCollectABag()

        try {
            this.treadmill.get(bag.getIdOwner()).write(bag);
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

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

}
