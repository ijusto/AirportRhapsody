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
        if( (this.termPass + this.departureTerm.getTermPass()) == SimulationParameters.N_PASS_PER_FLIGHT * SimulationParameters.N_FLIGHTS){
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
    public synchronized void goHome() throws InterruptedException {

        Passenger passenger = (Passenger) Thread.currentThread();
        assert(passenger.getSt() == PassengerStates.AT_THE_DISEMBARKING_ZONE ||
                passenger.getSt() == PassengerStates.AT_THE_LUGGAGE_COLLECTION_POINT ||
                passenger.getSt() == PassengerStates.AT_THE_BAGGAGE_RECLAIM_OFFICE);
        passenger.setSt(PassengerStates.EXITING_THE_ARRIVAL_TERMINAL);

        passenger.join();

        this.exitPassenger();
    }

}
