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

        // increment the number of passengers that leave the arrival terminal
        //this.nPassDead += 1;
        boolean isLastPass = this.deadPassCounter.increaseCounter();

        System.out.print("\npass " + passenger.getPassengerID() + " in goHome, npass: " + this.deadPassCounter.getValue());

        // update logger
        repos.updatePassSt(passenger.getPassengerID(), PassengerStates.EXITING_THE_ARRIVAL_TERMINAL);
        this.repos.passengerExit(passenger.getPassengerID());

        System.out.print("\nexitPassenger");
        System.out.print("\nExited n pass in arrterm: " + this.nPassDead);

        // TODO: allpassDead e allNotified só um cada partilhado entre as duas e fazer wait antes de se incrementar
        if(isLastPass) {
            System.out.print("\npass " + passenger.getPassengerID() + " last in goHome, npass: " + this.deadPassCounter.getValue());
            notifyAll();
            departureTerm.notifyFromGoHome();

            while(this.allNotified.getValue() < SimulationParameters.N_PASS_PER_FLIGHT - 1) {
                System.out.print("\npass " + passenger.getPassengerID() + " last in goHome, sleep");
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print("\npass " + passenger.getPassengerID() + " last in goHome, wake up");
            }

            this.arrivLounge.setNoPassAtAirport();
            this.arrivalQuay.setNoPassAtAirport();
        } else {
            System.out.print("\npass " + passenger.getPassengerID() + " NOT last in goHome, npass: " + this.deadPassCounter.getValue());

            while (this.deadPassCounter.getValue() < SimulationParameters.N_PASS_PER_FLIGHT) {
                System.out.print("\npass " + passenger.getPassengerID() + " NOT last in goHome, sleep");
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print("\npass " + passenger.getPassengerID() + " NOT last in goHome, wake up");
                if(this.allNotified.increaseCounter()){
                    System.out.print("\npass " + passenger.getPassengerID() + " last to be notified, notifies last to exit");
                    notifyAll();
                }
            }
        }
    }

    public synchronized void notifyFromPrepareNextLeg(){
        System.out.print("\nnotifyFromPrepareNextLeg");
        notifyAll();
    }

    public synchronized void resetArrivalTerminalExit(ArrivalLounge arrivLounge, ArrivalTermTransfQuay arrivalQuay){
        System.out.print("\nresetArrivalTerminalExit");

        this.arrivLounge = arrivLounge;
        this.arrivalQuay = arrivalQuay;
        deadPassCounter = new Counter(SimulationParameters.N_PASS_PER_FLIGHT);
        allNotified = new Counter(SimulationParameters.N_PASS_PER_FLIGHT - 1);
    }

    /* ************************************************* Getters ******************************************************/

    /**
     *
     *    @return Number of passengers of the current flight that left the airport at the Arrival Terminal.
     */

    public Counter getDeadPassCounter(){
        return this.deadPassCounter;
    }

    public Counter getAllNotified(){
        return allNotified;
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
