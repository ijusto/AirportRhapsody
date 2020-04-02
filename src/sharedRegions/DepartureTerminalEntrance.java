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

        // increment the number of passengers that leave the departure terminal
        boolean isLastPass = this.arrivalTerm.getDeadPassCounter().increaseCounter();

        System.out.print("\npass " + passenger.getPassengerID() + " in prepareNextLeg, npass: " + this.arrivalTerm.getDeadPassCounter().getValue());

        // update logger
        repos.updatePassSt(passenger.getPassengerID(), PassengerStates.ENTERING_THE_DEPARTURE_TERMINAL);
        this.repos.passengerExit(passenger.getPassengerID());

        System.out.print("\nexitPassenger");
        if(isLastPass) {
            System.out.print("\npass " + passenger.getPassengerID() + " last in prepareNextLeg, npass: " + this.arrivalTerm.getDeadPassCounter().getValue());
            notifyAll();
            arrivalTerm.notifyFromPrepareNextLeg();

            while (this.arrivalTerm.getAllNotified().getValue() < SimulationParameters.N_PASS_PER_FLIGHT - 1) {
                System.out.print("\npass " + passenger.getPassengerID() + " last in prepareNextLeg, sleep");
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print("\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAaa");
                System.out.print("\npass " + passenger.getPassengerID() + " last in prepareNextLeg, wake up");
            }

            this.arrivLounge.setNoPassAtAirport();
            this.arrivalQuay.setNoPassAtAirport();
        } else {
            System.out.print("\npass " + passenger.getPassengerID() + " NOT last in prepareNextLeg, npass: " + this.arrivalTerm.getDeadPassCounter().getValue());

            while (this.arrivalTerm.getDeadPassCounter().getValue() < SimulationParameters.N_PASS_PER_FLIGHT) {
                System.out.print("\npass " + passenger.getPassengerID() + " NOT last in prepareNextLeg, sleep");
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print("\npass " + passenger.getPassengerID() + " NOT last in prepareNextLeg, wake up");

                if(this.arrivalTerm.getAllNotified().increaseCounter()){
                    System.out.print("\npass " + passenger.getPassengerID() + " last to be notified, notifies last to exit");
                    notifyAll();
                }
            }
        }
    }

    /**
     *
     */

    public synchronized void notifyFromGoHome(){
        System.out.print("\nnotifyFromGoHome");
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

    public void setArrivalTerminalRef(ArrivalTerminalExit arrivalTerm){
        this.arrivalTerm = arrivalTerm;
    }

}
