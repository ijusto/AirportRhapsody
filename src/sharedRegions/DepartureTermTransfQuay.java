package sharedRegions;

import entities.BusDriverStates;
import entities.PassengerStates;
import entities.BusDriver;
import entities.Passenger;

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

    public DepartureTermTransfQuay(GenReposInfo repos){
        this.repos = repos;
    }

    /**
     *  ... (raised by the Passenger).
     *
     */

    public void leaveTheBus(){
        /*
          Blocked Entity: Passenger
          Freeing Entity: Driver

          Freeing Method: parkTheBusAndLetPassOff()
          Blocked Entity Reactions: leaveTheBus()
        */

        Passenger passenger = (Passenger) Thread.currentThread();
        passenger.setSt(PassengerStates.AT_THE_DEPARTURE_TRANSFER_TERMINAL);

    }

    /**
     *  BusDriver informs the passengers they can leave the bus (raised by the BusDriver).
     *  param passengerQueue The list of the passengers to inform
     */

    public void parkTheBusAndLetPassOff(/*Queue<Passenger> passengerQueue ^add @ in param*/) {
        /*
          Blocked Entity: Driver
          Condition: if number of passengers > 0
          Freeing Entity: Passenger
          Freeing Method: leaveTheBus()
          Freeing Condition: Last passenger to exit the bus
          Blocked Entity Reaction: goToArrivalTerminal()
        */

        BusDriver busDriver = (BusDriver) Thread.currentThread();
        busDriver.setStat(BusDriverStates.PARKING_AT_THE_DEPARTURE_TERMINAL);
        // ...
        notifyAll(); // ?
    }
}
