package sharedRegions;

import entities.EntitiesStates;
import entities.Passenger;

/**
 * ...
 *
 * @author Inês Justo
 * @author Miguel Lopes
 */

public class DepartureTerminalEntrance {

    /**
     *  ... (raised by the Passenger).
     *
     */

    public void prepareNextLeg(){

        Passenger passenger = (Passenger) Thread.currentThread();
        passenger.setSt(EntitiesStates.ENTERING_THE_DEPARTURE_TERMINAL);

    }


}
