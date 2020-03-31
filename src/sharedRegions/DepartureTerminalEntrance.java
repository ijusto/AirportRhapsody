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
     *   Arrival Terminal Transfer Quay.
     */

    private ArrivalTermTransfQuay arrivalQuay;

    /*
     *   Arrival Terminal Exit.
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

    public boolean exitPassenger(){
        this.termPass += 1;
        GenericIO.writeString("\nExited n pass in dpterm: " + this.termPass);
        if( !((this.termPass + this.arrivalTerm.getTermPass()) < SimulationParameters.N_PASS_PER_FLIGHT)){
            this.arrivLounge.setNoPassAtAirport();
            this.arrivalQuay.setNoPassAtAirport();
            GenericIO.writeString("\nNAO VALE A PENA");
            //System.exit(-1);
            return true;
        }
        return false;
    }

    /**
     *  ... (raised by the Passenger).
     *
     */

    public synchronized void prepareNextLeg(){

        Passenger passenger = (Passenger) Thread.currentThread();
        assert(passenger.getSt() == PassengerStates.AT_THE_DEPARTURE_TRANSFER_TERMINAL);
        passenger.setSt(PassengerStates.ENTERING_THE_DEPARTURE_TERMINAL);
        repos.updatePassengerState(passenger.getPassengerID(), PassengerStates.ENTERING_THE_DEPARTURE_TERMINAL);

        this.repos.passengerExit(passenger.getPassengerID());
        if(this.exitPassenger()){
            notifyAll();
        }
    }

    public void resetDepartureTerminalEntrance(ArrivalLounge arrivLounge, ArrivalTermTransfQuay arrivalQuay){
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
     *   @param arrivalTerm Arrival Terminal Exit
     */

    public void setArrivalTerminalRef(ArrivalTerminalExit arrivalTerm){
        this.arrivalTerm = arrivalTerm;
    }

}
