package sharedRegions;

import entities.Passenger;
import entities.PassengerStates;
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

    /**
     *   Number of passengers of the current flight that left the airport at the Departure Terminal.
     *   If summed up with the number of passengers of the current flight that left the airport at the Arrival
     *   Terminal isn't smaller than the number of passengers per flight, change boolean allPassDead at the Arrival
     *   Lounge and the Arrival Terminal Quay to true;
     */

    private int nPassDead;

    /**
     *   Instantiation of the Departure Terminal Entrance.
     *
     *     @param repos General Repository of Information.
     *     @param arrivLounge Arrival Lounge.
     *     @param arrivalQuay Arrival Terminal Transfer Quay.
     */


    public DepartureTerminalEntrance(GenReposInfo repos, ArrivalLounge arrivLounge, ArrivalTermTransfQuay arrivalQuay){
        this.arrivLounge = arrivLounge;
        this.arrivalQuay = arrivalQuay;
        this.repos = repos;
        this.nPassDead = 0;
    }

    /**
     *
     *    @return <li> true, if all passengers from the current flight left.</li>
     *            <li> false, otherwise.</li>
     */

    public synchronized boolean exitPassenger(){
        System.out.print("\nexitPassenger");
        this.nPassDead += 1;
        System.out.print("\nExited n pass in dpterm: " + this.nPassDead);
        if( !((this.nPassDead + this.arrivalTerm.getNPassDead()) < SimulationParameters.N_PASS_PER_FLIGHT)){
            this.arrivLounge.setNoPassAtAirport();
            this.arrivalQuay.setNoPassAtAirport();
            System.out.print("\nNAO VALE A PENA");
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
        System.out.print("\nprepareNextLeg");

        Passenger passenger = (Passenger) Thread.currentThread();
        assert(passenger.getSt() == PassengerStates.AT_THE_DEPARTURE_TRANSFER_TERMINAL);
        passenger.setSt(PassengerStates.ENTERING_THE_DEPARTURE_TERMINAL);
        repos.updatePassSt(passenger.getPassengerID(), PassengerStates.ENTERING_THE_DEPARTURE_TERMINAL);

        this.repos.passengerExit(passenger.getPassengerID());
        if(this.exitPassenger()){
            System.out.print("\nNOTIFY LAST PREPARE NEXT LEG");
            arrivLounge.wakeUpForNextFlight();
            arrivalQuay.wakeUpForNextFlight();
        }
    }

    /**
     *
     *    @param arrivLounge Arrival Lounge.
     *    @param arrivalQuay Arrival Terminal Transfer Quay.
     */

    public synchronized void resetDepartureTerminalEntrance(ArrivalLounge arrivLounge, ArrivalTermTransfQuay arrivalQuay){
        System.out.print("\nresetDepartureTerminalEntrance");
        this.arrivLounge = arrivLounge;
        this.arrivalQuay = arrivalQuay;
        this.nPassDead = 0;
    }

    /* ************************************************* Setters ******************************************************/

    /**
     *
     *    @return Number of passengers of the current flight that left the airport at the Departure Terminal.
     */

    public int getNPassDead(){
        return this.nPassDead;
    }

    /**
     *   ...
     *
     *    @param arrivalTerm Arrival Terminal Exit.
     */

    public void setArrivalTerminalRef(ArrivalTerminalExit arrivalTerm){
        this.arrivalTerm = arrivalTerm;
    }

}
