package sharedRegions;

import commonInfrastructures.EntitiesStates;
import entities.Passenger;
import entities.Porter;

/**
 * ...
 *
 * @author Inês Justo
 * @author Miguel Lopes
 */

public class ArrivalTerminalExit {

    /**
     *  Operation of going home (raised by the Passenger). <p> functionality: change state of entities.Passenger to EXITING_THE_ARRIVAL_TERMINAL
     *
     */

    public void goHome(){

        Passenger passenger = (Passenger) Thread.currentThread();
        passenger.setSt(EntitiesStates.EXITING_THE_ARRIVAL_TERMINAL);

    }
}
