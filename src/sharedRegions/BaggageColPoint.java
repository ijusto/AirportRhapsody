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

    private boolean porterAwake;

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
        this.porterAwake = false;
    }


    /* ************************************************Passenger***************************************************** */

    /**
     *  ... (raised by the Passenger).
     *
     */

    public synchronized boolean goCollectABag(){
        GenericIO.writeString("\ngoCollectABag");

        Passenger passenger = (Passenger) Thread.currentThread();
        assert(passenger.getSt() == PassengerStates.AT_THE_DISEMBARKING_ZONE);
        passenger.setSt(PassengerStates.AT_THE_LUGGAGE_COLLECTION_POINT);
        GenericIO.writeString("\nPASSENGER AT GOCOLLECTABAG");
        repos.updatePassengerState(passenger.getPassengerID(),PassengerStates.AT_THE_LUGGAGE_COLLECTION_POINT);

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

        do {
            //if(!this.porterAwake){
            //    GenericIO.writeString("\ngoCollectABag pass: " + passenger.getPassengerID() + " porterAwake false");
            //    continue;
            //} else
            GenericIO.writeString("\ngoCollectABag pass: " + passenger.getPassengerID() + " nBagsInTreadmill = " + this.nBagsInTreadmill);

            if(this.pHoldEmpty() && this.treadmill.get(passenger.getPassengerID()).isEmpty()) {
                GenericIO.writeString("\ngoCollectABag pass: " + passenger.getPassengerID() + " this.treadmill.get(passenger.getPassengerID()).isEmpty()");
                return false;
            }
            if(!this.treadmill.get(passenger.getPassengerID()).isEmpty()){
                GenericIO.writeString("\ngoCollectABag pass: " + passenger.getPassengerID() + " !this.treadmill.get(passenger.getPassengerID()).isEmpty()");
                try {
                    this.treadmill.get(passenger.getPassengerID()).read();
                    //GenericIO.writeString("\nREMOVED");
                    //System.exit(-1);
                    this.nBagsInTreadmill -= 1;
                    GenericIO.writeString("\nBags in treadmill: " + this.nBagsInTreadmill);
                    passenger.setNA(passenger.getNA() + 1);
                    repos.baggageCollected(passenger.getPassengerID(), passenger);
                    repos.updateStoredBaggageConveyorBeltDec();
                    GenericIO.writeString("\nwake up gocollectabag");
                    GenericIO.writeString(" passid " + passenger.getPassengerID());
                    return true;
                } catch (MemException e) {
                    e.printStackTrace();
                }
            } else if(this.pHoldEmpty()){
                GenericIO.writeString("\ngoCollectABag pass: " + passenger.getPassengerID() + " this.pHoldEmpty()");
                return false;
            }

            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            GenericIO.writeString("\ngoCollectABag pass wake up: " + passenger.getPassengerID());
        } while(true);

        /*
        while(!this.pHoldEmpty() || (this.pHoldEmpty()
                                      && this.treadmill.containsKey(passenger.getPassengerID())
                                      && !this.treadmill.get(passenger.getPassengerID()).isEmpty())){
            GenericIO.writeString("\nAre all bags collected: " + this.pHoldEmpty());
            GenericIO.writeString("\nBags in treadmill: " + this.nBagsInTreadmill);
            GenericIO.writeString("\nstack do pass na treadmill, empty: " + this.treadmill.get(passenger.getPassengerID()).isEmpty());
            GenericIO.writeString("\nsleep gocollectabag");
            GenericIO.writeString(" passid " + passenger.getPassengerID());
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            GenericIO.writeString("\nwake up gocollectabag");
            if(this.pHoldEmpty()){
                if(!this.treadmill.containsKey(passenger.getPassengerID()) || this.treadmill.get(passenger.getPassengerID()).isEmpty()) {
                    return false;
                }
            }
            GenericIO.writeString("\nwake up gocollectabag");
            GenericIO.writeString(" passid " + passenger.getPassengerID());
            try {
                this.treadmill.get(passenger.getPassengerID()).read();
                //GenericIO.writeString("\nREMOVED");
                //System.exit(-1);
                this.nBagsInTreadmill -= 1;
                GenericIO.writeString("\nBags in treadmill: " + this.nBagsInTreadmill);
                passenger.setNA(passenger.getNA() + 1);
                repos.baggageCollected(passenger.getPassengerID(), passenger);
                repos.updateStoredBaggageConveyorBeltDec();
                GenericIO.writeString("\nwake up gocollectabag");
                GenericIO.writeString(" passid " + passenger.getPassengerID());
                return true;
            } catch (MemException e) {
                if (this.pHoldEmpty() && this.treadmill.containsKey(passenger.getPassengerID())){
                    return false;
                }
            }
        }

        return false;
        */
    }


    /* **************************************************Porter****************************************************** */

    /**
     *   ... (raised by the Porter).
     *
     */

    public synchronized void noMoreBagsToCollect(){

        GenericIO.writeString("\nnoMoreBagsToCollect");
        Porter porter = (Porter) Thread.currentThread();
        assert(porter.getStat() == PorterStates.AT_THE_PLANES_HOLD);
        porter.setStat(PorterStates.WAITING_FOR_A_PLANE_TO_LAND);
        repos.updatePorterState(PorterStates.WAITING_FOR_A_PLANE_TO_LAND);

        notifyAll();  // wake up Passengers in goCollectABag()

    }

    /**
     *  Operation of carrying a bag from the plane's hold to the baggage colletion point (raised by the Porter).
     */

    public synchronized void carryItToAppropriateStore(Bag bag){
        GenericIO.writeString("\ncarryItToAppropriateStore");

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
            GenericIO.writeString("\ncarryItToAppropriateStore notify bag in treadmill from pass " + bag.getIdOwner());
        } catch (MemException e) {
            e.printStackTrace();
        }
    }

    public synchronized void resetBaggageColPoint(){
        GenericIO.writeString("\nresetBaggageColPoint");
        this.nBagsInTreadmill = 0;
        this.allBagsCollects = false;
        this.lastBagId = -1;
        this.treadmill.clear();
        this.porterAwake = false;
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

    public boolean pHoldEmpty() {
        return allBagsCollects;
    }

    public void setAllBagsCollected(boolean allBagsCollects) {
        this.allBagsCollects = allBagsCollects;
    }

    public void setPorterAwake(boolean porterAwake) {
        this.porterAwake = porterAwake;
    }
}
