package sharedRegions;

/**
 * ...
 *
 * @author Inês Justo
 * @author Miguel Lopes
 */

public class ArrivalTerminalTransferQuay {

    public void enterTheBus(){
        /*
         * params:
         * calling entity: entities.Passenger
         * functionality: change state of entities.Passenger to TERMINAL_TRANSFER
         */
    }

    public char hasDaysWorkEnded(){
        /*
         * params:
         * calling entity: entities.BusDriver
         * functionality: change state of entities.BusDriver to PARKING_AT_THE_ARRIVAL_TERMINAL
         */
        return 'F';
    }

    /*
     * params:
     * calling entity: entities.BusDriver
     * functionality: change state of entities.BusDriver to PARKING_AT_THE_ARRIVAL_TERMINAL
     */
    public void announcingBusBoarding(){
        /*
          Blocked Entity: Driver
          Freeing Entity: Passenger
          Freeing Method: enterTheBus()
          Freeing Condition: Last passenger in queue
          Blocked Entity Reaction: goToDepartureTerminal()
        */
    }

    public void goToDepartureTerminal(){
        /*
         * params:
         * calling entity: entities.BusDriver
         * functionality: change state of entities.BusDriver to DRIVING_FORWARD
         */
    }

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
    }
}
