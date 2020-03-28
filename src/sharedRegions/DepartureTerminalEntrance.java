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

    private ArrivalTerminalExit arrivalTerm;

    /*
     *
     */

    private int termPass;

    /*
     *
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

    public void setArrivalTerminalRef(ArrivalTerminalExit arrivalTerm){
        this.arrivalTerm = arrivalTerm;
    }

    public void exitPassenger(){
        this.termPass += 1;
        if( (this.termPass + this.arrivalTerm.getTermPass()) == SimulationParameters.N_PASS_PER_FLIGHT * SimulationParameters.N_FLIGHTS){
            this.arrivLounge.setExistsPassengers(false);
            this.arrivalQuay.setExistsPassengers(false);
        }
    }

    public int getTermPass(){
        return this.termPass;
    }

    /**
     *  ... (raised by the Passenger).
     *
     */

    public void prepareNextLeg(){

        Passenger passenger = (Passenger) Thread.currentThread();
        assert(passenger.getSt() == PassengerStates.AT_THE_DEPARTURE_TRANSFER_TERMINAL);
        passenger.setSt(PassengerStates.ENTERING_THE_DEPARTURE_TERMINAL);

        this.exitPassenger();
    }


}
