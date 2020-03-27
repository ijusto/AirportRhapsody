package sharedRegions;

import commonInfrastructures.EntitiesStates;
import entities.BusDriver;
import entities.Passenger;

import java.util.Queue;

/**
 * ...
 *
 * @author Inês Justo
 * @author Miguel Lopes
 */

public class DepartureTerminalTransferQuay {

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
        passenger.setSt(EntitiesStates.AT_THE_DEPARTURE_TRANSFER_TERMINAL);

    }



    /**
     *  BusDriver informs the passengers they can leave the bus (raised by the BusDriver).
     *  @param passengerQueue The list of the passengers to inform
     */

    public void parkTheBusAndLetPassOff(Queue<Passenger> passengerQueue) {
        /*
          Blocked Entity: Driver
          Condition: if number of passengers > 0
          Freeing Entity: Passenger
          Freeing Method: leaveTheBus()
          Freeing Condition: Last passenger to exit the bus
          Blocked Entity Reaction: goToArrivalTerminal()
        */

        // called by manager
        // alerts client that the vehicle is ready to be picked up, changes
        // carFixed[customer] to true
        // wakes up customers

        BusDriver busDriver = (BusDriver) Thread.currentThread();
        busDriver.setStat(EntitiesStates.PARKING_AT_THE_DEPARTURE_TERMINAL);
        // ...
        notifyAll(); // ?
    }
}
