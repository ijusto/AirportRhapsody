package sharedRegions;

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

    /*
     *
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

    public void setDepartureTerminalRef(DepartureTerminalEntrance departureTerm){
        this.departureTerm = departureTerm;
    }

    public void exitPassenger(){
        this.termPass += 1;
        if( (this.termPass + this.departureTerm.getTermPass()) == SimulationParameters.N * SimulationParameters.K){
            this.arrivLounge.setExistsPassengers(false);
            this.arrivalQuay.setExistsPassengers(false);
        }
    }

    public int getTermPass(){
        return this.termPass;
    }

    /**
     *  Operation of going home (raised by the Passenger). <p> functionality: change state of entities.Passenger to EXITING_THE_ARRIVAL_TERMINAL
     *
     */
}
