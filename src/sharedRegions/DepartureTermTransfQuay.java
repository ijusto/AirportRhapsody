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
    private boolean letPassOff;

    /*
     *
     */

    private int passOnTheBus;

    /**
     *   Instantiation of the Departure Terminal Transfer Quay.
     *
     *     @param repos general repository of information.
     */

    public DepartureTermTransfQuay(GenReposInfo repos){
        this.repos = repos;
        this.letPassOff = false;
        this.passOnTheBus = -1;
    }


    /* ************************************************Passenger***************************************************** */

    /**
     *   ... (raised by the Passenger).
     */

    public synchronized void leaveTheBus(){
        /*
         *   Blocked Entity: Passenger
         *   Freeing Entity: Driver
         *   Freeing Method: parkTheBusAndLetPassOff()
         *   Blocked Entity Reactions: prepareNextLeg()
        */

        Passenger passenger = (Passenger) Thread.currentThread();
        assert(passenger.getSt() == PassengerStates.TERMINAL_TRANSFER);
        passenger.setSt(PassengerStates.AT_THE_DEPARTURE_TRANSFER_TERMINAL);
        repos.updatePassengerState(passenger.getID(),PassengerStates.AT_THE_DEPARTURE_TRANSFER_TERMINAL);
        GenericIO.writeString("\npass that left the bus(id): " + passenger.getID());

        while(!this.doesBDLetPassOff()) {
            GenericIO.writeString("\nsleep leaveTheBus");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            GenericIO.writeString("\nwake up leaveTheBus");
            GenericIO.writeString("\nDBDLPO: " + this.doesBDLetPassOff());
        }

        this.passOnTheBus -= 1;
        GenericIO.writeString("\na pass left, nPass remaining: " + this.getPassOnTheBus());
        repos.busSeatStateOut(passenger.getID());
        if(this.getPassOnTheBus() == 0){
            notifyAll();  // wake up Bus Driver in parkTheBusAndLetPassOff()
        }

    }

    /* *************************************************Bus Driver*************************************************** */



    /**
     *   BusDriver informs the passengers they can leave the bus (raised by the BusDriver).
     */

    public synchronized void parkTheBusAndLetPassOff() {
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
        this.setPassOnTheBus(busDriver.getNPassOnTheBus());
        GenericIO.writeString("\nPassengers on the bus at dep quay " + this.passOnTheBus);
        GenericIO.writeString("\nBus driver set nPass: " + this.getPassOnTheBus());

        this.bdLetPassOff();
        notifyAll();  // wake up Passengers in leaveTheBus()

        while(this.getPassOnTheBus() != 0) {
            GenericIO.writeString("\nsleep parkTheBusAndLetPassOff");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            GenericIO.writeString("\nwake up parkTheBusAndLetPassOff");
        }

        busDriver.setNPassOnTheBus(this.getPassOnTheBus());
        this.letPassOff = false;
    }

    public void setPassOnTheBus(int passOnTheBus) {
        this.passOnTheBus = passOnTheBus;
    }


    public int getPassOnTheBus() {
        return this.passOnTheBus;
    }

    public void bdLetPassOff() {
        this.letPassOff = true;
    }

    public boolean doesBDLetPassOff() {
        return this.letPassOff;
    }

    public void resetDepartureTermTransfQuay(){
        this.letPassOff = false;
        this.passOnTheBus = -1;
    }

}
