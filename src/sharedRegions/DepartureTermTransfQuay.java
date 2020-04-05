package sharedRegions;

import commonInfrastructures.Counter;
import entities.BusDriver;
import entities.BusDriverStates;
import entities.Passenger;
import entities.PassengerStates;

/**
 *   ...
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class DepartureTermTransfQuay {

    /*
     *   General Repository of Information.
     */

    private GenReposInfo repos;

    /*
     *   Signaling the bus driver will to let the passengers enter the bus.
     */
    private boolean busDoorsOpen;

    /*
     *
     */

    private Counter nPassOnTheBus;

    /**
     *   Instantiation of the Departure Terminal Transfer Quay.
     *
     *     @param repos general repository of information.
     */

    public DepartureTermTransfQuay(GenReposInfo repos){
        this.repos = repos;
        this.busDoorsOpen = false;
        this.nPassOnTheBus = new Counter(0, false);
    }


    /* ************************************************Passenger***************************************************** */

    /**
     *   ... (raised by the Passenger).
     */

    public synchronized void leaveTheBus(){
        /*
         *   Blocked Entity: Passenger
         *   Freeing Entity: Driver
         *   Freeing Method: parkTheBusAndLetPassOff()
         *   Blocked Entity Reactions: prepareNextLeg()
        */

        Passenger passenger = (Passenger) Thread.currentThread();
        assert(passenger.getSt() == PassengerStates.TERMINAL_TRANSFER);
        passenger.setSt(PassengerStates.AT_THE_DEPARTURE_TRANSFER_TERMINAL);
        repos.updatePassSt(passenger.getPassengerID(),PassengerStates.AT_THE_DEPARTURE_TRANSFER_TERMINAL);

        while(!this.canPassLeaveTheBus()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        boolean last = this.nPassOnTheBus.incDecCounter();

        repos.freeBusSeat(passenger.getPassengerID());
        repos.printLog();

        // if the passenger is the last to exit the bus
        if(last){
            // wake up Bus Driver in parkTheBusAndLetPassOff()
            notifyAll();
        }
    }

    /* *************************************************Bus Driver*************************************************** */

    /**
     *   BusDriver informs the passengers they can leave the bus (raised by the BusDriver).
     */

    public synchronized void parkTheBusAndLetPassOff() {

        /*
         *   Blocked Entity: Driver
         *   Freeing Entity: Passenger
         *   Freeing Method: leaveTheBus()
         *   Freeing Condition: Last passenger to exit the bus
         *   Blocked Entity Reaction: goToArrivalTerminal()
        */

        BusDriver busDriver = (BusDriver) Thread.currentThread();
        assert(busDriver.getStat() == BusDriverStates.DRIVING_FORWARD);
        busDriver.setStat(BusDriverStates.PARKING_AT_THE_DEPARTURE_TERMINAL);
        repos.updateBDriverStat(BusDriverStates.PARKING_AT_THE_DEPARTURE_TERMINAL);
        repos.printLog();

        this.nPassOnTheBus.setValue(busDriver.getNPassOnTheBus());

        this.pleaseLeaveTheBus();
        notifyAll();  // wake up Passengers in leaveTheBus()

        while(this.nPassOnTheBus.getValue() != 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        busDriver.setNPassOnTheBus(this.nPassOnTheBus.getValue());

        this.busDoorsOpen = false;
    }

    /**
     *
     */

    public synchronized void resetDepartureTermTransfQuay(){

        if(this.busDoorsOpen){
            //
            notifyAll();
            this.busDoorsOpen = false;
        }
        this.nPassOnTheBus.reset();

    }

    /* ************************************************* Getters ******************************************************/

    /**
     *   Signaling the bus driver will to let the passengers enter the bus.
     *
     *    @return letPassOff
     */

    public boolean canPassLeaveTheBus() {
        return this.busDoorsOpen;
    }

    /* ************************************************* Setters ******************************************************/

    /**
     *
     */

    public void pleaseLeaveTheBus() {
        this.busDoorsOpen = true;
    }

}
