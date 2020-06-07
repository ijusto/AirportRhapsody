package serverSide.sharedRegions;

import clientSide.entities.Passenger;
import clientSide.PassengerStates;
import clientSide.SimulPar;
import clientSide.sharedRegionsStubs.ArrivalTerminalExitStub;
import clientSide.sharedRegionsStubs.GenReposInfoStub;

/**
 *   Departure Terminal Entrance.
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class DepartureTerminalEntrance {

    /*
     *   General Repository of Information Stub.
     */

    private GenReposInfoStub reposStub;

    /*
     *   Arrival Lounge.
     */

    private ArrivalLounge arrivLounge;

    /*
     *   Arrival Terminal Transfer Quay.
     */

    private ArrivalTermTransfQuay arrivalQuay;

    /*
     *   Arrival Terminal Exit Stub.
     */

    private ArrivalTerminalExitStub arrivalTermStub;

    /**
     *   Signaling the empty state of the plane's hold.
     */

    private boolean phEmpty;

    /**
     *   Instantiation of the Departure Terminal Entrance.
     *
     *     @param reposStub General Repository of Information.
     *     @param arrivLounge Arrival Lounge.
     *     @param arrivalQuay Arrival Terminal Transfer Quay.
     */

    public DepartureTerminalEntrance(/*GenReposInfo reposStub, ArrivalLounge arrivLounge, ArrivalTermTransfQuay arrivalQuay*/){
        this.arrivLounge = arrivLounge;
        this.arrivalQuay = arrivalQuay;
        this.reposStub = reposStub;
        this.phEmpty = false;
    }

    /**
     *   Operation of the passenger of waiting for the last passenger to arrive the Arrival Terminal Exit or the
     *   Departure Terminal Entrance or, if the last, to notify all the others.
     *   If there are still bags at the plane's hold, the passenger waits for the signal of the porter.
     */

    public synchronized void prepareNextLeg(){

        Passenger passenger = (Passenger) Thread.currentThread();
        assert passenger.getSt() == PassengerStates.AT_THE_DEPARTURE_TRANSFER_TERMINAL;
        passenger.setSt(PassengerStates.ENTERING_THE_DEPARTURE_TERMINAL);

        reposStub.updatePassSt(passenger.getPassengerID(), PassengerStates.ENTERING_THE_DEPARTURE_TERMINAL);
        reposStub.printLog();

        // increment the number of passengers that wants to leave the airport
        boolean isLastPass = this.arrivalTermStub.incDecCounter(true);

        if(isLastPass) {

            // if the plane's hold isn't empty, the last passenger to want to leave waits
            while (!this.phEmpty){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // wakes up all the passengers
            wakeAllPassengers();
            //arrivLounge.notifyAllPassExited();

        } else {

            // the passengers that are not the last ones to want to leave the airport, need to wait for the last one to
            // notify them so they can leave or for the notification of the porter (if the last passenger is at the
            // arrival terminal exit) and the plane's hold is not empty.
            while (this.arrivalTermStub.getDeadPassValue() < SimulPar.N_PASS_PER_FLIGHT || !this.phEmpty) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        reposStub.passengerExit(passenger.getPassengerID());
        reposStub.printLog();
    }

    /**
     *   Resets the signal of the porter of the plane's hold empty state.
     */

    public synchronized void resetDepartureTerminalExit(){
        this.phEmpty = false;
    }

    /**
     *   Wakes up all the passengers at the Arrival Terminal Exit and at the Departure Terminal Entrance.
     */

    public synchronized void wakeAllPassengers(){
        notifyAll();
        arrivalTermStub.notifyFromPrepareNextLeg();
    }

    /**
     *   Wakes up all the passengers at the Departure Terminal Entrance.
     */

    public synchronized void notifyFromGoHome(){
        notifyAll();
    }

    /**
     *   Called by Porter in noMoreBagsToCollect to wake up all the passengers that are waiting for the plane's hold to
     *   be out of bags.
     */

    public synchronized void noMoreBags() {
        // wake up Passengers in goCollectABag()
        phEmpty = true;
        notifyAll();
    }

    /* ************************************************* Setters ******************************************************/

    /**
     *   Sets the Arrival Terminal Exit Stub Reference.
     *
     *    @param arrivalTermStub Arrival Terminal Exit Stub.
     */

    public synchronized void setArrivalTerminalRef(ArrivalTerminalExitStub arrivalTermStub){
        this.arrivalTermStub = arrivalTermStub;
    }

}
