package serverSide.sharedRegions;

import clientSide.entities.Passenger;
import clientSide.entities.PassengerStates;
import clientSide.SimulPar;
import clientSide.sharedRegionsStubs.ArrivalLoungeStub;
import clientSide.sharedRegionsStubs.ArrivalTermTransfQuayStub;
import clientSide.sharedRegionsStubs.DepartureTerminalEntranceStub;
import clientSide.sharedRegionsStubs.GenReposInfoStub;

/**
 *   Arrival Terminal Exit.
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class ArrivalTerminalExit {

    private PassengerStates[] statePassengers;

    /**
     *   General Repository of Information Stub.
     */

    private GenReposInfoStub reposStub;

    /**
     *   Departure Terminal Entrance Stub
     */

    private DepartureTerminalEntranceStub departureTermStub;

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
     *     @param reposStub General Repository of Information Stub.
     */

    public ArrivalTerminalExit(GenReposInfoStub reposStub){
        this.reposStub = reposStub;
        this.resetDeadPassCounter();
    }

    /**
     *   Operation of the passenger of waiting for the last passenger to arrive the Arrival Terminal Exit or the
     *   Departure Terminal Entrance or, if the last, to notify all the others.
     */

    public synchronized void goHome(int passengerId){
        Passenger passenger = (Passenger) Thread.currentThread();
        assert(statePassengers[passengerId] == PassengerStates.AT_THE_DISEMBARKING_ZONE ||
                statePassengers[passengerId] == PassengerStates.AT_THE_LUGGAGE_COLLECTION_POINT ||
                statePassengers[passengerId] == PassengerStates.AT_THE_BAGGAGE_RECLAIM_OFFICE);
        statePassengers[passengerId] = PassengerStates.EXITING_THE_ARRIVAL_TERMINAL;

        reposStub.updatePassSt(passengerId, PassengerStates.EXITING_THE_ARRIVAL_TERMINAL);
        reposStub.printLog();

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

        reposStub.passengerExit(passengerId);
        reposStub.printLog();
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
        departureTermStub.notifyFromGoHome();
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
     *   Sets the Departure Terminal Entrance Stub Reference.
     *
     *    @param departureTermStub Departure Terminal Entrance Stub.
     */

    public synchronized void setDepartureTerminalRef(DepartureTerminalEntranceStub departureTermStub){
        this.departureTermStub = departureTermStub;
    }

}
