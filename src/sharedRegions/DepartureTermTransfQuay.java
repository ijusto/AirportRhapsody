package sharedRegions;

import entities.BusDriver;
import entities.BusDriverStates;
import entities.Passenger;
import entities.PassengerStates;

/**
 * ...
 *
 * @author Inês Justo
 * @author Miguel Lopes
 */

public class DepartureTermTransfQuay {

    /*
     *
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

    /*
     *
     */

    public DepartureTermTransfQuay(GenReposInfo repos){
        this.repos = repos;
        this.letPassOff = false;
        this.nPass = busNPass;
    }


    /* ************************************************Passenger***************************************************** */

    /**
     *  ... (raised by the Passenger).
     *
     */

    public void leaveTheBus(){
        /*
          Blocked Entity: Passenger
          Freeing Entity: Driver
          Freeing Method: parkTheBusAndLetPassOff()
          Blocked Entity Reactions: prepareNextLeg()
        */

        Passenger passenger = (Passenger) Thread.currentThread();
        assert(passenger.getSt() == PassengerStates.TERMINAL_TRANSFER);
        passenger.setSt(PassengerStates.AT_THE_DEPARTURE_TRANSFER_TERMINAL);

        nPass -= 1;
        if(nPass == 0){
            notifyAll();  // wake up Bus Driver in parkTheBus()
        }

        while(!this.letPassOff) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /* *************************************************Bus Driver*************************************************** */

    /**
     *  BusDriver informs the passengers they can leave the bus (raised by the BusDriver).
     */

    public void parkTheBusAndLetPassOff() {
        /*
          Blocked Entity: Driver
          Freeing Entity: Passenger
          Freeing Method: leaveTheBus()
          Freeing Condition: Last passenger to exit the bus
          Blocked Entity Reaction: goToArrivalTerminal()
        */

        BusDriver busDriver = (BusDriver) Thread.currentThread();
        assert(nPass > 0);
        busDriver.setStat(BusDriverStates.PARKING_AT_THE_DEPARTURE_TERMINAL);

        this.letPassOff = true;

        notifyAll();

        while(nPass > 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
