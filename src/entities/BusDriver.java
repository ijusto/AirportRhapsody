package entities;
import sharedRegions.ArrivalTermTransfQuay;
import sharedRegions.DepartureTermTransfQuay;

/**
 * ...
 *
 * @author Inês Justo
 * @author Miguel Lopes
 */

public class BusDriver extends Thread {

    /**
     *  State of the driver
     *
     *    @serialField Stat
     */

    private BusDriverStates Stat;

    /**
     *  Number os passengers on the bus.
     *
     *    @serialField nPass
     */

    private int nPass;

    /**
     *  ...
     *
     *    @serialField arrivalTerminalQuay
     */

    ArrivalTermTransfQuay arrivalTerminalQuay;

    /**
     *  ...
     *
     *    @serialField departureTransferQuay
     */

    DepartureTermTransfQuay departureTransferQuay;

    /**
     *  Instantiation of the thread BusDriver.
     *
     *    @param arrivalTerminalQuay ...
     *    @param departureTransferQuay ...
     */

    public BusDriver(BusDriverStates Stat, ArrivalTermTransfQuay arrivalTerminalQuay,
                     DepartureTermTransfQuay departureTransferQuay){
        this.Stat = Stat;
        this.arrivalTerminalQuay = arrivalTerminalQuay;
        this.departureTransferQuay = departureTransferQuay;
        this.nPass = 0;
    }

    /**
     *  Life cycle of the thread BusDriver.
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
     *
     *
     */

    public void goToArrivalTerminal(){

        assert(this.getStat() == BusDriverStates.PARKING_AT_THE_DEPARTURE_TERMINAL);
        this.setStat(BusDriverStates.DRIVING_BACKWARD);

    }

    /**
     *
     *
     */

    public void goToDepartureTerminal(){

        assert(this.getStat() == BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL);
        this.setStat(BusDriverStates.DRIVING_FORWARD);

    }

    /* ******************************************** Getters and Setters ***********************************************/

    /**
     *  State of the BusDriver
     *
     *    @return Stat
     */

    public BusDriverStates getStat() {
        return Stat;
    }

    /*
     *
     */

    public void setStat(BusDriverStates stat) {
        Stat = stat;
    }

    /*
     *
     */

    public void setNPass(int nPass) {
        this.nPass = nPass;
    }

    /*
     *
     */

    public int getNPass() {
        return this.nPass;
    }
}
