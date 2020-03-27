package sharedRegions;
import commonInfrastructures.Bag;
import entities.EntitiesStates;
import entities.*;
import main.AirportRhapsody;

import java.lang.reflect.Array;
import java.util.Random;
import java.util.Queue;

import static java.lang.Thread.sleep;

/**
 * ...
 *
 * @author Inês Justo
 * @author Miguel Lopes
 */

public class ArrivalLounge {

    /*
     *
     */

    public Queue<Passenger> passengerQueue;

    /*
     *
     */

    private GenReposInfo repos;

    /*
     *
     */

    private BaggageColPoint bagColPoint;

    /*
     *
     */

    private int[][] destStat;

    /*
     *
     */

    private int[][] nBagsPHold;

    /*
     *
     */

    public ArrivalLounge(int[][] destStat, int[][] nBagsPHold, BaggageColPoint bagColPoint, GenReposInfo repos){
        this.destStat = destStat;
        this.nBagsPHold = nBagsPHold;
        this.bagColPoint = bagColPoint;
        this.repos = repos;
    }

    /* **************************************************Passenger*************************************************** */

    /**
     *  Operation of deciding what to do next (raised by the Passenger). <p> Head start delay, that represents the time before the passenger chooses between what to do when arriving to the airport.
     *
     *    @return <li> true, if final destination
     *            <li> false, otherwise
     */
    // garanteed by prof
    public synchronized boolean whatShouldIDo(){

        Passenger currentPassenger = (Passenger) Thread.currentThread();

        if(currentPassenger.getSt() == EntitiesStates.AT_THE_DISEMBARKING_ZONE) {
            try {
                sleep((long) (new Random().nextInt(AirportRhapsody.maxSleep - AirportRhapsody.minSleep + 1) + AirportRhapsody.minSleep));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return currentPassenger.getSi() == Passenger.SituationPassenger.FDT;
    }

    /**
     *  Operation of taking a Bus (raised by the Passenger). <p> functionality: change state of entities.Passenger to AT_THE_ARRIVAL_TRANSFER_TERMINAL
     *
     *    @return idk
     */

    public synchronized char takeABus(){
        /*
          Blocked Entity: Passenger
          Freeing Entity: Driver
          Freeing Method: announcingBusBoarding()
          Blocked Entity Reactions: enterTheBus()
        */

        Passenger passenger = (Passenger) Thread.currentThread();
        passenger.setSt(EntitiesStates.AT_THE_ARRIVAL_TRANSFER_TERMINAL);

        return 0;
    }

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
        passenger.setSt(EntitiesStates.AT_THE_LUGGAGE_COLLECTION_POINT);

        return false;
    }

    /**
     *  ... (raised by the Passenger).
     *
     */
    public synchronized void goHome(){

        Passenger passenger = (Passenger) Thread.currentThread();
        passenger.setSt(EntitiesStates.EXITING_THE_ARRIVAL_TERMINAL);

    }


    /* **************************************************Porter****************************************************** */

    /**
     *  Operation of taking a rest (raised by the Porter). <p> functionality: change state of entities.Porter to WAITING_FOR_A_PLANE_TO_LAND
     *
     *    @return <li> 'E', if end of state
     *            <li> false, otherwise
     */
    // garanteed by prof
    public synchronized char takeARest(){
        /*
          Blocked Entity: Porter
          Freeing Entity: Passenger
          Freeing Method: whatShouldIDo()
          Freeing Condition: Last passenger to reach the arrival lounge
          Blocked Entity Reaction: tryToCollectABag()
         */

        Porter porter = (Porter) Thread.currentThread();
        porter.setStat(EntitiesStates.WAITING_FOR_A_PLANE_TO_LAND);

        // bloqueia porter
        return 'E'; // 'E' character return means end of state
    }

    /**
     *  ... (raised by the Porter).
     *
     */
    // garanteed by prof
    public synchronized Bag tryToCollectABag(){

        Porter porter = (Porter) Thread.currentThread();
        porter.setStat(EntitiesStates.AT_THE_PLANES_HOLD);

        return new Bag();
    }

    /**
     *  ... (raised by the Porter).
     *
     */
    // garanteed by prof
    public synchronized void noMoreBagsToCollect(){

        Porter porter = (Porter) Thread.currentThread();
        porter.setStat(EntitiesStates.WAITING_FOR_A_PLANE_TO_LAND);

    }
}
