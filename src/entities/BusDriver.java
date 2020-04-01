package entities;
import genclass.GenericIO;
import sharedRegions.ArrivalTermTransfQuay;
import sharedRegions.DepartureTermTransfQuay;
import sharedRegions.GenReposInfo;

/**
 * ...
 *
 * @author Inês Justo
 * @author Miguel Lopes
 */

public class BusDriver extends Thread {

    /**
     *  General repository of information.
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
     *   Number os passengers on the bus.
     *
     *    @serialField nPass
     */

    private int nPassOnTheBus;

    /**
     *  Instantiation of the thread BusDriver.
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
        this.nPassOnTheBus = 0;
        this.repos = repos;
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
        GenericIO.writeString("\nENDED BUS DRIVER.");
    }

    /**
     *
     */

    public synchronized void goToArrivalTerminal(){

        assert(this.getStat() == BusDriverStates.PARKING_AT_THE_DEPARTURE_TERMINAL);
        this.setStat(BusDriverStates.DRIVING_BACKWARD);
        repos.updateBusDriverState(BusDriverStates.DRIVING_BACKWARD);
    }

    /**
     *
     */

    public synchronized void goToDepartureTerminal(){

        assert(this.getStat() == BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL);
        this.setStat(BusDriverStates.DRIVING_FORWARD);
        repos.updateBusDriverState(BusDriverStates.DRIVING_FORWARD);

    }

    /* ************************************************* Getters ******************************************************/

    /**
     *   State of the BusDriver.
     *
     *    @return Stat
     */

    public BusDriverStates getStat() {
        return Stat;
    }


    /**
     *   Number os passengers on the bus.
     *
     *    @return nPassOnTheBus
     */

    public int getNPassOnTheBus() {
        return this.nPassOnTheBus;
    }

    /* ************************************************* Setters ******************************************************/

    /**
     *   ...
     *
     *    @param stat Bus Driver State.
     */

    public void setStat(BusDriverStates stat) {
        Stat = stat;
    }

    /**
     *   ...
     *
     *    @param nPassOnTheBus Number os passengers on the bus.
     */

    public void setNPassOnTheBus(int nPassOnTheBus) {
        this.nPassOnTheBus = nPassOnTheBus;
    }

}
