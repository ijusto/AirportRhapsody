package entities;
import sharedRegions.ArrivalTerminalTransferQuay;
import sharedRegions.DepartureTerminalTransferQuay;

public class BusDriver extends Thread {

    private static final int PARKING_AT_THE_ARRIVAL_TERMINAL = 0;
    private static final int DRIVING_FORWARD = 1;
    private static final int PARKING_AT_THE_DEPARTURE_TERMINAL = 2;
    private static final int DRIVING_BACKWARD = 3;

    private enum State { PARKING_AT_THE_ARRIVAL_TERMINAL,
                         DRIVING_FORWARD,
                         PARKING_AT_THE_DEPARTURE_TERMINAL,
                         DRIVING_BACKWARD
                        }

    /**
     *  State of the driver
     *
     *    @serialField Stat
     */

    private State Stat;

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

    ArrivalTerminalTransferQuay arrivalTerminalQuay;
    DepartureTerminalTransferQuay departureTransferQuay;

    /**
     *  Life cycle of the thread BusDriver.
     */

    @Override
    public void run() {
        while(arrivalTerminalQuay.hasDaysWorkEnded() != 'F'){
            arrivalTerminalQuay.announcingBusBoarding();
            arrivalTerminalQuay.goToDepartureTerminal();
            departureTransferQuay.parkTheBusAndLetPassOff();
            departureTransferQuay.goToArrivalTerminal();
            arrivalTerminalQuay.parkTheBus();
        }
    }


}
