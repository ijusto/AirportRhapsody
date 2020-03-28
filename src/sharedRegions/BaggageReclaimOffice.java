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

    /*
     *
     */
    private GenReposInfo repos;

    /*
     *
     */

    private ArrivalTerminalExit arrivalTerminalExit;

    /*
     *
     */

    public BaggageReclaimOffice(GenReposInfo repos){
        this.repos = repos;
    }

    /**
     *  ... (raised by the Passenger).
     *
     */

    public void goHome(){

        Passenger passenger = (Passenger) Thread.currentThread();
        assert(passenger.getSt() == PassengerStates.AT_THE_BAGGAGE_RECLAIM_OFFICE);
        passenger.setSt(PassengerStates.EXITING_THE_ARRIVAL_TERMINAL);

        this.arrivalTerminalExit.exitPassenger();
    }

    public void setArrivalTerminalRef(ArrivalTerminalExit arrivalTerm){
        this.arrivalTerminalExit = arrivalTerm;
    }
}
