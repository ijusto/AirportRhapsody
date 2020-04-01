package sharedRegions;

import commonInfrastructures.MemException;
import commonInfrastructures.MemFIFO;
import commonInfrastructures.MemStack;
import entities.*;
import genclass.GenericIO;
import main.SimulationParameters;

import java.util.HashMap;
import java.util.Map;

/**
 *   ...
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class ArrivalLounge {

    /**
     *   General repository of information.
     */

    private GenReposInfo repos;

    /**
     *   Baggage Collection Point.
     */

    private BaggageColPoint bagColPoint;

    /*
     *   Stack of bags on the plane's hold.
     */

    private MemStack<Bag> pHoldBagStack;

    /*
     *   Number of passengers that already arrived.
     */

    private int nPassAtArrivL;

    /*
     *
     */

    private boolean existsPassengers;

    /*
     *
     */
    //private boolean changedFlight;

    /*
     *
     */
    private int currentFlight;

    /*
     *
     */
    private boolean reset;

    /*
     *
     */
    private int shift;

    /**
     *
     */
    private boolean porterStopNoMoreBagsAndThereAreStillPassOnTheAirp;


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

        this.repos = repos;

        this.existsPassengers = true;

        this.currentFlight = 0;
        repos.updateFlightNumber(this.currentFlight);

        Map<Integer, MemFIFO<Bag>> treadmill = new HashMap<>();
        Map<Integer, Integer> nBagsPerPass = new HashMap<>();
        int nTotalBags = 0;
        for(int nPass = 0; nPass < SimulationParameters.N_PASS_PER_FLIGHT; nPass++){
            nTotalBags += nBagsPHold[nPass][this.currentFlight];
            nBagsPerPass.put(nPass, nBagsPHold[nPass][this.currentFlight]);
        }
        repos.initializeCargoHold(nTotalBags);
        this.pHoldBagStack = new MemStack<> (new Bag [nTotalBags]);     // stack instantiation
        for(int nPass = 0; nPass < SimulationParameters.N_PASS_PER_FLIGHT; nPass++){
            for(int bag = 0; bag < nBagsPHold[nPass][this.currentFlight]; bag++){
                this.pHoldBagStack.write(new Bag(destStat[nPass][this.currentFlight], nPass));
            }
            MemFIFO<Bag> bagPassFIFO =  new MemFIFO<>(new Bag [nBagsPerPass.get(nPass)]);        // FIFO instantiation
            treadmill.put(nPass, bagPassFIFO);
        }

        this.bagColPoint = bagColPoint;
        this.bagColPoint.setTreadmill(treadmill);

        this.nPassAtArrivL = 0;

        this.porterStopNoMoreBagsAndThereAreStillPassOnTheAirp = false;
        this.reset = false;

        this.shift = 0;

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

        GenericIO.writeString("\nwhatShouldIDo");
        Passenger currentPassenger = (Passenger) Thread.currentThread();
        assert(currentPassenger.getSt() == PassengerStates.AT_THE_DISEMBARKING_ZONE);
        this.nPassAtArrivL += 1;
        this.repos.numberOfPassengerLuggage(currentPassenger.getPassengerID(), currentPassenger);

        notifyAll();  // wake up Porter in takeARest()

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

        GenericIO.writeString("\ntakeARest");
        Porter porter = (Porter) Thread.currentThread();
        assert(porter.getStat() == PorterStates.WAITING_FOR_A_PLANE_TO_LAND);

        GenericIO.writeString("\n-------------TAKE A REST-------------------------");

        while ((this.nPassAtArrivL < SimulationParameters.N_PASS_PER_FLIGHT || bagColPoint.areAllBagsCollects()) && !this.reset){

       // while((this.nArrivPass < SimulationParameters.N_PASS_PER_FLIGHT || this.porterStopNoMoreBagsAndThereAreStillPassOnTheAirp) && (this.currentFlight < SimulationParameters.N_FLIGHTS - 1)){ // && !this.changedFlight) {
            GenericIO.writeString("\nsleep takeARest");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            GenericIO.writeString("\nwake up takeARest (normal state)");
            GenericIO.writeString("Take a rest, currentflight: " + this.currentFlight);
            GenericIO.writeString("Take a rest, bagColPoint.areAllBagsCollects(): " + bagColPoint.areAllBagsCollects());
            if(this.currentFlight == SimulationParameters.N_FLIGHTS - 1 && bagColPoint.areAllBagsCollects()) {
                return 'E';
            }
            if(this.shift < this.currentFlight){
                break;
            }
        }

        if(this.reset){
            this.reset = false;
        }
        if(this.nPassAtArrivL == SimulationParameters.N_PASS_PER_FLIGHT){
            this.nPassAtArrivL = 0;
        }

        GenericIO.writeString("\n-------------END TAKE A REST-------------------------");

        return 'R';
    }

    /**
     *   Operation of trying to collect a bag from the Plane's hold (raised by the Porter).
     *
     *     @return <li> a bag, if there is still bags on the Plane's hold.</li>
     *             <li> null, otherwise. </li>
     */

    public synchronized Bag tryToCollectABag(){
        GenericIO.writeString("\ntryToCollectABag");

        Porter porter = (Porter) Thread.currentThread();
        assert(porter.getStat() == PorterStates.WAITING_FOR_A_PLANE_TO_LAND);
        porter.setStat(PorterStates.AT_THE_PLANES_HOLD);
        repos.updatePorterState(PorterStates.AT_THE_PLANES_HOLD);

        try {
            Bag tmpBag = pHoldBagStack.read();
            repos.updateStoredBaggageCargoHold();
            return tmpBag;
        } catch (MemException e) {
            bagColPoint.setAllBagsCollected();  // tell the passengers that there is no more bags arriving the bcColPoint
            GenericIO.writeString("\nsetAllBagsCollected " + this.bagColPoint.pHoldEmpty());
            //notifyAll();  // wake up Passengers in goCollectABag()
            GenericIO.writeString("\ntrytocollectabag notify no more bags");
            // GenericIO.writeString("ACABOU VÊ SE ENTENDES");
            // System.exit(-1);

            this.porterStopNoMoreBagsAndThereAreStillPassOnTheAirp = true;
            return null;
        }

    }

    public synchronized void resetArrivalLounge(char[][] destStat, int[][] nBagsPHold, BaggageColPoint bagColPoint) throws MemException {

        this.currentFlight += 1;
        notifyAll();
        GenericIO.writeString("\nresetArrivalLounge");
        GenericIO.writeString("Porter stoped");
        do {
        } while (this.porterStopNoMoreBagsAndThereAreStillPassOnTheAirp);
        GenericIO.writeString("Porter started");
        repos.updateFlightNumber(this.currentFlight);

        this.existsPassengers = true;

        Map<Integer, MemFIFO<Bag>> treadmill = new HashMap<>();
        Map<Integer, Integer> nBagsPerPass = new HashMap<>();

        int nTotalBags = 0;
        for(int nPass = 0; nPass < SimulationParameters.N_PASS_PER_FLIGHT; nPass++){
            nTotalBags += nBagsPHold[nPass][this.currentFlight];
            nBagsPerPass.put(nPass, nBagsPHold[nPass][this.currentFlight]);
        }

        repos.initializeCargoHold(nTotalBags);
        this.pHoldBagStack = new MemStack<> (new Bag [nTotalBags]);     // stack instantiation

        for(int nPass = 0; nPass < SimulationParameters.N_PASS_PER_FLIGHT; nPass++){
            for(int bag = 0; bag < nBagsPHold[nPass][this.currentFlight]; bag++){
                this.pHoldBagStack.write(new Bag(destStat[nPass][this.currentFlight], nPass));
            }
            MemFIFO<Bag> bagPassFIFO =  new MemFIFO<>(new Bag [nBagsPerPass.get(nPass)]);        // FIFO instantiation
            treadmill.put(nPass, bagPassFIFO);
        }

        this.bagColPoint = bagColPoint;
        this.bagColPoint.setTreadmill(treadmill);

        this.nPassAtArrivL = 0;
        //this.changedFlight = true;
        this.reset = true;
        this.shift += 1;
    }

    public synchronized void wakeUpForNextFlight(){
        this.porterStart();
    }

    /* ************************************************* Getters ******************************************************/

    /**
     *
     *   @return existsPassengers
     */

    public boolean doPassExist() {
        return this.existsPassengers;
    }

    /* ************************************************* Setters ******************************************************/


    /**
     *   Setter for existsPassengers to false.
     */

    public void setNoPassAtAirport() {
        this.existsPassengers = false;
    }

    /**
     *   Setter for existsPassengers to false.
     */

    public void porterStart(){
        this.porterStopNoMoreBagsAndThereAreStillPassOnTheAirp = false;
    }

}
