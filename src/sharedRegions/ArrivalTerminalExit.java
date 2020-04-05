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
     *   Number of passengers of the current flight that are at the exit od the Arrival Terminal or at the entrance of
     *   the Departure Terminal.
     */

    private Counter deadPassCounter;

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
        deadPassCounter = new Counter(SimulPar.N_PASS_PER_FLIGHT);

        repos.printLog();
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

        // update logger
        repos.updatePassSt(passenger.getPassengerID(), PassengerStates.EXITING_THE_ARRIVAL_TERMINAL);

        // increment the number of passengers that wants to leave the airport
        boolean isLastPass = this.deadPassCounter.increaseCounter();

        if(isLastPass) {
            // wakes up all the passengers
            wakeAllPassengers();
            //arrivLounge.notifyAllPassExited();

        } else {

            // the passengers that are not the last ones to want to leave the airport, need to wait for the last one to
            // notify them so they can leave
            while (this.deadPassCounter.getValue() < SimulPar.N_PASS_PER_FLIGHT) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            repos.printLog();
        }
    }

    /**
     *
     */

    public synchronized void notifyFromPrepareNextLeg(){
        notifyAll();
    }

    /**
     *
     */

    public synchronized void resetArrivalTerminalExit(){
        deadPassCounter.reset();
    }

    /* ************************************************* Getters ******************************************************/

    /**
     *
     */

    public synchronized void wakeAllPassengers(){
        notifyAll();
        wakedep();
    }

    /**
     *
     */

    public synchronized void wakedep(){
        departureTerm.notifyFromGoHome();
    }
    /**
     *
     *    @return Number of passengers of the current flight that left the airport at the Arrival Terminal.
     */

    public synchronized Counter getDeadPassCounter(){
        return this.deadPassCounter;
    }

    /* ************************************************* Setters ******************************************************/

    /**
     *   Sets the Departure Terminal Entrance Reference.
     *
     *    @param departureTerm Departure Terminal Entrance.
     */

    public synchronized void setDepartureTerminalRef(DepartureTerminalEntrance departureTerm){
        this.departureTerm = departureTerm;
    }

}
