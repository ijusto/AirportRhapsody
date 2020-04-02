package sharedRegions;

import entities.Passenger;
import entities.PassengerStates;
import main.SimulationParameters;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

    private int allNotified;

    private final Lock lock = new ReentrantLock();
    private final Condition waitAllPassDead = lock.newCondition();
    private final Condition allPassNotified = lock.newCondition();

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
        this.allNotified = 0;
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
        this.nPassDead += 1;

        System.out.print("\npass " + passenger.getPassengerID() + " in goHome, npass: " + this.getAllDeadPass());

        // update logger
        repos.updatePassSt(passenger.getPassengerID(), PassengerStates.EXITING_THE_ARRIVAL_TERMINAL);
        this.repos.passengerExit(passenger.getPassengerID());

        System.out.print("\nexitPassenger");
        System.out.print("\nExited n pass in arrterm: " + this.nPassDead);

        // TODO: allpassDead e allNotified só um cada partilhado entre as duas e fazer wait antes de se incrementar
        if(this.getAllDeadPass() == SimulationParameters.N_PASS_PER_FLIGHT) {
            System.out.print("\npass " + passenger.getPassengerID() + " last in goHome, npass: " + this.getAllDeadPass());
            notifyAll();
            departureTerm.notifyFromGoHome();

            while((this.allNotified + departureTerm.getAllNotified()) < SimulationParameters.N_PASS_PER_FLIGHT - 1) {
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
            arrivLounge.wakeUpForNextFlight();
            arrivalQuay.wakeUpForNextFlight();
        } else {
            System.out.print("\npass " + passenger.getPassengerID() + " NOT last in goHome, npass: " + this.getAllDeadPass());

            while (getAllDeadPass() < SimulationParameters.N_PASS_PER_FLIGHT) {
                System.out.print("\npass " + passenger.getPassengerID() + " NOT last in goHome, wake up");
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print("\npass " + passenger.getPassengerID() + " NOT last in goHome, wake up");
                allNotified += 1;
                notifyAll();
            }
        }

        /*
        if(this.getAllDeadPass() == SimulationParameters.N_PASS_PER_FLIGHT){

            this.arrivLounge.setNoPassAtAirport();
            this.arrivalQuay.setNoPassAtAirport();

            System.out.print("\nNOTIFY LAST GO HOME");

            departureTerm.notifyFromGoHome();
            arrivLounge.wakeUpForNextFlight();
            arrivalQuay.wakeUpForNextFlight();
        } else {

            // if the number of passengers that wants to leave the airport is smaller than the number of passengers per flight
            while (this.getAllDeadPass() < SimulationParameters.N_PASS_PER_FLIGHT) {

                System.out.print("\npass " + passenger.getPassengerID() + " sleep goHome, npass: " + this.getAllDeadPass());
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.print("\npass " + passenger.getPassengerID() + " wake up goHome, npass: " + this.getAllDeadPass());

            }
        }

         */
    }

    public synchronized void notifyFromPrepareNextLeg(){
        System.out.print("\nnotifyFromPrepareNextLeg");
        notifyAll();
    }

    public void signalFromPrepareNextLeg(){
        System.out.print("\nsignalFromPrepareNextLeg");
        waitAllPassDead.signal();
    }

    public synchronized void resetArrivalTerminalExit(ArrivalLounge arrivLounge, ArrivalTermTransfQuay arrivalQuay){
        System.out.print("\nresetArrivalTerminalExit");

        this.arrivLounge = arrivLounge;
        this.arrivalQuay = arrivalQuay;
        this.nPassDead = 0;

        this.allNotified = 0;
    }

    /* ************************************************* Getters ******************************************************/

    /**
     *
     *    @return Number of passengers of the current flight that left the airport at the Arrival Terminal.
     */

    public int getNPassDead(){
        return this.nPassDead;
    }

    public int getAllNotified(){
        return this.allNotified;
    }

    /**
     *
     */

    public int getAllDeadPass(){
        return this.nPassDead + this.departureTerm.getNPassDead();
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
