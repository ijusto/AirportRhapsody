package sharedRegions;

import entities.BusDriver;
import entities.BusDriverStates;
import entities.Passenger;
import entities.PassengerStates;

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

        do {
            try {
                wait();
                this.nPass -= 1;
                if(this.nPass == 0){
                    notifyAll();  // wake up Bus Driver in parkTheBus()
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while(!this.doesBDLetPassOff());

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
        assert(this.nPass > 0);
        busDriver.setStat(BusDriverStates.PARKING_AT_THE_DEPARTURE_TERMINAL);
        repos.updateBusDriverState(BusDriverStates.PARKING_AT_THE_DEPARTURE_TERMINAL);
        this.nPass = busDriver.getNPass();
        bdLetPassOff();

        notifyAll();  // wake up Passengers in leaveTheBus()

        while(this.nPass > 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
