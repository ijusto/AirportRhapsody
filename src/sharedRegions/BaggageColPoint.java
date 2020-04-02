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


    private boolean pHoldEmpty;

    /**
     *   Instantiation of the Baggage Collection Point.
     *
     *     @param repos general repository of information
     */

    public BaggageColPoint(GenReposInfo repos){
        this.repos = repos;
        this.nBagsInTreadmill = 0;
        this.pHoldEmpty = false;
        this.lastBagId = -1;
        this.porterAwake = false;
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
        System.out.print("\ngoCollectABag");
        System.out.print("\nPASSENGER AT GOCOLLECTABAG");

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

        do {

            System.out.print("\ngoCollectABag pass: " + passenger.getPassengerID() + " nBagsInTreadmill = " + this.nBagsInTreadmill);

            //
            if(this.pHoldEmpty() && this.treadmill.get(passenger.getPassengerID()).isEmpty()) {
                System.out.print("\ngoCollectABag pass: " + passenger.getPassengerID() + " this.treadmill.get(passenger.getPassengerID()).isEmpty()");
                return false;
            }
            if(!this.treadmill.get(passenger.getPassengerID()).isEmpty()){
                System.out.print("\ngoCollectABag pass: " + passenger.getPassengerID() + " !this.treadmill.get(passenger.getPassengerID()).isEmpty()");
                try {
                    this.treadmill.get(passenger.getPassengerID()).read();
                    this.nBagsInTreadmill -= 1;
                    System.out.print("\nBags in treadmill: " + this.nBagsInTreadmill);
                    passenger.setNA(passenger.getNA() + 1);
                    repos.updatesPassNA(passenger.getPassengerID(), passenger.getNA());
                    repos.pGetsABag();
                    System.out.print("\nwake up gocollectabag");
                    System.out.print(" passid " + passenger.getPassengerID());
                    return true;
                } catch (MemException e) {
                    e.printStackTrace();
                }
            } else if(this.pHoldEmpty()){
                System.out.print("\ngoCollectABag pass: " + passenger.getPassengerID() + " this.pHoldEmpty()");
                return false;
            }

            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.print("\ngoCollectABag pass wake up: " + passenger.getPassengerID());
        } while(true);

    }


    /* **************************************************Porter****************************************************** */

    /**
     *   ... (raised by the Porter).
     *
     */

    public synchronized void noMoreBagsToCollect(){
        System.out.print("\nnoMoreBagsToCollect");

        Porter porter = (Porter) Thread.currentThread();
        assert(porter.getStat() == PorterStates.AT_THE_PLANES_HOLD);
        porter.setStat(PorterStates.WAITING_FOR_A_PLANE_TO_LAND);

        // update logger
        repos.updatePorterStat(PorterStates.WAITING_FOR_A_PLANE_TO_LAND);

        //this.setAllBagsCollected();
     }

    /**
     *  Operation of carrying a bag from the plane's hold to the baggage colletion point (raised by the Porter).
     */

    public synchronized void carryItToAppropriateStore(Bag bag){
        System.out.print("\ncarryItToAppropriateStore");

        Porter porter = (Porter) Thread.currentThread();
        assert(porter.getStat() == PorterStates.AT_THE_PLANES_HOLD);
        porter.setStat(PorterStates.AT_THE_LUGGAGE_BELT_CONVEYOR);
        repos.updatePorterStat(PorterStates.AT_THE_LUGGAGE_BELT_CONVEYOR);

        try {
            this.treadmill.get(bag.getIdOwner()).write(bag);
            this.lastBagId = bag.getIdOwner();
            this.nBagsInTreadmill += 1;
            System.out.print("\nBags in treadmill: " + this.nBagsInTreadmill);
            repos.incBaggageCB();
            notifyAll();  // wake up Passengers in goCollectABag()
            System.out.print("\ncarryItToAppropriateStore notify bag in treadmill from pass " + bag.getIdOwner());
        } catch (MemException e) {
            e.printStackTrace();
        }
    }

    public synchronized void resetBaggageColPoint(){
        System.out.print("\nresetBaggageColPoint");
        while(!pHoldEmpty()){}
        this.nBagsInTreadmill = 0;
        this.pHoldEmpty = false;
        this.lastBagId = -1;
        this.treadmill.clear();
        this.porterAwake = false;
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

    public boolean pHoldEmpty() {
        return pHoldEmpty;
    }

    /* ************************************************* Setters ******************************************************/

    /**
     *   ...
     *
     *    @param treadmill ...
     */

    public void setTreadmill(Map<Integer, MemFIFO<Bag>> treadmill) {
        this.treadmill = treadmill;
    }

    /**
     *
     */

    public void setAllBagsCollected() {
        this.pHoldEmpty = true;
    }


}
