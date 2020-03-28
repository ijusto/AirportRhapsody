package sharedRegions;

import commonInfrastructures.MemException;
import commonInfrastructures.MemStack;
import entities.*;
import main.AirportConcurrentVersion;

import java.util.Queue;
import java.util.Random;

/**
 * ...
 *
 * @author Inês Justo
 * @author Miguel Lopes
 */

public class ArrivalLounge {

    /*
     * TODO: add Stack (guardar as malas para irem para o aviao)
     * */
    int numberOfBags = 8; /* delete later */
    private MemStack<Bag> bagStack;

    private int passCounter;

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

    public ArrivalLounge(int[][] destStat, int[][] nBagsPHold, BaggageColPoint bagColPoint, GenReposInfo repos) throws MemException {
        this.destStat = destStat;
        this.nBagsPHold = nBagsPHold;
        this.bagColPoint = bagColPoint;
        this.repos = repos;
        this.bagStack = new MemStack<> (new Bag [numberOfBags]);     // stack instantiation
        this.passCounter = 0;
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

        if(currentPassenger.getSt() == PassengerStates.AT_THE_DISEMBARKING_ZONE) {
            try {
                wait(new Random().nextInt(AirportConcurrentVersion.maxSleep - AirportConcurrentVersion.minSleep + 1) + AirportConcurrentVersion.minSleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return currentPassenger.getSi() == Passenger.SituationPassenger.FDT;
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
        passenger.setSt(PassengerStates.AT_THE_LUGGAGE_COLLECTION_POINT);

        return false;
    }

    /**
     *  ... (raised by the Passenger).
     *
     */
    public synchronized void goHome(){

        Passenger passenger = (Passenger) Thread.currentThread();
        passenger.setSt(PassengerStates.EXITING_THE_ARRIVAL_TERMINAL);

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
        porter.setStat(PorterStates.WAITING_FOR_A_PLANE_TO_LAND);

        // bloqueia porter
        return 'E';
    }

    /**
     *  ... (raised by the Porter).
     *
     */
    // garanteed by prof
    public synchronized Bag tryToCollectABag(){

        Porter porter = (Porter) Thread.currentThread();
        porter.setStat(PorterStates.AT_THE_PLANES_HOLD);

        return new Bag();
    }

    /**
     *  ... (raised by the Porter).
     *
     */
    // garanteed by prof
    public synchronized void noMoreBagsToCollect(){

        Porter porter = (Porter) Thread.currentThread();
        porter.setStat(PorterStates.WAITING_FOR_A_PLANE_TO_LAND);

    }
}
