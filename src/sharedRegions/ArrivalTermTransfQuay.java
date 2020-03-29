package sharedRegions;

import commonInfrastructures.MemException;
import commonInfrastructures.MemFIFO;
import entities.BusDriver;
import entities.BusDriverStates;
import entities.Passenger;
import entities.PassengerStates;
import main.SimulationParameters;

/**
 * ...
 *
 * @author Inês Justo
 * @author Miguel Lopes
 */

public class ArrivalTermTransfQuay {

    /*
     *    FIFO of passengers that want to enter the bus
     */

    private MemFIFO<Passenger> waitingPass;

    /*
     *
     */

    private int nPassOnTheBus;

    /*
     *
     */

    private DepartureTermTransfQuay departureQuay;

    /*
     *
     */
    private boolean boardBus;

    /*
     *
     */
    private boolean existsPassengers;

    /*
     *
     */
    private GenReposInfo repos;

    /*
     *
     */

    public ArrivalTermTransfQuay(GenReposInfo repos) throws MemException {
        this.repos = repos;
        this.waitingPass = new MemFIFO<>(new Passenger [SimulationParameters.BUS_CAP]);        // FIFO instantiation
        this.nPassOnTheBus = 0;
        this.boardBus = false;
        this.existsPassengers = true;
    }

    /* ************************************************Passenger***************************************************** */

    /**
     *  Operation of taking a Bus (raised by the Passenger). <p> functionality: change state of entities.Passenger to AT_THE_ARRIVAL_TRANSFER_TERMINAL
     *
     */

    public synchronized void takeABus() {
        Passenger passenger = (Passenger) Thread.currentThread();

        assert(passenger.getSt() == PassengerStates.AT_THE_DISEMBARKING_ZONE);
        passenger.setSt(PassengerStates.AT_THE_ARRIVAL_TRANSFER_TERMINAL);

        try {
            waitingPass.write(passenger);
        } catch (MemException e) {
            e.printStackTrace();
        }

        if(waitingPass.isFull()){
            notifyAll();  // wake up Bus Driver in parkTheBus()
        }

        /*
          Blocked Entity: Passenger
          Freeing Entity: Driver
          Freeing Method: announcingBusBoarding()
          Blocked Entity Reactions: enterTheBus()
        */
        while(!this.boardBus){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     *  ... (raised by the Passenger).
     *
     */

    public void enterTheBus(){

        Passenger passenger = (Passenger) Thread.currentThread();
        assert(passenger.getSt() == PassengerStates.AT_THE_ARRIVAL_TRANSFER_TERMINAL);
        passenger.setSt(PassengerStates.TERMINAL_TRANSFER);

        try{
            this.waitingPass.read();
            this.nPassOnTheBus += 1;
        } catch (MemException e) {
            notifyAll();  // wake up Bus driver in announcingBusBoarding()
        }
    }

    /* ************************************************Bus Driver**************************************************** */

    /**
     *  ... (raised by the BusDriver).
     *
     */

    public char hasDaysWorkEnded(){

        BusDriver busDriver = (BusDriver) Thread.currentThread();
        assert(busDriver.getStat() == BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL);

        /* TODO: if(!this.existsPass) wake up the bus driver*/

        return 'F';
    }

    /**
     *  ... (raised by the BusDriver).
     *
     */

    public void parkTheBus(){
        /*
            Blocked Entity: Driver
        */

        BusDriver busDriver = (BusDriver) Thread.currentThread();
        assert(busDriver.getStat() == BusDriverStates.DRIVING_BACKWARD);
        busDriver.setStat(BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL);

        /* TODO: if(!this.existsPass) wake up the bus driver*/

        /*
            1) Freeing Entity: Passenger
            1) Freeing Method: takeABus()
            1) Freeing Condition: place in waiting queue = bus capacity
            1) Blocked Entity Reactions: announcingBusBoarding()
            2) Freeing Entity: Driver
            2) Freeing Method: time
            2) Freeing Condition: at least 1 passenger in queue
            2) Blocked Entity Reaction: announcingBusBoarding()
         */

       do {
            try {
                wait(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
       } while(waitingPass.isEmpty());

    }

    /**
     *  ... (raised by the BusDriver).
     *
     */

    public void announcingBusBoarding(){
        /*
          Blocked Entity: Driver
          Freeing Entity: Passenger
          Freeing Method: enterTheBus()
          Freeing Condition: Last passenger in queue
          Blocked Entity Reaction: goToDepartureTerminal()
        */

        BusDriver busDriver = (BusDriver) Thread.currentThread();
        assert(busDriver.getStat() == BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL);

        /* TODO: if(!this.existsPass) wake up the bus driver*/

        this.boardBus = true;
        notifyAll();  // wake up Passengers in takeABus()

        while(!waitingPass.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void setExistsPassengers(boolean existsPassengers) {
        this.existsPassengers = existsPassengers;
    }

    /*
     *
     */

    public void setDepartureQuayRef(DepartureTermTransfQuay departureQuay){
        this.departureQuay = departureQuay;
    }

    public int getNPassOnTheBus() {
        return nPassOnTheBus;
    }


}
