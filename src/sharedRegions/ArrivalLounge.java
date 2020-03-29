package sharedRegions;

import commonInfrastructures.MemException;
import commonInfrastructures.MemFIFO;
import commonInfrastructures.MemStack;
import entities.*;
import main.AirportConcurrentVersion;
import main.SimulationParameters;

import java.util.*;

/**
 *   ...
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class ArrivalLounge {

    /*
     *   Stack of bags on the plane's hold.
     */

    private MemStack<Bag> bagStack;

    /*
     *   Number of passengers that already arrived.
     */

    private int passCounter;

    /*
     *
     */

    private boolean existsPassengers;

    /*
     *   General repository of information.
     */

    private GenReposInfo repos;

    /*
     *   Baggage Collection Point.
     */

    private BaggageColPoint bagColPoint;

    /**
     *   Instantiation of the Arrival Lounge.
     *
     *     @param destStat destination state of the bags.
     *     @param nBagsPHold number of bags per passenger and flight.
     *     @param bagColPoint baggage collection point.
     *     @param repos general repository of information.
     */

    public ArrivalLounge(char[][] destStat, int[][] nBagsPHold, BaggageColPoint bagColPoint, GenReposInfo repos)
            throws MemException {

        this.existsPassengers = true;

        this.repos = repos;

        Map<Integer, MemFIFO<Bag>> treadmill = new HashMap<>();
        Map<Integer, Integer> nBagsPerPass = new HashMap<>();

        int nTotalBags = 0;
        for(int nPass = 0; nPass < SimulationParameters.N_PASS_PER_FLIGHT; nPass++){
            nTotalBags += nBagsPHold[nPass][repos.getFN()];
            nBagsPerPass.put(nPass, nBagsPHold[nPass][repos.getFN()]);
        }

        this.bagStack = new MemStack<> (new Bag [nTotalBags]);     // stack instantiation

        for(int nPass = 0; nPass < SimulationParameters.N_PASS_PER_FLIGHT; nPass++){
            this.bagStack.write(new Bag(destStat[nPass][repos.getFN()], nPass));
            MemFIFO<Bag> bagPassFIFO =  new MemFIFO<>(new Bag [nBagsPerPass.get(nPass)]);        // FIFO instantiation
            treadmill.put(nPass, bagPassFIFO);
        }

        this.bagColPoint = bagColPoint;
        this.bagColPoint.setTreadmill(treadmill);

        this.passCounter = 0;

    }

    /* **************************************************Passenger*************************************************** */

    /**
     *   Operation of deciding what to do next (raised by the Passenger).
     *   <p> Head start delay, that represents the time before the passenger chooses between what to do when arriving to
     *   the airport.
     *
     *     @return <li> true, if final destination
     *             <li> false, otherwise
     */

    public synchronized boolean whatShouldIDo(){

        Passenger currentPassenger = (Passenger) Thread.currentThread();
        assert(currentPassenger.getSt() == PassengerStates.AT_THE_DISEMBARKING_ZONE);
        passCounter += 1;

        notifyAll();  // wake up Porter in takeARest()

        try {
            wait(new Random().nextInt(AirportConcurrentVersion.maxSleep - AirportConcurrentVersion.minSleep + 1)
                    + AirportConcurrentVersion.minSleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return currentPassenger.getSi() == Passenger.SituationPassenger.FDT;
    }

    /* **************************************************Porter****************************************************** */

    /**
     *   Operation of taking a rest (raised by the Porter).
     *
     *     @return <li> 'E', if end of state </li>
     *             <li> 'R', otherwise </li>
     */

    public synchronized char takeARest(){
        /*
         *   Blocked Entity: Porter
         *   Freeing Entity: Passenger
         *   Freeing Method: whatShouldIDo()
         *   Freeing Condition: Last passenger to reach the arrival lounge
         *   Blocked Entity Reaction: tryToCollectABag()
         *
         *   Freeing Condition: No more passengers in the airport
         *   Blocked Entity Reaction: finish the thread
         */

        Porter porter = (Porter) Thread.currentThread();
        assert(porter.getStat() == PorterStates.WAITING_FOR_A_PLANE_TO_LAND);

        while (this.passCounter != SimulationParameters.N_PASS_PER_FLIGHT || !this.existsPassengers){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(this.existsPassengers){
            return 'R';
        }
        return 'E';
    }

    /**
     *   Operation of trying to collect a bag from the Plane's hold (raised by the Porter).
     *
     *     @return <li> a bag, if there is still bags on the Plane's hold.</li>
     *             <li> null, otherwise. </li>
     */

    public synchronized Bag tryToCollectABag(){

        Porter porter = (Porter) Thread.currentThread();
        assert(porter.getStat() == PorterStates.WAITING_FOR_A_PLANE_TO_LAND);
        porter.setStat(PorterStates.AT_THE_PLANES_HOLD);
        repos.updatePorterState(PorterStates.AT_THE_PLANES_HOLD);

        notifyAll();  // wake up Passengers in goCollectABag()

        try {
            return bagStack.read();
        } catch (MemException e) {
            bagColPoint.setCollected(true);  // tell the passengers that there is no more bags arriving the bcColPoint
            return null;
        }

    }

    /**
     *   ... (raised by the Porter).
     *
     */

    public synchronized void noMoreBagsToCollect(){

        Porter porter = (Porter) Thread.currentThread();
        assert(porter.getStat() == PorterStates.AT_THE_PLANES_HOLD);
        porter.setStat(PorterStates.WAITING_FOR_A_PLANE_TO_LAND);
        repos.updatePorterState(PorterStates.WAITING_FOR_A_PLANE_TO_LAND);
    }

    /* ******************************************** Getters and Setters ***********************************************/

    /**
     *   Setter for existsPassengers.
     *
     *   @param existsPassengers ...
     */

    public void setExistsPassengers(boolean existsPassengers) {
        this.existsPassengers = existsPassengers;
    }

}
