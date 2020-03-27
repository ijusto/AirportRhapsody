package sharedRegions;

import entities.EntitiesStates;
import entities.Passenger;

/**
 * ...
 *
 * @author Inês Justo
 * @author Miguel Lopes
 */

public class ArrivalTerminalExit {

    /*
     *
     */

    private GenReposInfo repos;

    /*
     *
     */

    private ArrivalLounge arrivLounge;

    /*
     *
     */

    private ArrivalTermTransfQuay arrivalQuay;

    /*
     *
     */

    public ArrivalTerminalExit(ArrivalLounge arrivLounge, ArrivalTermTransfQuay arrivalQuay,GenReposInfo repos){
        this.arrivLounge = arrivLounge;
        this.arrivalQuay = arrivalQuay;
        this.repos = repos;
    }

    /*
     *
     */

    public void setDepartureTerminalRef(DepartureTerminalEntrance departureTerm){

    }

    /**
     *  Operation of going home (raised by the Passenger). <p> functionality: change state of entities.Passenger to EXITING_THE_ARRIVAL_TERMINAL
     *
     */

    public void goHome(){

        Passenger passenger = (Passenger) Thread.currentThread();
        passenger.setSt(EntitiesStates.EXITING_THE_ARRIVAL_TERMINAL);

    }
}
