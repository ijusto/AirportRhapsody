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

    public void announcingBusBoarding(){
        /*
         * params:
         * calling entity: entities.BusDriver
         * functionality: change state of entities.BusDriver to PARKING_AT_THE_ARRIVAL_TERMINAL
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

    }
}
