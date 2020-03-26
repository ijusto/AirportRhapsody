package entities;

public class BusDriver {
    private static final int PARKING_AT_THE_ARRIVAL_TERMINAL = 0;
    private static final int DRIVING_FORWARD = 1;
    private static final int PARKING_AT_THE_DEPARTURE_TERMINAL = 2;
    private static final int DRIVING_BACKWARD = 3;

    private enum State { PARKING_AT_THE_ARRIVAL_TERMINAL,
                         DRIVING_FORWARD,
                         PARKING_AT_THE_DEPARTURE_TERMINAL,
                         DRIVING_BACKWARD
                        }

    private State Stat;  // state of the driver
    private int Q;  // occupation state for the waiting queue (passenger id / - (empty))
    private int S;  // occupation state for seat in the bus (passenger id / - (empty))

    ArrivalTerminalTransferQuay arrivalTerminalQuay;
    DepartureTransferQuay departureTransferQuay;
    public void run(){
        while(arrivalTerminalQuay.hasDaysWorkEnded() != 'F'){
            arrivalTerminalQuay.announcingBusBoarding();
            arrivalTerminalQuay.goToDepartureTerminal();
            departureTransferQuay.parkTheBusAndLetPassOff();
            departureTransferQuay.goToArrivalTerminal();
            arrivalTerminalQuay.parkTheBus();
        }
    }


}
