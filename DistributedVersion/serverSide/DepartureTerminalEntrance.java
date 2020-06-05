package serverSide;

import clientSide.Passenger;
import clientSide.PassengerStates;
import main.SimulPar;

/**
 *   Departure Terminal Entrance.
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
     *   Signaling the empty state of the plane's hold.
     */

    private boolean phEmpty;

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

        repos.updatePassSt(passenger.getPassengerID(), PassengerStates.ENTERING_THE_DEPARTURE_TERMINAL);
        repos.printLog();

        // increment the number of passengers that wants to leave the airport
        boolean isLastPass = this.arrivalTerm.incDecCounter(true);

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
            while (this.arrivalTerm.getDeadPassValue() < SimulPar.N_PASS_PER_FLIGHT || !this.phEmpty) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        repos.passengerExit(passenger.getPassengerID());
        repos.printLog();
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
        arrivalTerm.notifyFromPrepareNextLeg();
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
     *   Sets the Arrival Terminal Exit Reference.
     *
     *    @param arrivalTerm Arrival Terminal Exit.
     */

    public synchronized void setArrivalTerminalRef(ArrivalTerminalExit arrivalTerm){
        this.arrivalTerm = arrivalTerm;
    }

}
