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


    private int allNotified;
    private final Lock lock = new ReentrantLock();
    private final Condition waitAllPassDead = lock.newCondition();
    private final Condition allPassNotified = lock.newCondition();

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
        this.allNotified = 0;
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

        System.out.print("\npass " + passenger.getPassengerID() + " in prepareNextLeg, npass: " + this.getAllDeadPass());

        // update logger
        repos.updatePassSt(passenger.getPassengerID(), PassengerStates.ENTERING_THE_DEPARTURE_TERMINAL);
        this.repos.passengerExit(passenger.getPassengerID());

        System.out.print("\nexitPassenger");
        if(this.getAllDeadPass() == SimulationParameters.N_PASS_PER_FLIGHT) {
            System.out.print("\npass " + passenger.getPassengerID() + " last in prepareNextLeg, npass: " + this.getAllDeadPass());
            notifyAll();
            arrivalTerm.notifyFromPrepareNextLeg();

            while ((this.allNotified + arrivalTerm.getAllNotified()) < SimulationParameters.N_PASS_PER_FLIGHT - 1) {
                System.out.print("\npass " + passenger.getPassengerID() + " last in prepareNextLeg, sleep");
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print("\npass " + passenger.getPassengerID() + " last in prepareNextLeg, wake up");
            }

            this.arrivLounge.setNoPassAtAirport();
            this.arrivalQuay.setNoPassAtAirport();
            arrivLounge.wakeUpForNextFlight();
            arrivalQuay.wakeUpForNextFlight();
        } else {
            System.out.print("\npass " + passenger.getPassengerID() + " NOT last in prepareNextLeg, npass: " + this.getAllDeadPass());

            while (getAllDeadPass() < SimulationParameters.N_PASS_PER_FLIGHT) {
                System.out.print("\npass " + passenger.getPassengerID() + " NOT last in prepareNextLeg, wake up");
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print("\npass " + passenger.getPassengerID() + " NOT last in prepareNextLeg, wake up");
                allNotified += 1;
                notifyAll();
            }
        }

        /*
        if(this.getAllDeadPass() == SimulationParameters.N_PASS_PER_FLIGHT){
            System.out.print("\nExited n pass in dpterm: " + this.nPassDead);

            this.arrivLounge.setNoPassAtAirport();
            this.arrivalQuay.setNoPassAtAirport();

            System.out.print("\nNOTIFY LAST PREPARE NEXT LEG");

            arrivalTerm.notifyFromPrepareNextLeg();
            arrivLounge.wakeUpForNextFlight();
            arrivalQuay.wakeUpForNextFlight();
        } else {

            // if the number of passengers that wants to leave the airport is smaller than the number of passengers per flight
            while (this.getAllDeadPass() < SimulationParameters.N_PASS_PER_FLIGHT) {

                System.out.print("\npass " + passenger.getPassengerID() + " sleep prepareNextLeg, npass: " + this.getAllDeadPass());
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.print("\npass " + passenger.getPassengerID() + " wakeup prepareNextLeg, npass: " + this.getAllDeadPass());
            }
        }
        */
    }

    /**
     *
     */

    public synchronized void notifyFromGoHome(){
        System.out.print("\nnotifyFromGoHome");
        notifyAll();
    }

    public void signalFromGoHome(){
        System.out.print("\nsignalFromPrepareNextLeg");
        waitAllPassDead.signal();
    }

    public synchronized void notifyHere(){
        System.out.print("\nnotifyHere");
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
        this.allNotified = 0;
    }

    /* ************************************************* Getters ******************************************************/

    /**
     *
     *    @return Number of passengers of the current flight that left the airport at the Departure Terminal.
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
        return this.nPassDead + this.arrivalTerm.getNPassDead();
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
