package sharedRegions;

import commonInfrastructures.Counter;
import commonInfrastructures.MemException;
import commonInfrastructures.MemFIFO;
import entities.BusDriver;
import entities.BusDriverStates;
import entities.Passenger;
import entities.PassengerStates;
import main.SimulPar;

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

    private Counter nPassOnTheBus;

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

    private Counter nPassAllowedToEnter;

    /**
     *
     */

    private boolean endDay;

    /**
     *   Instantiation of the Arrival Terminal Transfer Quay.
     *
     *     @param repos general repository of information
     */

    public ArrivalTermTransfQuay(GenReposInfo repos) throws MemException {
        this.repos = repos;
        this.waitingLine = new MemFIFO<>(new Passenger [SimulPar.N_PASS_PER_FLIGHT]);  // FIFO instantiation
        this.nPassAllowedToEnter = new Counter(SimulPar.N_PASS_PER_FLIGHT, true);
        this.allowBoardBus = false;
        this.nPassOnTheBus = new Counter(SimulPar.BUS_CAP, true);
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

        // the place of this passenger in the waiting queue = bus capacity
        if(waitingLine.getNObjects() == SimulPar.BUS_CAP){
            // wake up Bus Driver in parkTheBus()
            notifyAll();
        }

        while(!this.allowBoardBus || this.nPassAllowedToEnter.getValue() >= SimulPar.BUS_CAP){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.nPassAllowedToEnter.incDecCounter();

        repos.printLog();
    }

    /**
     *   ... (raised by the Passenger).
     */

    public synchronized void enterTheBus(){

        Passenger passenger = (Passenger) Thread.currentThread();
        assert(passenger.getSt() == PassengerStates.AT_THE_ARRIVAL_TRANSFER_TERMINAL);
        assert(this.nPassOnTheBus.getValue() < SimulPar.BUS_CAP);

        passenger.setSt(PassengerStates.TERMINAL_TRANSFER);

        // update logger
        repos.updatePassSt(passenger.getPassengerID(),PassengerStates.TERMINAL_TRANSFER);

        try{
            this.waitingLine.read();

            // update logger
            repos.pLeftWaitingQueue(passenger.getPassengerID());

            boolean last = this.nPassOnTheBus.incDecCounter();

            // last passenger to enter the bus wakes up the bus driver
            if(last || this.waitingLine.isEmpty()){ //this.nWaitingPass == 0){

                // wake up Bus driver in announcingBusBoarding()
                notifyAll();
            }
        } catch (MemException e) {
            e.printStackTrace();
        }

        repos.printLog();
    }

    /* ************************************************Bus Driver**************************************************** */

    /**
     *   Operation of checking if all the flights ended (raised by the BusDriver).
     *
     *     @return <li> 'F', if end of day </li>
     *             <li> 'R', otherwise </li>
     */

    public synchronized char hasDaysWorkEnded(){

        BusDriver busDriver = (BusDriver) Thread.currentThread();
        assert(busDriver.getStat() == BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL);

        // if the last flight arrived and all passengers left the airport, end the bus driver life cycle
        if(this.endDay){

            repos.printLog();
            return 'F';
        }

        repos.printLog();
        return 'R';
    }

    /**
     *   ... (raised by the BusDriver).
     *   The Bus Driver is blocked until
     */

    public synchronized void parkTheBus(){

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

        this.nPassOnTheBus.reset();

        while(waitingLine.getNObjects() != SimulPar.BUS_CAP){ // if false waken up by takeABus()
            try {
                // wait until the departure time has been reached and verify again if there are passengers waiting
                wait(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // if the last flight arrived and all passengers left the airport, stop waiting
            if(this.endDay){
                break;
            }

            // if there are passengers waiting
            if(!this.waitingLine.isEmpty()){
                break;
            }
        }

        repos.printLog();

    }

    /**
     *   ... (raised by the BusDriver).
     */

    public synchronized void announcingBusBoarding(){
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

        while(!(this.nPassOnTheBus.getValue() == SimulPar.BUS_CAP || this.waitingLine.isEmpty())){ //this.nWaitingPass == 0)) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.allowBoardBus = false;
        this.nPassAllowedToEnter.reset();
        busDriver.setNPassOnTheBus(this.nPassOnTheBus.getValue());

        repos.printLog();
    }

    /**
     *
     */

    public synchronized void resetArrivalTermTransfQuay() throws MemException {
        this.waitingLine = new MemFIFO<>(new Passenger [SimulPar.N_PASS_PER_FLIGHT]);  // FIFO instantiation
        this.nPassAllowedToEnter.reset();
        this.nPassOnTheBus.reset();
        this.allowBoardBus = false;
    }

    /* ************************************************* Setters ******************************************************/

    public synchronized void setEndDay(){
        this.endDay = true;
        notifyAll();
    }
}
