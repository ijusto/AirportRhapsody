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

    private int nPass;

    /**
     *   Instantiation of the Departure Terminal Transfer Quay.
     *
     *     @param repos general repository of information.
     */

    public DepartureTermTransfQuay(GenReposInfo repos){
        this.repos = repos;
        this.letPassOff = false;
        this.nPass = -1;
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
        GenericIO.writeString("\nleaveTheBus: " + passenger.getID());
        do {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            GenericIO.writeString("\nDBDLPO: " + this.doesBDLetPassOff());
        } while(!this.doesBDLetPassOff());

        this.setnPass(this.getnPass() - 1);
        GenericIO.writeString("\nnPass: " + this.getnPass());
        if(this.getnPass() == 0){
            notifyAll();  // wake up Bus Driver in parkTheBus()
        }

        repos.busSeatStateOut(passenger.getID());
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
        this.setnPass(busDriver.getNPass());
        GenericIO.writeString("\nBus driver set nPass: " + this.getnPass());
        this.bdLetPassOff();

        notifyAll();  // wake up Passengers in leaveTheBus()

        while(this.getnPass() > 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setnPass(int nPass) {
        this.nPass = nPass;
    }


    public int getnPass() {
        return this.nPass;
    }

    public void bdLetPassOff() {
        this.letPassOff = true;
    }

    public boolean doesBDLetPassOff() {
        return this.letPassOff;
    }

}
