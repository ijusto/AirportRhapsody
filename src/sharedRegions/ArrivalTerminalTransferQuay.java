package sharedRegions;

import commonInfrastructures.EntitiesStates;
import entities.BusDriver;
import entities.Passenger;

/**
 * ...
 *
 * @author Inês Justo
 * @author Miguel Lopes
 */

public class ArrivalTerminalTransferQuay {

    /**
     *  ... (raised by the Passenger).
     *
     */

    public void enterTheBus(){

        Passenger passenger = (Passenger) Thread.currentThread();
        passenger.setSt(EntitiesStates.TERMINAL_TRANSFER);

    }

    /**
     *  ... (raised by the BusDriver).
     *
     */

    public char hasDaysWorkEnded(){

        BusDriver busDriver = (BusDriver) Thread.currentThread();
        busDriver.setStat(EntitiesStates.PARKING_AT_THE_ARRIVAL_TERMINAL);

        return 'F';
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
        busDriver.setStat(EntitiesStates.PARKING_AT_THE_ARRIVAL_TERMINAL);

    }

    /**
     *  ... (raised by the BusDriver).
     *
     */

    public void goToDepartureTerminal(){

        BusDriver busDriver = (BusDriver) Thread.currentThread();
        busDriver.setStat(EntitiesStates.DRIVING_FORWARD);

    }

    /**
     *  ... (raised by the BusDriver).
     *
     */

    public void parkTheBus(){
        /*
          Blocked Entity: Driver
          Freeing Entity: Passenger
          Freeing Method: takeABus()
          Freeing Condition: place in waiting queue = bus capacity
          Blocked Entity Reactions: announcingBusBoarding()

          Freeing Entity: Driver
          Freeing Method: time
          Freeing Condition: at least 1 passenger in queue
          Blocked Entity Reaction: announcingBusBoarding()
        */

        BusDriver busDriver = (BusDriver) Thread.currentThread();
        busDriver.setStat(EntitiesStates.PARKING_AT_THE_ARRIVAL_TERMINAL);

    }
}
