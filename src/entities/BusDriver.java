package entities;
import sharedRegions.ArrivalTermTransfQuay;
import sharedRegions.DepartureTermTransfQuay;
import sharedRegions.GenReposInfo;

/**
 *   Bus driver thread.
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class BusDriver extends Thread {

    /**
     *   General repository of information.
     *
     *    @serialField repos
     */

    GenReposInfo repos;

    /**
     *   Arrival Terminal Transfer Quay.
     *
     *    @serialField arrivalTerminalQuay
     */

    ArrivalTermTransfQuay arrivalTerminalQuay;

    /**
     *   Departure Terminal Transfer Quay.
     *
     *    @serialField departureTransferQuay
     */

    DepartureTermTransfQuay departureTransferQuay;

    /**
     *   State of the driver.
     *
     *    @serialField Stat
     */

    private BusDriverStates Stat;

    /**
     *   Number of passengers on the bus.
     *
     *    @serialField nPass
     */

    private int nPassOnTheBus;

    /**
     *   Instantiation of the thread BusDriver.
     *
     *    @param Stat State of the driver.
     *    @param arrivalTerminalQuay Arrival Terminal Transfer Quay.
     *    @param departureTransferQuay Departure Terminal Transfer Quay.
     *    @param repos General Repository of Information.
     */

    public BusDriver(BusDriverStates Stat, ArrivalTermTransfQuay arrivalTerminalQuay,
                     DepartureTermTransfQuay departureTransferQuay, GenReposInfo repos){
        this.Stat = Stat;
        this.arrivalTerminalQuay = arrivalTerminalQuay;
        this.departureTransferQuay = departureTransferQuay;
        this.repos = repos;
        this.nPassOnTheBus = 0;
    }

    /**
     *   Life cycle of the thread BusDriver.
     */

    @Override
    public void run() {
        while(arrivalTerminalQuay.hasDaysWorkEnded() != 'F'){
            arrivalTerminalQuay.announcingBusBoarding();
            goToDepartureTerminal();
            departureTransferQuay.parkTheBusAndLetPassOff();
            goToArrivalTerminal();
            arrivalTerminalQuay.parkTheBus();
        }
    }

    /**
     *   Operation of changing quay, from the departure terminal quay to the arrival terminal quay.
     */

    public synchronized void goToArrivalTerminal() {
        assert(this.getStat() == BusDriverStates.PARKING_AT_THE_DEPARTURE_TERMINAL);
        this.setStat(BusDriverStates.DRIVING_BACKWARD);
        repos.updateBDriverStat(BusDriverStates.DRIVING_BACKWARD);

        // simulates the bus going from the departure terminal to the arrival terminal
        try {
            sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *   Operation of changing quay, from the arrival terminal quay to the departure terminal quay.
     */

    public synchronized void goToDepartureTerminal(){
        assert(this.getStat() == BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL);
        this.setStat(BusDriverStates.DRIVING_FORWARD);
        repos.updateBDriverStat(BusDriverStates.DRIVING_FORWARD);

        // simulates the bus going from the departure arrival to the departure terminal
        try {
            sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /* ************************************************* Getters ******************************************************/

    /**
     *   Gets the state of the BusDriver.
     *
     *    @return State of the BusDriver.
     */

    public BusDriverStates getStat() {
        return Stat;
    }


    /**
     *   Gets the number os passengers on the bus.
     *
     *    @return number os passengers on the bus.
     */

    public int getNPassOnTheBus() {
        return this.nPassOnTheBus;
    }

    /* ************************************************* Setters ******************************************************/

    /**
     *   Sets the Bus Driver State.
     *
     *    @param stat Bus Driver State.
     */

    public void setStat(BusDriverStates stat) {
        Stat = stat;
    }

    /**
     *   Sets the Number os passengers on the bus.
     *
     *    @param nPassOnTheBus Number os passengers on the bus.
     */

    public void setNPassOnTheBus(int nPassOnTheBus) {
        this.nPassOnTheBus = nPassOnTheBus;
    }

}
