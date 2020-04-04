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

        // increment the number of passengers that wants to leave the airport
        boolean isLastPass = this.arrivalTerm.getDeadPassCounter().increaseCounter();

        System.out.print("\npass " + passenger.getPassengerID() + " in prepareNextLeg, npass: " + this.arrivalTerm.getDeadPassCounter().getValue());

        // update logger
        repos.updatePassSt(passenger.getPassengerID(), PassengerStates.ENTERING_THE_DEPARTURE_TERMINAL);

        System.out.print("\nexitPassenger");

        if(isLastPass) {
            System.out.print("\npass " + passenger.getPassengerID() + " last in prepareNextLeg, npass: " + this.arrivalTerm.getDeadPassCounter().getValue());

            // the last passenger wanting to leave waits for the notification of the porter if there are still bags in
            // the plane hold
            while(arrivLounge.ispHoldNotEmpty()){

                System.out.print("\n!arrivLounge.ispHEmpty() " + arrivLounge.ispHoldNotEmpty());
                System.out.print("\npass " + passenger.getPassengerID() + " last in prepareNextLeg, sleep");

                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.print("\npass " + passenger.getPassengerID() + " last in prepareNextLeg, wake up");
            }

            // if the plane's hold is empty, the last passenger to want to leave
            // wakes up all the passengers in the same terminal
            notifyAll();
            // and wakes up the passengers in the other terminal
            arrivalTerm.notifyFromPrepareNextLeg();

            // he changes the boolean of no passengers at the airport to true, so the porter and bus driver know
            this.arrivLounge.setNoPassAtAirport();
            this.arrivalQuay.setNoPassAtAirport();
            // he call's the function that verifies if the current flight was the last and if so, notifies the porter
            this.arrivLounge.dayOver();

            System.out.print("\nthis.arrivLounge.getCurrentFlight() == SimulationParameters.N_FLIGHTS - 1" + (this.arrivLounge.getCurrentFlight() == SimulationParameters.N_FLIGHTS - 1));

        } else {

            System.out.print("\npass " + passenger.getPassengerID() + " NOT last in prepareNextLeg, npass: " + this.arrivalTerm.getDeadPassCounter().getValue());

            // the passengers that are not the last ones to want to leave the airport, need to wait for the last one to
            // notify them so they can leave
            while (this.arrivalTerm.getDeadPassCounter().getValue() < SimulationParameters.N_PASS_PER_FLIGHT) {

                System.out.print("\npass " + passenger.getPassengerID() + " NOT last in prepareNextLeg, sleep");

                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.print("\npass " + passenger.getPassengerID() + " NOT last in prepareNextLeg, wake up");

            }
        }

        this.repos.passengerExit(passenger.getPassengerID());
    }

    /**
     *
     */

    public synchronized void notifyFromGoHome(){
        System.out.print("\nnotifyFromGoHome");
        notifyAll();
    }

    public synchronized void notifyPHEmpty(){
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
    }

    /* ************************************************* Getters ******************************************************/

    /**
     *
     */

    /* ************************************************* Setters ******************************************************/

    /**
     *   ...
     *
     *    @param arrivalTerm Arrival Terminal Exit.
     */

    public synchronized void setArrivalTerminalRef(ArrivalTerminalExit arrivalTerm){
        this.arrivalTerm = arrivalTerm;
    }

}
