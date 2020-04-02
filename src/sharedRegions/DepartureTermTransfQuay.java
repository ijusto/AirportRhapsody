package sharedRegions;

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
     *
     */
    private boolean busDoorsOpen;

    /*
     *
     */

    private int nPassOnTheBus;

    /**
     *   Instantiation of the Departure Terminal Transfer Quay.
     *
     *     @param repos general repository of information.
     */

    public DepartureTermTransfQuay(GenReposInfo repos){
        this.repos = repos;
        this.busDoorsOpen = false;
        this.nPassOnTheBus = -1;
    }


    /* ************************************************Passenger***************************************************** */

    /**
     *   ... (raised by the Passenger).
     */

    public synchronized void leaveTheBus(){
        System.out.print("\nleaveTheBus");
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
        System.out.print("\npass that left the bus(id): " + passenger.getPassengerID());

        while(!this.canPassLeaveTheBus()) {
            System.out.print("\nsleep leaveTheBus");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.print("\nwake up leaveTheBus");
            System.out.print("\nDBDLPO: " + this.canPassLeaveTheBus());
        }

        this.nPassOnTheBus -= 1;
        System.out.print("\na pass left, nPass remaining: " + this.getNPassOnTheBus());
        repos.freeBusSeat(passenger.getPassengerID());

        // if the passenger is the last to exit the bus
        if(this.getNPassOnTheBus() == 0){
            // wake up Bus Driver in parkTheBusAndLetPassOff()
            notifyAll();
        }

    }

    /* *************************************************Bus Driver*************************************************** */

    /**
     *   BusDriver informs the passengers they can leave the bus (raised by the BusDriver).
     */

    public synchronized void parkTheBusAndLetPassOff() {
        System.out.print("\nparkTheBusAndLetPassOff");
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
        this.setNPassOnTheBus(busDriver.getNPassOnTheBus());
        System.out.print("\nPassengers on the bus at dep quay " + this.nPassOnTheBus);
        System.out.print("\nBus driver set nPass: " + this.getNPassOnTheBus());

        this.pleaseLeaveTheBus();
        notifyAll();  // wake up Passengers in leaveTheBus()

        while(this.getNPassOnTheBus() != 0) {
            System.out.print("\nsleep parkTheBusAndLetPassOff");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.print("\nwake up parkTheBusAndLetPassOff");
        }

        busDriver.setNPassOnTheBus(this.getNPassOnTheBus());
        this.busDoorsOpen = false;
    }

    /**
     *
     */

    public synchronized void resetDepartureTermTransfQuay(){
        System.out.print("\nresetDepartureTermTransfQuay");
        //while(true){if(this.letPassOff){break;}}
        if(this.busDoorsOpen){
            System.out.print("\nresetDepartureTermTransfQuay");
            System.out.print("\nthis.getNPassOnTheBus() != 0 " + (this.getNPassOnTheBus() != 0));

            //
            notifyAll();
            this.busDoorsOpen = false;
        }
        System.out.print("\nendresetDepartureTermTransfQuay");
        this.nPassOnTheBus = 0;
    }

    /* ************************************************* Getters ******************************************************/

    /**
     *
     *    @return passOnTheBus
     */

    public int getNPassOnTheBus() {
        return this.nPassOnTheBus;
    }

    /**
     *
     *    @return letPassOff
     */
    public boolean canPassLeaveTheBus() {
        return this.busDoorsOpen;
    }

    /* ************************************************* Setters ******************************************************/

    /**
     *
     *    @param nPassOnTheBus
     */

    public void setNPassOnTheBus(int nPassOnTheBus) {
        this.nPassOnTheBus = nPassOnTheBus;
    }

    /**
     *
     */

    public void pleaseLeaveTheBus() {
        this.busDoorsOpen = true;
    }

}
