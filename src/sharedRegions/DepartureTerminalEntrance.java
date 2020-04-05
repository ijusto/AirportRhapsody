package sharedRegions;

import commonInfrastructures.Counter;
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

    private Counter dpc;

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

        // update logger
        repos.updatePassSt(passenger.getPassengerID(), PassengerStates.ENTERING_THE_DEPARTURE_TERMINAL);

        // increment the number of passengers that wants to leave the airport
        boolean isLastPass = this.dpc.increaseCounter();

        System.out.print("\npass " + passenger.getPassengerID() + " in prepareNextLeg, npass: " + this.dpc.getValue());
        System.out.print("\nexitPassenger");

        if(isLastPass) {

            System.out.print("\npass " + passenger.getPassengerID() + " last in prepareNextLeg, npass: " + this.dpc.getValue());

            // if the plane's hold is empty, the last passenger to want to leave
            // wakes up all the passengers
            System.out.print("\nwakeAllPassengers");
            wakeAllPassengers();
            System.out.print("\nnotifyAllPassExited");
            arrivLounge.notifyAllPassExited();

            System.out.print("\nthis.arrivLounge.getCurrentFlight() == SimulationParameters.N_FLIGHTS - 1" + (this.arrivLounge.getCurrentFlight() == SimulationParameters.N_FLIGHTS - 1));

        } else {

            System.out.print("\npass " + passenger.getPassengerID() + " NOT last in prepareNextLeg, npass: " + this.dpc.getValue());

            // the passengers that are not the last ones to want to leave the airport, need to wait for the last one to
            // notify them so they can leave
            while (this.dpc.getValue() < SimulationParameters.N_PASS_PER_FLIGHT) {

                System.out.print("\npass " + passenger.getPassengerID() + " NOT last in prepareNextLeg, sleep");

                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.print("\npass " + passenger.getPassengerID() + " NOT last in prepareNextLeg, wake up");

            }

        }

        //this.repos.passengerExit(passenger.getPassengerID());
    }

    /**
     *
     */

    public synchronized void notifyFromGoHome(){
        System.out.print("\nnotifyFromGoHome");
        notifyAll();
    }

    public synchronized void wakeAllPassengers(){
        System.out.print("\nwakeAllPassengers4");
        notifyAll();
        System.out.print("\nwakeAllPassengers5");
        arrivalTerm.notifyFromPrepareNextLeg();
        System.out.print("\nwakeAllPassengers6");
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
        this.dpc = this.arrivalTerm.getDeadPassCounter();
    }

}
