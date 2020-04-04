package sharedRegions;

import commonInfrastructures.MemException;
import commonInfrastructures.MemFIFO;
import commonInfrastructures.MemStack;
import entities.*;
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
     *   True if all the passengers from the current flight left, false otherwise.
     */

    private boolean allPassDead;

    /*
     *   Number of the current flight.
     */

    private int currentFlight;

    /*
     *   True if resetArrivalLounge is called and there are no passengers in the arrival Lounge, false otherwise.
     *   If false, the porter must wait when there is no more bags in the plane's Hold.
     */

    private boolean reset;

    /**
     *
     */
    private boolean porterStop;

    /**
     *
     */
    private boolean pHEmpty;


    /**
     *
     */
    private boolean porterSleep;
    private boolean notifPassExit;


    /*
     *   Arrival Terminal Exit.
     */

    private ArrivalTerminalExit arrivTerm;


    /*
     *   Departure Terminal Entrance.
     */

    private DepartureTerminalEntrance depTerm;



    /**
     *   Instantiation of the Arrival Lounge.
     *
     *     @param repos general repository of information.
     *     @param bagColPoint baggage collection point.
     *     @param destStat destination state of the bags.
     *     @param nBagsPHold number of bags per passenger and flight.
     */

    public ArrivalLounge(GenReposInfo repos, BaggageColPoint bagColPoint, char[][] destStat, int[][] nBagsPHold)
            throws MemException {

        this.repos = repos;

        this.allPassDead = false;

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
        this.bagColPoint.setPHoldNotEmpty();
        this.bagColPoint.setTreadmill(treadmill);

        this.nPassAtArrivL = 0;

        this.porterStop = true;
        this.reset = false;
        this.porterSleep = true;
        this.pHEmpty = false;
        this.notifPassExit = false;

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

        System.out.print("\nwhatShouldIDo");

        Passenger currentPassenger = (Passenger) Thread.currentThread();
        assert(currentPassenger.getSt() == PassengerStates.AT_THE_DISEMBARKING_ZONE);

        // increment passengers that arrive so the porter knows when to wake up in takeARest()
        this.nPassAtArrivL += 1;
        allPassDead = false;

        // update logger
        this.repos.updatesPassNR(currentPassenger.getPassengerID(), currentPassenger.getNR());
        this.repos.numberNRTotal(currentPassenger.getNR());
        this.repos.newPass(currentPassenger.getSi());

        //this.reset = false;

        if(this.nPassAtArrivL == SimulationParameters.N_PASS_PER_FLIGHT) {
            // wake up Porter in takeARest()
            notifPassExit = false;
            notifyAll();
        }

        return currentPassenger.getSi() == Passenger.SituationPassenger.FDT;
    }

    /* **************************************************Porter****************************************************** */

    /**
     *   Operation of taking a rest (raised by the Porter).
     *   The porter is waken up by the operation whatShouldIDo of the last of the passengers to reach the arrival
     *   lounge.
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

        System.out.print("\ntakeARest");
        System.out.print("\n-------------TAKE A REST-------------------------");

        Porter porter = (Porter) Thread.currentThread();
        assert(porter.getStat() == PorterStates.WAITING_FOR_A_PLANE_TO_LAND);

        if(this.currentFlight == SimulationParameters.N_FLIGHTS - 1 && this.allPassDead && this.pHEmpty){ // && bagColPoint.areAllBagsCollects()) {
            return 'E';
        } else {
            if(this.nPassAtArrivL < SimulationParameters.N_PASS_PER_FLIGHT) {
                while (this.nPassAtArrivL < SimulationParameters.N_PASS_PER_FLIGHT) {//&& this.porterStop){

                    // while((this.nArrivPass < SimulationParameters.N_PASS_PER_FLIGHT || this.porterStopNoMoreBagsAndThereAreStillPassOnTheAirp) && (this.currentFlight < SimulationParameters.N_FLIGHTS - 1)){ // && !this.changedFlight) {
                    System.out.print("\nsleep takeARest");

                    this.porterSleep = true;
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.print("\nwake up takeARest (normal state)");
                    System.out.print("\nTake a rest, currentflight: " + this.currentFlight);
                    System.out.print("\nTake a rest, bagColPoint.areAllBagsCollects(): " + bagColPoint.pHoldEmpty());

                    // end he's life cycle if the porter is resting in the last flight and all passengers left the airport
                    // TODO: verify if this needs to change and allPassDead is set in the right places

                }
            } else {
                while (!this.notifPassExit) {//&& this.porterStop){
                    // while((this.nArrivPass < SimulationParameters.N_PASS_PER_FLIGHT || this.porterStopNoMoreBagsAndThereAreStillPassOnTheAirp) && (this.currentFlight < SimulationParameters.N_FLIGHTS - 1)){ // && !this.changedFlight) {
                    System.out.print("\nsleep takeARest");

                    this.porterSleep = true;
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        this.porterSleep = false;



        System.out.print("\nthis.nPassAtArrivL == SimulationParameters.N_PASS_PER_FLIGHT: " + (this.nPassAtArrivL == SimulationParameters.N_PASS_PER_FLIGHT));

        System.out.print("\nthis.currentFlight == SimulationParameters.N_FLIGHTS - 1: " + (this.currentFlight == SimulationParameters.N_FLIGHTS - 1));
        System.out.print("\nthis.allPassDead " + this.allPassDead);


        System.out.print("\n-------------END TAKE A REST-------------------------");

        return 'R';
    }

    /**
     *   Operation of trying to collect a bag from the Plane's hold (raised by the Porter).
     *
     *     @return <li> a bag, if there is still bags on the Plane's hold.</li>
     *             <li> null, otherwise. </li>
     */

    public synchronized Bag tryToCollectABag(){
        System.out.print("\ntryToCollectABag");

        Porter porter = (Porter) Thread.currentThread();
        assert(porter.getStat() == PorterStates.WAITING_FOR_A_PLANE_TO_LAND);
        porter.setStat(PorterStates.AT_THE_PLANES_HOLD);

        // update logger
        repos.updatePorterStat(PorterStates.AT_THE_PLANES_HOLD);

        try {
            Bag tmpBag = pHoldBagStack.read();

            // update logger
            repos.removeBagFromCargoHold();

            return tmpBag;
        } catch (MemException e) {

            // change allBagsCollected so the passengers know there are no more bags arriving the bcColPoint
            // when they get awaken up by noMoreBagsToCollect()
            bagColPoint.setAllBagsCollected();

            System.out.print("\nsetAllBagsCollected " + this.bagColPoint.pHoldEmpty());

            this.pHEmpty = true;
            if(!allPassDead) {
                // notify passenger in goCollectABag()
                bagColPoint.noMoreBags();
            } else {
                if(!notifPassExit){
                    depTerm.notifyPHEmpty();
                    arrivTerm.notifyPHEmpty();
                    notifPassExit = true;
                    //notifyAll(); // wake up dayOver
                }
            }

            System.out.print("\ntrytocollectabag notify no more bags");

            //this.porterStopNoMoreBagsAndThereAreStillPassOnTheAirp = true;

            return null;
        }

    }

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


    public synchronized void resetArrivalLounge(char[][] destStat, int[][] nBagsPHold, BaggageColPoint bagColPoint)
            throws MemException {

        // update flight number
        this.currentFlight += 1;

        // update logger
        repos.updateFlightNumber(this.currentFlight);

        System.out.print("\nresetArrivalLounge");
        System.out.print("\nPorter stoped");
        System.out.print("\nreset "+ this.reset);
        //do {
        //} while (this.porterStopNoMoreBagsAndThereAreStillPassOnTheAirp);
        System.out.print("\nreset "+ this.reset);
        System.out.print("\nPorter started");

        Map<Integer, MemFIFO<Bag>> treadmill = new HashMap<>();
        Map<Integer, Integer> nBagsPerPass = new HashMap<>();

        int nTotalBags = 0;
        for(int nPass = 0; nPass < SimulationParameters.N_PASS_PER_FLIGHT; nPass++){
            nTotalBags += nBagsPHold[nPass][this.currentFlight];
            nBagsPerPass.put(nPass, nBagsPHold[nPass][this.currentFlight]);
        }

        // update logger
        repos.initializeCargoHold(nTotalBags);

        // plane's hold baggage stack instantiation
        this.pHoldBagStack = new MemStack<> (new Bag [nTotalBags]);

        for(int nPass = 0; nPass < SimulationParameters.N_PASS_PER_FLIGHT; nPass++){
            for(int bag = 0; bag < nBagsPHold[nPass][this.currentFlight]; bag++){
                this.pHoldBagStack.write(new Bag(destStat[nPass][this.currentFlight], nPass));
            }

            // instantiation of the passenger's bag FIFO for the treadmill in the baggage collection point
            MemFIFO<Bag> bagPassFIFO =  new MemFIFO<>(new Bag [nBagsPerPass.get(nPass)]);
            treadmill.put(nPass, bagPassFIFO);
        }

        this.bagColPoint = bagColPoint;
        this.bagColPoint.setPHoldNotEmpty();
        this.bagColPoint.setTreadmill(treadmill);

        // reset the number of passengers that arrived the airport
        this.nPassAtArrivL = 0;


        this.reset = true;
        this.pHEmpty = false;
        /*
        this.reset = true;


        notifyAll();

         */
    }

    /* ************************************************* Setters ******************************************************/


    /**
     *   Setter for existsPassengers to false.
     */

    public synchronized void setNoPassAtAirport() {
        System.out.print("\nsetNoPassAtAirport");
        this.allPassDead = true;
    }

    /**
     *   Sets the Departure Terminal Entrance Reference.
     *
     *    @param departureTerm Departure Terminal Entrance.
     */

    public synchronized void setDepartureTerminalRef(DepartureTerminalEntrance departureTerm){
        this.depTerm = departureTerm;
    }

    /**
     *   ...
     *
     *    @param arrivalTerm Arrival Terminal Exit.
     */

    public synchronized void setArrivalTerminalRef(ArrivalTerminalExit arrivalTerm){
        this.arrivTerm = arrivalTerm;
    }

    public synchronized void dayOver(){ //wake up porter in take a rest
        System.out.print("\nbagColPoint.pHoldEmpty(): " + bagColPoint.pHoldEmpty());
        System.out.print("\ndayOver sleep");

        if(this.currentFlight == SimulationParameters.N_FLIGHTS - 1) {
            while (!this.pHEmpty) {//!bagColPoint.pHoldEmpty()) {//&& this.porterStop){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.print("\ndayOver wake up ");

            notify();
        }
    }


    public synchronized int getCurrentFlight(){
        return currentFlight;
    }

    public synchronized  boolean ispHoldNotEmpty() {
        return !pHEmpty;
    }

    public synchronized void allPassExited(){
        notifPassExit = true;
        notify();
    }

}
