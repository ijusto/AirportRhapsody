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

    /*
    private static final int PARKING_AT_THE_ARRIVAL_TERMINAL = 0;
    private static final int DRIVING_FORWARD = 1;
    private static final int PARKING_AT_THE_DEPARTURE_TERMINAL = 2;
    private static final int DRIVING_BACKWARD = 3;

    private enum State { PARKING_AT_THE_ARRIVAL_TERMINAL,
                         DRIVING_FORWARD,
                         PARKING_AT_THE_DEPARTURE_TERMINAL,
                         DRIVING_BACKWARD
                        }
     */

    /**
     *  State of the driver
     *
     *    @serialField Stat
     */

    private EntitiesStates Stat;

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

        this.setStat(EntitiesStates.DRIVING_BACKWARD);

    }

    /**
     *
     *
     */

    public void goToDepartureTerminal(){

        this.setStat(EntitiesStates.DRIVING_FORWARD);

    }

    /**
     *  State of the BusDriver
     *
     *    @return Stat
     */

    public EntitiesStates getStat() {
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


    public void setStat(EntitiesStates stat) {
        Stat = stat;
    }

    public void setQ(int q) {
        Q = q;
    }

    public void setS(int s) {
        S = s;
    }
}
