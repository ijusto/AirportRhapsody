package sharedRegions;

import entities.Passenger;
import entities.PassengerStates;
import main.SimulationParameters;

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

    private DepartureTerminalEntrance departureTerm;

    /*
     *
     */

    private int termPass;

    /**
     *   Instantiation of the Arrival Terminal Exit.
     *
     *     @param arrivLounge ...
     *     @param arrivalQuay ...
     *     @param repos general repository of information
     */

    public ArrivalTerminalExit(ArrivalLounge arrivLounge, ArrivalTermTransfQuay arrivalQuay, GenReposInfo repos){
        this.arrivLounge = arrivLounge;
        this.arrivalQuay = arrivalQuay;
        this.repos = repos;
        this.termPass = 0;
    }

    /*
     *
     */

    public void exitPassenger(){
        this.termPass += 1;
        if( (this.termPass + this.departureTerm.getTermPass()) == SimulationParameters.N_PASS_PER_FLIGHT *
                                                                                    SimulationParameters.N_FLIGHTS){
            this.arrivLounge.setExistsPassengers(false);
            this.arrivalQuay.setExistsPassengers(false);
        }
    }

    /**
     *  ... (raised by the Passenger).
     *
     */
    public synchronized void goHome(){

        Passenger passenger = (Passenger) Thread.currentThread();
        assert(passenger.getSt() == PassengerStates.AT_THE_DISEMBARKING_ZONE ||
                passenger.getSt() == PassengerStates.AT_THE_LUGGAGE_COLLECTION_POINT ||
                passenger.getSt() == PassengerStates.AT_THE_BAGGAGE_RECLAIM_OFFICE);
        passenger.setSt(PassengerStates.EXITING_THE_ARRIVAL_TERMINAL);
        repos.updatePassengerState(passenger.getID(), PassengerStates.EXITING_THE_ARRIVAL_TERMINAL);

        this.exitPassenger();
    }

    /* ******************************************** Getters and Setters ***********************************************/

    /*
     *
     */

    public int getTermPass(){
        return this.termPass;
    }

    /*
     *
     */

    public void setDepartureTerminalRef(DepartureTerminalEntrance departureTerm){
        this.departureTerm = departureTerm;
    }

}
