package sharedRegions;

import entities.PassengerStates;
import entities.Passenger;

/**
 * ...
 *
 * @author Inês Justo
 * @author Miguel Lopes
 */

public class BaggageReclaimOffice {

    /**
     *  ... (raised by the Passenger).
     *
     */

    public void goHome(){

        Passenger passenger = (Passenger) Thread.currentThread();
        passenger.setSt(PassengerStates.EXITING_THE_ARRIVAL_TERMINAL);

    }
}
