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
     *    FIFO of passengers that want to enter the bus.
     */

    private MemFIFO<Passenger> waitingLine;

    /**
     *
     */

    private int nPassOnTheBus;

    /**
     *
     */

    private int aboutToEnter;

    /**
     *
     */

    private int nFlights;

    /**
     *
     */

    private int nWaitingPass;

    /**
     *
     */

    private boolean allowBoardBus;

    /**
     *
     */

    private boolean allPassDead;

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
        this.nPassOnTheBus = 0;
        this.allowBoardBus = false;
        this.allPassDead = false;
        this.nWaitingPass = 0;
        this.aboutToEnter = 0;
        this.nFlights = 0;
        this.busDriverStop = false;
    }

    /* ************************************************Passenger***************************************************** */

    /**
     *   Operation of taking a Bus (raised by the Passenger).
     */

    public synchronized void takeABus() {
        System.out.print("\ntakeABus");

        Passenger passenger = (Passenger) Thread.currentThread();

        assert(passenger.getSt() == PassengerStates.AT_THE_DISEMBARKING_ZONE);
        passenger.setSt(PassengerStates.AT_THE_ARRIVAL_TRANSFER_TERMINAL);
        repos.updatePassSt(passenger.getPassengerID(), PassengerStates.AT_THE_ARRIVAL_TRANSFER_TERMINAL);

        try {
            waitingLine.write(passenger);
            repos.pJoinWaitingQueue(passenger.getPassengerID());
        } catch (MemException e) {
            e.printStackTrace();
        }

        this.nWaitingPass += 1;
        //if(this.nWaitingPass == SimulationParameters.BUS_CAP){
            notifyAll();  // wake up Bus Driver in parkTheBus()
            System.out.print("\nnotify parkTheBus at arrival terminal (at least 3 passengers waiting)");
        //}

        /*
         *   Blocked Entity: Passenger
         *   Freeing Entity: Driver
         *   Freeing Method: announcingBusBoarding()
         *   Blocked Entity Reactions: enterTheBus()
        */
        while(!this.allowBoardBus || this.aboutToEnter >= SimulationParameters.BUS_CAP){
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
        this.aboutToEnter += 1;

    }

    /**
     *   ... (raised by the Passenger).
     */

    public synchronized void enterTheBus(){
        System.out.print("\nenterTheBus");

        Passenger passenger = (Passenger) Thread.currentThread();
        assert(passenger.getSt() == PassengerStates.AT_THE_ARRIVAL_TRANSFER_TERMINAL);
        passenger.setSt(PassengerStates.TERMINAL_TRANSFER);
        repos.updatePassSt(passenger.getPassengerID(),PassengerStates.TERMINAL_TRANSFER);


        try{
            if(this.nPassOnTheBus < SimulationParameters.BUS_CAP) {
                this.waitingLine.read();
                this.nWaitingPass -= 1;
                this.nPassOnTheBus += 1;
                repos.pLeftWaitingQueue(passenger.getPassengerID());
                repos.occupyBusSeat(passenger.getPassengerID());

                System.out.print("\nPass " + passenger.getPassengerID() + " entered the bus.");
                if(this.nPassOnTheBus == SimulationParameters.BUS_CAP || this.nWaitingPass == 0){
                    System.out.print("\nLast passenger. notify announcingBusBoarding");
                    notifyAll();  // wake up Bus driver in announcingBusBoarding()
                }
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

        if(this.nFlights == SimulationParameters.N_FLIGHTS - 1 && this.allPassDead){
            return 'F';
        }

        return 'R';
    }

    /**
     *   ... (raised by the BusDriver).
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
        repos.updateBDriverStat(BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL);
        this.nPassOnTheBus = 0;

        System.out.print("\nnWaitingPass parkTheBus " + this.nWaitingPass);

        System.out.print("\nsleep parkTheBus");


        //while ((this.nWaitingPass == 0 && this.workDay < SimulationParameters.N_FLIGHTS)
        //        || (this.busdriverStop && this.workDay < SimulationParameters.N_FLIGHTS - 1)){


        System.out.print("\nthis.nWaitingPass == 0 : " + (this.nWaitingPass == 0));
        System.out.print("\nthis.busdriverStop: " + this.busDriverStop);
        System.out.print("\nthis.workDay == SimulationParameters.N_FLIGHTS - 1: " + (this.nFlights == SimulationParameters.N_FLIGHTS - 1));

        System.out.print("\nthis.waitingPass.isEmpty(): " + this.waitingLine.isEmpty());
        System.out.print("\nthis.allPassDead " + this.allPassDead);
        while(true){// || this.busdriverStop) && this.workDay < SimulationParameters.N_FLIGHTS - 1){// this.existsPassengers){
            try {
                wait(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(this.nFlights == SimulationParameters.N_FLIGHTS - 1 && this.allPassDead){ //!this.busDriverStop){
                break;
            }
            if(!this.waitingLine.isEmpty()){
                System.out.print("\nWTTTTFFFFFFFFFFFFFFFFFFFFFFF");
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
        notifyAll();  // wake up Passengers in takeABus()

        while(!(this.nPassOnTheBus == SimulationParameters.BUS_CAP || this.nWaitingPass == 0)) {
            System.out.print("\nsleep announcingBusBoarding");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.print("\nwake up announcingBusBoarding");
        }

        this.allowBoardBus = false;
        this.aboutToEnter = 0;
        busDriver.setNPassOnTheBus(this.nPassOnTheBus);
        System.out.print("\nPassengers on the bus at arr quay " + this.nPassOnTheBus);
    }

    /**
     *
     */

    public synchronized void resetArrivalTermTransfQuay() throws MemException {
        System.out.print("\nresetArrivalTermTransfQuay");
        System.out.print("\nbusdriver stoped");
        do {
        } while (this.busDriverStop);
        System.out.print("\nbusdriver start");
        this.waitingLine = new MemFIFO<>(new Passenger [SimulationParameters.N_PASS_PER_FLIGHT]);  // FIFO instantiation
        this.nPassOnTheBus = 0;
        this.allowBoardBus = false;
        this.allPassDead = false;
        this.nWaitingPass = 0;
        this.aboutToEnter = 0;
        this.busDriverStop = false;
        this.nFlights += 1;
    }

    /**
     *
     */

    public synchronized void wakeUpForNextFlight(){
        this.busDriverStart();
        notifyAll();
    }

    /* ************************************************* Setters ******************************************************/

    /**
     *
     */

    public void setNoPassAtAirport() {
        this.allPassDead = true;
        this.busDriverStop = true;
    }

    /**
     *
     */

    public synchronized void busDriverStart() {
        this.busDriverStop = false;
    }

}
