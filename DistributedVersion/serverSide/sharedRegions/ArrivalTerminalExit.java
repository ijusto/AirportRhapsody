package serverSide.sharedRegions;

import clientSide.entities.Passenger;
import clientSide.PassengerStates;
import clientSide.SimulPar;

/**
 *   Arrival Terminal Exit.
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class ArrivalTerminalExit {

    private PassengerStates[] statePassengers;

    /**
     *   General Repository of Information.
     */

    private GenReposInfo repos;

    /**
     *   Arrival Lounge.
     */

    private ArrivalLounge arrivLounge;

    /**
     *   Arrival Terminal Transfer Quay.
     */

    private ArrivalTermTransfQuay arrivalQuay;

    /**
     *   Departure Terminal Entrance
     */

    private DepartureTerminalEntrance departureTerm;

    /**
     *   Counter of passengers of the current flight that are at the exit od the Arrival Terminal or at the entrance of
     *   the Departure Terminal.
     */

    private int deadPassCounter;

    /**
     *   Object used for synchronization.
     */

    private static final Object lockDeadPassCounter = new Object();

    /**
     *   Instantiation of the Arrival Terminal Exit.
     *
     *     @param repos General Repository of Information.
     *     @param arrivLounge Arrival Lounge.
     *     @param arrivalQuay Arrival Terminal Transfer Quay.
     */

    public ArrivalTerminalExit(/*GenReposInfo repos, ArrivalLounge arrivLounge, ArrivalTermTransfQuay arrivalQuay*/){
        this.arrivLounge = arrivLounge;
        this.arrivalQuay = arrivalQuay;
        this.repos = repos;
        this.resetDeadPassCounter();
    }

    /**
     *   Operation of the passenger of waiting for the last passenger to arrive the Arrival Terminal Exit or the
     *   Departure Terminal Entrance or, if the last, to notify all the others.
     */

    public synchronized void goHome(int passengerId){
        assert(statePassengers[passengerId] == PassengerStates.AT_THE_DISEMBARKING_ZONE ||
                statePassengers[passengerId] == PassengerStates.AT_THE_LUGGAGE_COLLECTION_POINT ||
                statePassengers[passengerId] == PassengerStates.AT_THE_BAGGAGE_RECLAIM_OFFICE);
        statePassengers[passengerId] = PassengerStates.EXITING_THE_ARRIVAL_TERMINAL;

        repos.updatePassSt(passengerId, PassengerStates.EXITING_THE_ARRIVAL_TERMINAL);
        repos.printLog();

        // increment the number of passengers that wants to leave the airport
        boolean isLastPass = this.incDecCounter(true);

        if(isLastPass) {
            // wakes up all the passengers
            wakeAllPassengers();
            //arrivLounge.notifyAllPassExited();

        } else {

            // the passengers that are not the last ones to want to leave the airport, need to wait for the last one to
            // notify them so they can leave
            while (this.getDeadPassValue() < SimulPar.N_PASS_PER_FLIGHT) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        repos.passengerExit(passengerId);
        repos.printLog();
    }

    /**
     *   Wakes up the passengers waiting in the Arrival Terminal Entrance.
     */

    public synchronized void notifyFromPrepareNextLeg(){
        notifyAll();
    }

    /**
     *   Wakes up all the passengers at the Arrival Terminal Exit and at the Departure Terminal Entrance.
     */

    public synchronized void wakeAllPassengers(){
        notifyAll();
        wakeDepPass();
    }

    /**
     *   Wakes up the passengers waiting in the Departure Terminal Entrance.
     */

    public synchronized void wakeDepPass(){
        departureTerm.notifyFromGoHome();
    }

    /**
     *   Operation of incrementing/decrementing the counter.
     *
     *    @return <li>true, if the value of the counter after the operation is the limit.</li>
     *            <li>false, otherwise.</li>
     */

    public boolean incDecCounter(boolean inc) {
        synchronized (lockDeadPassCounter) {
            if(inc) {
                deadPassCounter++;
            } else {
                deadPassCounter--;
            }
            return deadPassCounter == SimulPar.N_PASS_PER_FLIGHT;
        }
    }

    /**
     *   Sets the value of the counter to zero.
     */

    public void resetDeadPassCounter(){
        synchronized (lockDeadPassCounter) { // Locks on the private Object
            deadPassCounter = 0;
        }
    }

    /**
     *   Resets the counter of passengers that are at the exit od the Arrival Terminal or at the entrance of the
     *   Departure Terminal.
     */

    public synchronized void resetArrivalTerminalExit(){
        this.resetDeadPassCounter();
    }

    /* ************************************************* Getters ******************************************************/

    /**
     *   Getter for the value of the of passengers of the current flight that left the airport at the Arrival Terminal
     *   or at the Departure Terminal.
     *
     *    @return the value of the counter.
     */

    public int getDeadPassValue(){
        synchronized (lockDeadPassCounter) {
            return deadPassCounter;
        }
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
