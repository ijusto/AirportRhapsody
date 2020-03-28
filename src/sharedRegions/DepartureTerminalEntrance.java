package sharedRegions;

import entities.PassengerStates;
import entities.Passenger;

/**
 * ...
 *
 * @author Inês Justo
 * @author Miguel Lopes
 */

public class DepartureTerminalEntrance {

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

    public DepartureTerminalEntrance(ArrivalLounge arrivLounge, ArrivalTermTransfQuay arrivalQuay,GenReposInfo repos){
        this.arrivLounge = arrivLounge;
        this.arrivalQuay = arrivalQuay;
        this.repos = repos;
    }

    /**
     *  ... (raised by the Passenger).
     *
     */

    public void prepareNextLeg(){

        Passenger passenger = (Passenger) Thread.currentThread();
        assert(passenger.getSt() == PassengerStates.AT_THE_DEPARTURE_TRANSFER_TERMINAL);
        passenger.setSt(PassengerStates.ENTERING_THE_DEPARTURE_TERMINAL);

    }


}
