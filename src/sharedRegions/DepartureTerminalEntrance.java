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
     *   Number of passengers of the current flight/shift that left the airport at the Departure Terminal.
     */

    private int nPassDead;

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
        this.nPassDead = 0;
    }

    /*
     *
     */

    public synchronized boolean exitPassenger(){
        GenericIO.writeString("\nexitPassenger");
        this.nPassDead += 1;
        GenericIO.writeString("\nExited n pass in dpterm: " + this.nPassDead);
        if( !((this.nPassDead + this.arrivalTerm.getNPassDead()) < SimulationParameters.N_PASS_PER_FLIGHT)){
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
        GenericIO.writeString("\nprepareNextLeg");

        Passenger passenger = (Passenger) Thread.currentThread();
        assert(passenger.getSt() == PassengerStates.AT_THE_DEPARTURE_TRANSFER_TERMINAL);
        passenger.setSt(PassengerStates.ENTERING_THE_DEPARTURE_TERMINAL);
        repos.updatePassengerState(passenger.getPassengerID(), PassengerStates.ENTERING_THE_DEPARTURE_TERMINAL);

        this.repos.passengerExit(passenger.getPassengerID());
        if(this.exitPassenger()){
            GenericIO.writeString("NOTIFY LAST PREPARE NEXT LEG");
            arrivLounge.wakeUpForNextFlight();
            arrivalQuay.wakeUpForNextFlight();
        }
    }

    public synchronized void resetDepartureTerminalEntrance(ArrivalLounge arrivLounge, ArrivalTermTransfQuay arrivalQuay){
        GenericIO.writeString("\nresetDepartureTerminalEntrance");
        this.arrivLounge = arrivLounge;
        this.arrivalQuay = arrivalQuay;
        this.nPassDead = 0;
    }

    /* ************************************************* Getters ******************************************************/

    /* ************************************************* Setters ******************************************************/

    /*
     *
     */

    public int getNPassDead(){
        return this.nPassDead;
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
