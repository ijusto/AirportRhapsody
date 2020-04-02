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
     *  ... (raised by the Passenger).
     *    the passenger is waken up by the operations goHome or prepareNextLeg of the last passenger of each flight to
     *    exit the arrival terminal or to enter the departure terminal
     */

    public synchronized void prepareNextLeg(){
        System.out.print("\nprepareNextLeg");

        Passenger passenger = (Passenger) Thread.currentThread();
        assert passenger.getSt() == PassengerStates.AT_THE_DEPARTURE_TRANSFER_TERMINAL;
        passenger.setSt(PassengerStates.ENTERING_THE_DEPARTURE_TERMINAL);

        // increment the number of passengers that leave the departure terminal
        this.nPassDead += 1;

        int allDead = this.nPassDead + this.arrivalTerm.getNPassDead();
        System.out.print("\npass " + passenger.getPassengerID() + " in prepareNextLeg, npass: " + allDead);

        // update logger
        repos.updatePassSt(passenger.getPassengerID(), PassengerStates.ENTERING_THE_DEPARTURE_TERMINAL);
        this.repos.passengerExit(passenger.getPassengerID());

        System.out.print("\nexitPassenger");




        if(this.nPassDead + this.arrivalTerm.getNPassDead() == SimulationParameters.N_PASS_PER_FLIGHT){
            System.out.print("\nExited n pass in dpterm: " + this.nPassDead);

            this.arrivLounge.setNoPassAtAirport();
            this.arrivalQuay.setNoPassAtAirport();

            System.out.print("\nNOTIFY LAST PREPARE NEXT LEG");

            notifyPassengersInDep();
            arrivalTerm.notifyFromPrepareNextLeg();
            arrivLounge.wakeUpForNextFlight();
            arrivalQuay.wakeUpForNextFlight();
        } else {

            // if the number of passengers that wants to leave the airport is smaller than the number of passengers per flight
            while ((this.nPassDead + this.arrivalTerm.getNPassDead()) < SimulationParameters.N_PASS_PER_FLIGHT) {
                allDead = this.nPassDead + this.arrivalTerm.getNPassDead();
                System.out.print("\npass " + passenger.getPassengerID() + " sleep prepareNextLeg, npass: " + allDead);
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                allDead = this.nPassDead + this.arrivalTerm.getNPassDead();
                System.out.print("\npass " + passenger.getPassengerID() + " wakeup prepareNextLeg, npass: " + allDead);
            }

            // TODO: if i am the last passenger wake up gohome, do the same in gohome to wake up here
            // TODO: fazer notify numa função à parte notifyFromGoHome aqui e notifyFromPrepareNextLeg

        }
    }

    /**
     *
     */

    public synchronized void notifyFromGoHome(){
        notifyAll();
    }

    /**
     *
     */

    public synchronized void notifyPassengersInDep(){
        notifyAll();
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

    /* ************************************************* Getters ******************************************************/

    /**
     *
     *    @return Number of passengers of the current flight that left the airport at the Departure Terminal.
     */

    public int getNPassDead(){
        return this.nPassDead;
    }

    /* ************************************************* Setters ******************************************************/

    /**
     *   ...
     *
     *    @param arrivalTerm Arrival Terminal Exit.
     */

    public void setArrivalTerminalRef(ArrivalTerminalExit arrivalTerm){
        this.arrivalTerm = arrivalTerm;
    }

}
