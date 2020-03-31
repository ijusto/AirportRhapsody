package sharedRegions;

import entities.Passenger;
import entities.PassengerStates;
import genclass.GenericIO;
import main.SimulationParameters;

/**
 *   ...
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class ArrivalTerminalExit {

    /*
     *   General Repository of Information.
     */

    private GenReposInfo repos;

    /*
     *   Arrival Lounge.
     */

    private ArrivalLounge arrivLounge;

    /*
     *   Arrival Terminal Transfer Quay.
     */

    private ArrivalTermTransfQuay arrivalQuay;

    /*
     *   Departure Terminal Entrance
     */

    private DepartureTerminalEntrance departureTerm;

    /*
     *
     */

    private int termPass;

    /**
     *   Instantiation of the Arrival Terminal Exit.
     *
     *     @param arrivLounge Arrival Lounge.
     *     @param arrivalQuay Arrival Terminal Transfer Quay.
     *     @param repos General Repository of Information.
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

    public boolean exitPassenger(){
        //GenericIO.writeString("\nNAO POSSO MAIS");
        this.termPass += 1;
        GenericIO.writeString("\nExited n pass in arrterm: " + this.termPass);
        if( !((this.termPass + this.departureTerm.getTermPass()) < SimulationParameters.N_PASS_PER_FLIGHT)){
            this.arrivLounge.setNoPassAtAirport();
            this.arrivalQuay.setNoPassAtAirport();
            GenericIO.writeString("\nMESMO QUE TU TENTES");
            //System.exit(-1);
            return true;
        }
        return false;
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
        repos.updatePassengerState(passenger.getPassengerID(), PassengerStates.EXITING_THE_ARRIVAL_TERMINAL);

        this.repos.passengerExit(passenger.getPassengerID());
        if(this.exitPassenger()){
            notifyAll();
        }
    }

    public void resetArrivalTerminalExit(ArrivalLounge arrivLounge, ArrivalTermTransfQuay arrivalQuay){
        this.arrivLounge = arrivLounge;
        this.arrivalQuay = arrivalQuay;
        this.termPass = 0;
    }

    /* ******************************************** Getters and Setters ***********************************************/

    /*
     *
     */

    public int getTermPass(){
        return this.termPass;
    }

    /**
     *   ...
     *
     *   @param departureTerm Departure Terminal Entrance.
     */

    public void setDepartureTerminalRef(DepartureTerminalEntrance departureTerm){
        this.departureTerm = departureTerm;
    }


}
