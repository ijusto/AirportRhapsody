package sharedRegions;

import commonInfrastructures.MemException;
import commonInfrastructures.MemFIFO;
import entities.BusDriver;
import entities.BusDriverStates;
import entities.Passenger;
import entities.PassengerStates;
import main.SimulationParameters;

/**
 *   ...
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class ArrivalTermTransfQuay {

    /*
     *   General Repository of Information.
     */

    private GenReposInfo repos;

    /*
     *   FIFO of passengers that want to enter the bus.
     *   A passenger belongs to the waitingLine in takeABus() and leaves the waitingLine in enterTheBus().
     */

    private MemFIFO<Passenger> waitingLine;

    /**
     *
     */

    private int nPassOnTheBus;


    /**
     *   True, if the bus driver is waiting in announcingBusBoarding();
     *   If false, when the passengers are waken up in takeABus() they go back to sleep.
     */

    private boolean allowBoardBus;

    /**
     *   Number of passengers that are allowed by the bus driver to enter.
     *   When the passengers are waken up in takeABus() they go back to sleep if this number isn't smaller than the bus
     *   seat capacity.
     */

    private int nPassAllowedToEnter;

    /**
     *
     */

    private int nFlights;

    //private int nWaitingPass;

    /**
     *
     */

    private boolean allPassDead;

    private boolean reset;

    private boolean endDay;

    /**
     *
     */

    private boolean busDriverStop;

    /**
     *   Instantiation of the Arrival Terminal Transfer Quay.
     *
     *     @param repos general repository of information
     */

    public ArrivalTermTransfQuay(GenReposInfo repos) throws MemException {
        this.repos = repos;
        this.waitingLine = new MemFIFO<>(new Passenger [SimulationParameters.N_PASS_PER_FLIGHT]);  // FIFO instantiation

        this.nPassAllowedToEnter = 0;
        this.allowBoardBus = false;

        this.nPassOnTheBus = 0;
        this.allPassDead = false;
        // this.nWaitingPass = 0;
        this.nFlights = 0;
        this.busDriverStop = false;

        this.reset = false;
        this.endDay = false;
    }

    /* ************************************************Passenger***************************************************** */

    /**
     *   Operation of taking a Bus (raised by the Passenger).
     *   Before blocking, the passenger wakes up the bus driver, if the passenger's place in the waiting queue equals
     *   the bus capacity, and is waken up by the operation announcingBusBoarding of the driver to mimic her entry
     *   in the bus.
     */

    public synchronized void takeABus() {

        /*
         *   Blocked Entity: Passenger
         *   Freeing Entity: Driver
         *   Freeing Method: announcingBusBoarding()
         *   Blocked Entity Reactions: enterTheBus()
         */

        System.out.print("\ntakeABus");

        Passenger passenger = (Passenger) Thread.currentThread();
        assert(passenger.getSt() == PassengerStates.AT_THE_DISEMBARKING_ZONE);
        passenger.setSt(PassengerStates.AT_THE_ARRIVAL_TRANSFER_TERMINAL);

        // update logger
        repos.updatePassSt(passenger.getPassengerID(), PassengerStates.AT_THE_ARRIVAL_TRANSFER_TERMINAL);

        try {
            waitingLine.write(passenger);

            // update logger
            repos.pJoinWaitingQueue(passenger.getPassengerID());
        } catch (MemException e) {
            e.printStackTrace();
        }

        // increment the number of passenger waiting to take a bus
        //this.nWaitingPass += 1;

        // the place of this passenger in the waiting queue = bus capacity
        if(waitingLine.getNObjects() == SimulationParameters.BUS_CAP){
            // wake up Bus Driver in parkTheBus()
            notifyAll();
        }

        System.out.print("\nnotify parkTheBus at arrival terminal (at least 3 passengers waiting)");

        while(!this.allowBoardBus || this.nPassAllowedToEnter >= SimulationParameters.BUS_CAP){
            System.out.print("\nsleep takeABus");

            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.print("\nwake up takeABus");
            System.out.print("\nthis.waitingPass.isEmpty(): " + this.waitingLine.isEmpty());
        }

        System.out.print("\nexit takeABus");

        this.nPassAllowedToEnter += 1;
    }

    /**
     *   ... (raised by the Passenger).
     */

    public synchronized void enterTheBus(){
        System.out.print("\nenterTheBus");

        Passenger passenger = (Passenger) Thread.currentThread();
        assert(passenger.getSt() == PassengerStates.AT_THE_ARRIVAL_TRANSFER_TERMINAL);
        assert(this.nPassOnTheBus < SimulationParameters.BUS_CAP);

        passenger.setSt(PassengerStates.TERMINAL_TRANSFER);

        // update logger
        repos.updatePassSt(passenger.getPassengerID(),PassengerStates.TERMINAL_TRANSFER);

        try{
            this.waitingLine.read();

            // update logger
            repos.pLeftWaitingQueue(passenger.getPassengerID());
            repos.occupyBusSeat(passenger.getPassengerID());

            //this.nWaitingPass -= 1;
            this.nPassOnTheBus += 1;

            System.out.print("\nPass " + passenger.getPassengerID() + " entered the bus.");

            // last passenger to enter the bus wakes up the bus driver
            if(this.nPassOnTheBus == SimulationParameters.BUS_CAP || this.waitingLine.isEmpty()){ //this.nWaitingPass == 0){
                System.out.print("\nLast passenger. notify announcingBusBoarding");

                // wake up Bus driver in announcingBusBoarding()
                notifyAll();
            }
        } catch (MemException e) {
            e.printStackTrace();
        }
    }

    /* ************************************************Bus Driver**************************************************** */

    /**
     *   Operation of checking if all the flights ended (raised by the BusDriver).
     *
     *     @return <li> 'F', if end of day </li>
     *             <li> 'R', otherwise </li>
     */

    public synchronized char hasDaysWorkEnded(){
        System.out.print("\nhasDaysWorkEnded");
        System.out.print("\nworkday: "+this.nFlights);

        BusDriver busDriver = (BusDriver) Thread.currentThread();
        assert(busDriver.getStat() == BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL);

        // if the last flight arrived and all passengers left the airport, end the bus driver life cycle
        if(this.endDay){
            return 'F';
        }

        return 'R';
    }

    /**
     *   ... (raised by the BusDriver).
     *   The Bus Driver is blocked until
     */

    public synchronized void parkTheBus(){
        System.out.print("\nparkTheBus");
        /*
         *   Blocked Entity: Driver
         *   1) Freeing Entity: Passenger
         *   1) Freeing Method: takeABus()
         *   1) Freeing Condition: place in waiting queue = bus capacity
         *   1) Blocked Entity Reactions: announcingBusBoarding()
         *
         *   2) Freeing Entity: Driver
         *   2) Freeing Method: time
         *   2) Freeing Condition: at least 1 passenger in queue
         *   2) Blocked Entity Reaction: announcingBusBoarding()
         */

        BusDriver busDriver = (BusDriver) Thread.currentThread();
        assert(busDriver.getStat() == BusDriverStates.DRIVING_BACKWARD);
        busDriver.setStat(BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL);

        // update logger
        repos.updateBDriverStat(BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL);

        this.nPassOnTheBus = 0;

        // System.out.print("\nnWaitingPass parkTheBus " + this.nWaitingPass);

        System.out.print("\nsleep parkTheBus");


        //while ((this.nWaitingPass == 0 && this.workDay < SimulationParameters.N_FLIGHTS)
        //        || (this.busdriverStop && this.workDay < SimulationParameters.N_FLIGHTS - 1)){


        // System.out.print("\nthis.nWaitingPass == 0 : " + (this.nWaitingPass == 0));
        System.out.print("\nwaitingLine.getNObjects() != SimulationParameters.BUS_CAP " + (waitingLine.getNObjects() != SimulationParameters.BUS_CAP));
        System.out.print("\nthis.nFlights == SimulationParameters.N_FLIGHTS && this.allPassDead " + (this.nFlights == SimulationParameters.N_FLIGHTS && this.allPassDead));
        System.out.print("\n!this.waitingLine.isEmpty() " + (!this.waitingLine.isEmpty()));

        while(waitingLine.getNObjects() != SimulationParameters.BUS_CAP){ // if false waken up by takeABus()
            try {
                // wait until the departure time has been reached and verify again if there are passengers waiting
                wait(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // if the last flight arrived and all passengers left the airport, stop waiting
            if(this.endDay){ //!this.busDriverStop){
                break;
            }

            // if there are passengers waiting
            if(!this.waitingLine.isEmpty()){
                System.out.print("\nwake up parkTheBus because !this.waitingLine.isEmpty()");
                break;
            }
        }

        System.out.print("\nwake up parkTheBus");
    }

    /**
     *   ... (raised by the BusDriver).
     */

    public synchronized void announcingBusBoarding(){
        System.out.print("\nannouncingBusBoarding");

        /*
         *   Blocked Entity: Driver
         *   Freeing Entity: Passenger
         *   Freeing Method: enterTheBus()
         *   Freeing Condition: Last passenger in queue
         *   Blocked Entity Reaction: goToDepartureTerminal()
        */

        BusDriver busDriver = (BusDriver) Thread.currentThread();
        assert(busDriver.getStat() == BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL);

        this.allowBoardBus = true;

        // wake up Passengers in takeABus()
        notifyAll();

        while(!(this.nPassOnTheBus == SimulationParameters.BUS_CAP || this.waitingLine.isEmpty())){ //this.nWaitingPass == 0)) {
            System.out.print("\nsleep announcingBusBoarding");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.print("\nwake up announcingBusBoarding");
        }

        this.allowBoardBus = false;
        this.nPassAllowedToEnter = 0;
        busDriver.setNPassOnTheBus(this.nPassOnTheBus);

        System.out.print("\nPassengers on the bus at arr quay " + this.nPassOnTheBus);
    }

    /**
     *
     */

    public synchronized void resetArrivalTermTransfQuay() throws MemException {
        System.out.print("\nresetArrivalTermTransfQuay");
        this.waitingLine = new MemFIFO<>(new Passenger [SimulationParameters.N_PASS_PER_FLIGHT]);  // FIFO instantiation
        this.nPassAllowedToEnter = 0;
        this.nPassOnTheBus = 0;
        this.allowBoardBus = false;
        this.allPassDead = false;
        this.nFlights += 1;

    }

    /* ************************************************* Setters ******************************************************/

    /**
     *
     */

    public synchronized void setNoPassAtAirport() {
        this.allPassDead = true;
        this.busDriverStop = true;
    }


    public synchronized void setEndDay(){
        this.endDay = true;
    }
}
