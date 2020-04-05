package sharedRegions;

import commonInfrastructures.Counter;
import entities.Passenger;
import entities.PassengerStates;
import main.SimulPar;

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

        Passenger passenger = (Passenger) Thread.currentThread();
        assert passenger.getSt() == PassengerStates.AT_THE_DEPARTURE_TRANSFER_TERMINAL;
        passenger.setSt(PassengerStates.ENTERING_THE_DEPARTURE_TERMINAL);

        // update logger
        repos.updatePassSt(passenger.getPassengerID(), PassengerStates.ENTERING_THE_DEPARTURE_TERMINAL);

        // increment the number of passengers that wants to leave the airport
        boolean isLastPass = this.dpc.increaseCounter();

        if(isLastPass) {

            // if the plane's hold is empty, the last passenger to want to leave
            // wakes up all the passengers
            wakeAllPassengers();
            arrivLounge.notifyAllPassExited();

        } else {

            // the passengers that are not the last ones to want to leave the airport, need to wait for the last one to
            // notify them so they can leave
            while (this.dpc.getValue() < SimulPar.N_PASS_PER_FLIGHT) {
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

    public synchronized void notifyFromGoHome(){
        notifyAll();
    }

    public synchronized void wakeAllPassengers(){
        notifyAll();
        arrivalTerm.notifyFromPrepareNextLeg();
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
