package sharedRegions;

/**
 * ...
 *
 * @author Inês Justo
 * @author Miguel Lopes
 */

public class DepartureTerminalTransferQuay {
    /*
     * @param
     * calling entity: entities.Passenger
     * functionality: change state of entities.Passenger to AT_THE_DEPARTURE_TRANSFER_TERMINAL
     */
    public void leaveTheBus(){

    }

    public void goToArrivalTerminal(){
        /*
         * params:
         * calling entity: entities.BusDriver
         * functionality: change state of entities.BusDriver to DRIVING_BACKWARD
         */
    }

    /*
     * @param
     * calling entity: entities.BusDriver
     * functionality: change state of entities.BusDriver to PARKING_AT_THE_DEPARTURE_TERMINAL
     */
    public void parkTheBusAndLetPassOff(){

    }
}
