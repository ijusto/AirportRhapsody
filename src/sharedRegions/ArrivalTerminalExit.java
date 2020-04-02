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

    /**
     *   Number of passengers of the current flight that left the airport at the Arrival Terminal.
     *   If summed up with the number of passengers of the current flight that left the airport at the Departure
     *   Terminal isn't smaller than the number of passengers per flight, change boolean allPassDead at the Arrival
     *   Lounge and the Arrival Terminal Quay to true;
     */

    private int nPassDead;

    /**
     *
     */
    private boolean tellPassToGetOut;

    /**
     *   Instantiation of the Arrival Terminal Exit.
     *
     *     @param repos General Repository of Information.
     *     @param arrivLounge Arrival Lounge.
     *     @param arrivalQuay Arrival Terminal Transfer Quay.
     */

    public ArrivalTerminalExit(GenReposInfo repos, ArrivalLounge arrivLounge, ArrivalTermTransfQuay arrivalQuay){
        this.arrivLounge = arrivLounge;
        this.arrivalQuay = arrivalQuay;
        this.repos = repos;
        this.nPassDead = 0;
        this.tellPassToGetOut = false;
    }

    /**
     *  ... (raised by the Passenger).
     *
     */

    public synchronized void goHome(){
        System.out.print("\ngoHome");

        Passenger passenger = (Passenger) Thread.currentThread();
        assert(passenger.getSt() == PassengerStates.AT_THE_DISEMBARKING_ZONE ||
                passenger.getSt() == PassengerStates.AT_THE_LUGGAGE_COLLECTION_POINT ||
                passenger.getSt() == PassengerStates.AT_THE_BAGGAGE_RECLAIM_OFFICE);
        passenger.setSt(PassengerStates.EXITING_THE_ARRIVAL_TERMINAL);

        // update logger
        repos.updatePassSt(passenger.getPassengerID(), PassengerStates.EXITING_THE_ARRIVAL_TERMINAL);
        this.repos.passengerExit(passenger.getPassengerID());

        System.out.print("\nexitPassenger");

        // increment the number of passengers that leave the arrival terminal
        this.nPassDead += 1;

        System.out.print("\nExited n pass in arrterm: " + this.nPassDead);

        if(this.nPassDead + this.departureTerm.getNPassDead() == SimulationParameters.N_PASS_PER_FLIGHT){
            departureTerm.notifyFromGoHome();

            this.arrivLounge.setNoPassAtAirport();
            this.arrivalQuay.setNoPassAtAirport();

            System.out.print("\nNOTIFY LAST GO HOME");

            arrivLounge.wakeUpForNextFlight();
            arrivalQuay.wakeUpForNextFlight();
        } else {

            // if the number of passengers that wants to leave the airport is smaller than the number of passengers per flight
            while (this.nPassDead + this.departureTerm.getNPassDead() < SimulationParameters.N_PASS_PER_FLIGHT && tellPassToGetOut) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     *
     */

    public synchronized void exitPassenger(){

    }

    public synchronized void notifyFromPrepareNextLeg(){
        this.tellPassToGetOut = true;
        notifyAll();
    }

    public synchronized void resetArrivalTerminalExit(ArrivalLounge arrivLounge, ArrivalTermTransfQuay arrivalQuay){
        System.out.print("\nresetArrivalTerminalExit");

        this.arrivLounge = arrivLounge;
        this.arrivalQuay = arrivalQuay;
        this.nPassDead = 0;
        this.tellPassToGetOut = false;
    }

    /* ************************************************* Getters ******************************************************/

    /**
     *
     *    @return Number of passengers of the current flight that left the airport at the Arrival Terminal.
     */

    public int getNPassDead(){
        return this.nPassDead;
    }

    /* ************************************************* Setters ******************************************************/

    /**
     *   Sets the Departure Terminal Entrance Reference.
     *
     *    @param departureTerm Departure Terminal Entrance.
     */

    public void setDepartureTerminalRef(DepartureTerminalEntrance departureTerm){
        this.departureTerm = departureTerm;
    }

}
