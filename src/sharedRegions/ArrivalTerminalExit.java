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

    private Counter allNotified;

    private Counter deadPassCounter;

    private boolean lastWakeUp;

    private boolean lastOfNotLast;


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
        deadPassCounter = new Counter(SimulationParameters.N_PASS_PER_FLIGHT);
        allNotified = new Counter(SimulationParameters.N_PASS_PER_FLIGHT - 1);
        lastWakeUp = false;
        lastOfNotLast = false;
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

        // increment the number of passengers that wants to leave the airport
        boolean isLastPass = this.deadPassCounter.increaseCounter();

        System.out.print("\npass " + passenger.getPassengerID() + " in goHome, npass: " + this.deadPassCounter.getValue());

        // update logger
        repos.updatePassSt(passenger.getPassengerID(), PassengerStates.EXITING_THE_ARRIVAL_TERMINAL);

        System.out.print("\nexitPassenger");
        System.out.print("\nExited n pass in arrterm: " + this.nPassDead);

        if(isLastPass) {
            // he changes the boolean of no passengers at the airport to true, so the porter and bus driver know
            this.arrivLounge.setNoPassAtAirport();
            this.arrivalQuay.setNoPassAtAirport();

            System.out.print("\npass " + passenger.getPassengerID() + " last in goHome, npass: " + this.deadPassCounter.getValue());

            // the last passenger wanting to leave waits for the notification of the porter if there are still bags in
            // the plane hold
            while(arrivLounge.ispHoldNotEmpty()){

                System.out.print("\n!arrivLounge.ispHEmpty() " + arrivLounge.ispHoldNotEmpty());
                System.out.print("\npass " + passenger.getPassengerID() + " last in goHome, sleep");

                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.print("\npass " + passenger.getPassengerID() + " last in goHome, wake up");
            }

            this.setLastWakeUp(true);

            // if the plane's hold is empty, the last passenger to want to leave
            // wakes up all the passengers in the same terminal
            notifyAll();
            // and wakes up the passengers in the other terminal
            departureTerm.notifyFromGoHome();

            // the last passenger wanting to for all the other passengers to leave
            while(this.isLastOfNotLast()){

                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            // wakes up the porter in takeARest
            this.arrivLounge.allPassExited();

            // he call's the function that verifies if the current flight was the last and if so, notifies the porter
            //this.arrivLounge.dayOver();

            System.out.print("\nthis.arrivLounge.getCurrentFlight() == SimulationParameters.N_FLIGHTS " + (this.arrivLounge.getCurrentFlight() == SimulationParameters.N_FLIGHTS - 1));

        } else {
            System.out.print("\npass " + passenger.getPassengerID() + " NOT last in goHome, npass: " + this.deadPassCounter.getValue());

            // the passengers that are not the last ones to want to leave the airport, need to wait for the last one to
            // notify them so they can leave
            while (!this.isLastWakeUp()) {

                System.out.print("\npass " + passenger.getPassengerID() + " NOT last in goHome, sleep");

                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.print("\npass " + passenger.getPassengerID() + " NOT last in goHome, wake up");
            }

            this.setLastOfNotLast(this.allNotified.increaseCounter());
            if(this.isLastOfNotLast()){
                System.out.print("\npass " + passenger.getPassengerID() + " last to wake up");
                // if this passenger is the last to be awaken by the last passenger, he wakes up, the last passenger
                notifyAll();
                departureTerm.notifyFromGoHome();
            }
        }

        this.repos.passengerExit(passenger.getPassengerID());
    }

    public synchronized void notifyFromPrepareNextLeg(){
        System.out.print("\nnotifyFromPrepareNextLeg");
        notifyAll();
    }

    public synchronized void notifyPHEmpty(){
        notifyAll();
    }

    public synchronized void resetArrivalTerminalExit(ArrivalLounge arrivLounge, ArrivalTermTransfQuay arrivalQuay){
        System.out.print("\nresetArrivalTerminalExit");

        this.arrivLounge = arrivLounge;
        this.arrivalQuay = arrivalQuay;
        deadPassCounter = new Counter(SimulationParameters.N_PASS_PER_FLIGHT);
        allNotified = new Counter(SimulationParameters.N_PASS_PER_FLIGHT - 1);
        lastWakeUp = false;
        lastOfNotLast = false;
    }

    /* ************************************************* Getters ******************************************************/

    /**
     *
     *    @return Number of passengers of the current flight that left the airport at the Arrival Terminal.
     */

    public synchronized Counter getDeadPassCounter(){
        return this.deadPassCounter;
    }

    public synchronized Counter getAllNotified(){
        return allNotified;
    }

    public synchronized boolean isLastWakeUp(){
        return this.lastWakeUp;
    }

    public synchronized boolean isLastOfNotLast(){
        return this.lastOfNotLast;
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

    public synchronized void setLastWakeUp(boolean lastWakeUp){
        this.lastWakeUp = lastWakeUp;
    }

    public synchronized void setLastOfNotLast(boolean lastOfNotLast){
        this.lastOfNotLast = lastOfNotLast;
    }

}
