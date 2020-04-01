package sharedRegions;

import entities.BusDriver;
import entities.BusDriverStates;
import entities.Passenger;
import entities.PassengerStates;
import genclass.GenericIO;

/**
 *   ...
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class DepartureTermTransfQuay {

    /*
     *   General Repository of Information.
     */

    private GenReposInfo repos;

    /*
     *
     */
    private boolean busDoorsOpen;

    /*
     *
     */

    private int nPassOnTheBus;

    /**
     *   Instantiation of the Departure Terminal Transfer Quay.
     *
     *     @param repos general repository of information.
     */

    public DepartureTermTransfQuay(GenReposInfo repos){
        this.repos = repos;
        this.busDoorsOpen = false;
        this.nPassOnTheBus = -1;
    }


    /* ************************************************Passenger***************************************************** */

    /**
     *   ... (raised by the Passenger).
     */

    public synchronized void leaveTheBus(){
        GenericIO.writeString("\nleaveTheBus");
        /*
         *   Blocked Entity: Passenger
         *   Freeing Entity: Driver
         *   Freeing Method: parkTheBusAndLetPassOff()
         *   Blocked Entity Reactions: prepareNextLeg()
        */

        Passenger passenger = (Passenger) Thread.currentThread();
        assert(passenger.getSt() == PassengerStates.TERMINAL_TRANSFER);
        passenger.setSt(PassengerStates.AT_THE_DEPARTURE_TRANSFER_TERMINAL);
        repos.updatePassengerState(passenger.getPassengerID(),PassengerStates.AT_THE_DEPARTURE_TRANSFER_TERMINAL);
        GenericIO.writeString("\npass that left the bus(id): " + passenger.getPassengerID());

        while(!this.canPassLeaveTheBus()) {
            GenericIO.writeString("\nsleep leaveTheBus");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            GenericIO.writeString("\nwake up leaveTheBus");
            GenericIO.writeString("\nDBDLPO: " + this.canPassLeaveTheBus());
        }

        this.nPassOnTheBus -= 1;
        GenericIO.writeString("\na pass left, nPass remaining: " + this.getNPassOnTheBus());
        repos.busSeatStateOut(passenger.getPassengerID());
        //if(this.getnPassOnTheBus() == 0){
            notifyAll();  // wake up Bus Driver in parkTheBusAndLetPassOff()
        //}

    }

    /* *************************************************Bus Driver*************************************************** */



    /**
     *   BusDriver informs the passengers they can leave the bus (raised by the BusDriver).
     */

    public synchronized void parkTheBusAndLetPassOff() {
        GenericIO.writeString("\nparkTheBusAndLetPassOff");
        /*
         *   Blocked Entity: Driver
         *   Freeing Entity: Passenger
         *   Freeing Method: leaveTheBus()
         *   Freeing Condition: Last passenger to exit the bus
         *   Blocked Entity Reaction: goToArrivalTerminal()
        */

        BusDriver busDriver = (BusDriver) Thread.currentThread();
        assert(busDriver.getStat() == BusDriverStates.DRIVING_FORWARD);
        busDriver.setStat(BusDriverStates.PARKING_AT_THE_DEPARTURE_TERMINAL);
        repos.updateBusDriverState(BusDriverStates.PARKING_AT_THE_DEPARTURE_TERMINAL);
        this.setNPassOnTheBus(busDriver.getNPassOnTheBus());
        GenericIO.writeString("\nPassengers on the bus at dep quay " + this.nPassOnTheBus);
        GenericIO.writeString("\nBus driver set nPass: " + this.getNPassOnTheBus());

        this.pleaseLeaveTheBus();
        notifyAll();  // wake up Passengers in leaveTheBus()

        while(this.getNPassOnTheBus() != 0) {
            GenericIO.writeString("\nsleep parkTheBusAndLetPassOff");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            GenericIO.writeString("\nwake up parkTheBusAndLetPassOff");
        }

        busDriver.setNPassOnTheBus(this.getNPassOnTheBus());
        this.busDoorsOpen = false;
    }

    /**
     *
     */

    public synchronized void resetDepartureTermTransfQuay(){
        GenericIO.writeString("\nresetDepartureTermTransfQuay");
        //while(true){if(this.letPassOff){break;}}
        this.busDoorsOpen = false;
        this.nPassOnTheBus = -1;
    }

    /* ************************************************* Getters ******************************************************/

    /**
     *
     *    @return passOnTheBus
     */

    public int getNPassOnTheBus() {
        return this.nPassOnTheBus;
    }

    /**
     *
     *    @return letPassOff
     */
    public boolean canPassLeaveTheBus() {
        return this.busDoorsOpen;
    }

    /* ************************************************* Setters ******************************************************/

    /**
     *
     *    @param nPassOnTheBus
     */

    public void setNPassOnTheBus(int nPassOnTheBus) {
        this.nPassOnTheBus = nPassOnTheBus;
    }

    /**
     *
     */

    public void pleaseLeaveTheBus() {
        this.busDoorsOpen = true;
    }

}
