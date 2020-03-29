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
     *  Occupation state for the waiting queue (passenger id / - (empty))
     *
     *    @serialField Q
     */

    private int Q;

    /**
     *  Occupation state for seat in the bus (passenger id / - (empty))
     *
     *    @serialField S
     */

    private int S;

    ArrivalTermTransfQuay arrivalTerminalQuay;
    DepartureTermTransfQuay departureTransferQuay;

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

    /**
     *  Occupation state for the waiting queue (passenger id / - (empty))
     *
     *    @return Q
     */

    public int getQ() {
        return Q;
    }

    /**
     *  Occupation state for seat in the bus (passenger id / - (empty))
     *
     *    @return S
     */

    public int getS() {
        return S;
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

    public void setQ(int q) {
        Q = q;
    }

    /*
     *
     */

    public void setS(int s) {
        S = s;
    }
}
