package sharedRegions;

import entities.PassengerStates;
import entities.Passenger;
import main.SimulationParameters;

/**
 * ...
 *
 * @author Inês Justo
 * @author Miguel Lopes
 */

public class DepartureTerminalEntrance {

    /*
     *   General Repository of Information.
     */

    private GenReposInfo repos;

    /*
     *   Arrival Lounge.
     */

    private ArrivalLounge arrivLounge;

    /*
     *
     */

    private ArrivalTermTransfQuay arrivalQuay;

    /*
     *
     */

    private ArrivalTerminalExit arrivalTerm;

    /*
     *
     */

    private int termPass;

    /**
     *   Instantiation of the Departure Terminal Entrance.
     *
     *     @param arrivLounge ....
     *     @param arrivalQuay ...
     *     @param repos general repository of information
     */


    public DepartureTerminalEntrance(ArrivalLounge arrivLounge, ArrivalTermTransfQuay arrivalQuay,GenReposInfo repos){
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
        if( (this.termPass + this.arrivalTerm.getTermPass()) == SimulationParameters.N_PASS_PER_FLIGHT * SimulationParameters.N_FLIGHTS){
            this.arrivLounge.setExistsPassengers(false);
            this.arrivalQuay.setExistsPassengers(false);
        }
    }

    /**
     *  ... (raised by the Passenger).
     *
     */

    public void prepareNextLeg(){

        Passenger passenger = (Passenger) Thread.currentThread();
        assert(passenger.getSt() == PassengerStates.AT_THE_DEPARTURE_TRANSFER_TERMINAL);
        passenger.setSt(PassengerStates.ENTERING_THE_DEPARTURE_TERMINAL);
        repos.updatePassengerState(passenger.getID(), PassengerStates.ENTERING_THE_DEPARTURE_TERMINAL);

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

    public void setArrivalTerminalRef(ArrivalTerminalExit arrivalTerm){
        this.arrivalTerm = arrivalTerm;
    }

}
