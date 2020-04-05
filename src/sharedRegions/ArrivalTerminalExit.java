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
        deadPassCounter = new Counter(SimulationParameters.N_PASS_PER_FLIGHT);
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

        // increment the number of passengers that wants to leave the airport
        boolean isLastPass = this.deadPassCounter.increaseCounter();

        System.out.print("\npass " + passenger.getPassengerID() + " in goHome, npass: " + this.deadPassCounter.getValue());

        System.out.print("\nexitPassenger");

        if(isLastPass) {

            System.out.print("\npass " + passenger.getPassengerID() + " last in goHome, npass: " + this.deadPassCounter.getValue());
            System.out.print("\nsetAllLastWakeUp");

            // if the plane's hold is empty, the last passenger to want to leave
            // wakes up all the passengers
            System.out.print("\nwakeAllPassengers");
            wakeAllPassengers();
            System.out.print("\nnotifyAllPassExited");
            arrivLounge.notifyAllPassExited();

            System.out.print("\nthis.arrivLounge.getCurrentFlight() == SimulationParameters.N_FLIGHTS " + (this.arrivLounge.getCurrentFlight() == SimulationParameters.N_FLIGHTS - 1));

        } else {
            System.out.print("\npass " + passenger.getPassengerID() + " NOT last in goHome, npass: " + this.deadPassCounter.getValue());

            // the passengers that are not the last ones to want to leave the airport, need to wait for the last one to
            // notify them so they can leave
            while (this.deadPassCounter.getValue() < SimulationParameters.N_PASS_PER_FLIGHT) {

                System.out.print("\npass " + passenger.getPassengerID() + " NOT last in goHome, sleep");

                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.print("\npass " + passenger.getPassengerID() + " NOT last in goHome, wake up");
            }
        }

        //this.repos.passengerExit(passenger.getPassengerID());
    }

    public synchronized void notifyFromPrepareNextLeg(){
        System.out.print("\nnotifyFromPrepareNextLeg");
        notifyAll();
    }

    public synchronized void resetArrivalTerminalExit(){
        System.out.print("\nresetArrivalTerminalExit");

        deadPassCounter.reset();
    }

    /* ************************************************* Getters ******************************************************/

    public synchronized void wakeAllPassengers(){
        System.out.print("\nwakeAllPassengers1");
        notifyAll();
        System.out.print("\nwakeAllPassengers2");
        wakedep();
        System.out.print("\nwakeAllPassengers3");
    }
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
