package sharedRegions;

import commonInfrastructures.MemException;
import commonInfrastructures.MemFIFO;
import entities.BusDriver;
import entities.BusDriverStates;
import entities.Passenger;
import entities.PassengerStates;
import genclass.GenericIO;
import main.SimulationParameters;

/**
 *   ...
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class ArrivalTermTransfQuay {

    /*
     *    FIFO of passengers that want to enter the bus.
     */

    private MemFIFO<Passenger> waitingPass;

    /*
     *
     */

    private int nPassOnTheBus;

    /*
     *
     */

    private int aboutToEnter;

    /*
     *
     */

    private int nWaitingPass;

    /*
     *
     */

    private boolean allowBoardBus;

    /*
     *
     */

    private boolean existsPassengers;

    /*
     *   General Repository of Information.
     */

    private GenReposInfo repos;

    /**
     *   Instantiation of the Arrival Terminal Transfer Quay.
     *
     *     @param repos general repository of information
     */

    public ArrivalTermTransfQuay(GenReposInfo repos) throws MemException {
        this.repos = repos;
        this.waitingPass = new MemFIFO<>(new Passenger [SimulationParameters.N_PASS_PER_FLIGHT]);  // FIFO instantiation
        this.nPassOnTheBus = 0;
        this.allowBoardBus = false;
        this.existsPassengers = true;
        this.nWaitingPass = 0;
        this.aboutToEnter = 0;
    }

    /* ************************************************Passenger***************************************************** */

    /**
     *   Operation of taking a Bus (raised by the Passenger).
     */

    public synchronized void takeABus() {
        Passenger passenger = (Passenger) Thread.currentThread();

        assert(passenger.getSt() == PassengerStates.AT_THE_DISEMBARKING_ZONE);
        passenger.setSt(PassengerStates.AT_THE_ARRIVAL_TRANSFER_TERMINAL);
        repos.updatePassengerState(passenger.getID(), PassengerStates.AT_THE_ARRIVAL_TRANSFER_TERMINAL);

        try {
            waitingPass.write(passenger);
            repos.passengerQueueStateIn(passenger.getID());
        } catch (MemException e) {
            e.printStackTrace();
        }

        this.nWaitingPass += 1;
        if(this.nWaitingPass == SimulationParameters.BUS_CAP){
            notifyAll();  // wake up Bus Driver in parkTheBus()
        }

        /*
         *   Blocked Entity: Passenger
         *   Freeing Entity: Driver
         *   Freeing Method: announcingBusBoarding()
         *   Blocked Entity Reactions: enterTheBus()
        */
        while(true){
            GenericIO.writeString("\nsleep takeABus");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            GenericIO.writeString("\nwake up takeABus");
            if(this.allowBoardBus && this.aboutToEnter < SimulationParameters.BUS_CAP){
                break;
            }
        }

    }

    /**
     *   ... (raised by the Passenger).
     */

    public synchronized void enterTheBus(){

        Passenger passenger = (Passenger) Thread.currentThread();
        assert(passenger.getSt() == PassengerStates.AT_THE_ARRIVAL_TRANSFER_TERMINAL);
        passenger.setSt(PassengerStates.TERMINAL_TRANSFER);
        repos.updatePassengerState(passenger.getID(),PassengerStates.TERMINAL_TRANSFER);


        try{
            if(this.nPassOnTheBus < SimulationParameters.BUS_CAP) {
                this.waitingPass.read();
                this.nWaitingPass -= 1;
                this.incPassOnTheBus();
                repos.passengerQueueStateOut(passenger.getID());
                repos.busSeatStateIn(passenger.getID());
                this.aboutToEnter += 1;
                if(this.nPassOnTheBus == SimulationParameters.BUS_CAP || this.nWaitingPass == 0){
                    notifyAll();  // wake up Bus driver in announcingBusBoarding()
                }
            }
        } catch (MemException e) {
            e.printStackTrace();
        }
    }

    /* ************************************************Bus Driver**************************************************** */

    /**
     *   Operation of checking if the passengers all left and the day ended (raised by the BusDriver).
     *
     *     @return <li> 'F', if end of day </li>
     *             <li> 'R', otherwise </li>
     */

    public synchronized char hasDaysWorkEnded(){

        BusDriver busDriver = (BusDriver) Thread.currentThread();
        assert(busDriver.getStat() == BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL);

        if(this.existsPassengers){
            return 'R';
        }

        return 'F';
    }

    /**
     *   ... (raised by the BusDriver).
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
        repos.updateBusDriverState(BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL);
        this.nPassOnTheBus = 0;

        GenericIO.writeString("\nnWaitingPass parkTheBus " + this.nWaitingPass);

        GenericIO.writeString("\nsleep parkTheBus");
        while(this.nWaitingPass == 0 && this.existsPassengers){
            try {
                wait(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        GenericIO.writeString("\nwake up parkTheBus");
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
        notifyAll();  // wake up Passengers in takeABus()



        while(!(this.nPassOnTheBus == SimulationParameters.BUS_CAP
                || (this.nPassOnTheBus < SimulationParameters.BUS_CAP && this.nPassOnTheBus > 0
                && this.nWaitingPass == 0))) {
            GenericIO.writeString("\nsleep announcingBusBoarding");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            GenericIO.writeString("\nwake up announcingBusBoarding");
        }

        this.allowBoardBus = false;
        this.aboutToEnter = 0;
        busDriver.setNPassOnTheBus(this.nPassOnTheBus);
        GenericIO.writeString("\nPassengers on the bus at arr quay " + this.nPassOnTheBus);
    }

    public void resetArrivalTermTransfQuay() throws MemException {
        this.waitingPass = new MemFIFO<>(new Passenger [SimulationParameters.N_PASS_PER_FLIGHT]);  // FIFO instantiation
        this.nPassOnTheBus = 0;
        this.allowBoardBus = false;
        this.existsPassengers = true;
        this.nWaitingPass = 0;
        this.aboutToEnter = 0;
    }

    /* ******************************************** Getters and Setters ***********************************************/

    /*
     *
     */

    public void setNoPassAtAirport() {
        this.existsPassengers = false;
    }

    public void incPassOnTheBus(){
        this.nPassOnTheBus += 1;
    }

    public void decPassOnTheBus(){
        this.nPassOnTheBus -= 1;
    }

}
