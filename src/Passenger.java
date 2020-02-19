public class Passenger {
    private static final int AT_THE_DISEMBARKING_ZONE = 0;
    private static final int AT_THE_LUGGAGE_COLLECTION_POINT = 1;
    private static final int EXITING_THE_ARRIVAL_TERMINAL = 2;
    private static final int AT_THE_BAGGAGE_RECLAIM_OFFICE = 3;

    private static final int AT_THE_ARRIVAL_TRANSFER_TERMINAL = 4;
    private static final int TERMINAL_TRANSFER = 5;
    private static final int AT_THE_DEPARTURE_TRANSFER_TERMINAL = 6;
    private static final int ENTERING_THE_DEPARTURE_TERMINAL = 7;

    private static final int TRT = 0;  // in transit
    private static final int FDT = 1;  // has this airport as her final destination

    private enum State { AT_THE_DISEMBARKING_ZONE,
                         AT_THE_LUGGAGE_COLLECTION_POINT,
                         EXITING_THE_ARRIVAL_TERMINAL,
                         AT_THE_BAGGAGE_RECLAIM_OFFICE,
                         AT_THE_ARRIVAL_TRANSFER_TERMINAL,
                         TERMINAL_TRANSFER,
                         AT_THE_DEPARTURE_TRANSFER_TERMINAL,
                         ENTERING_THE_DEPARTURE_TERMINAL
                        };

    private enum SituationPassenger {TRT, FDT};

    private State St;  // state of passenger (0 .. 5)
    private SituationPassenger Si;  //situation of passenger (0 .. 5)
    private int NR;  // number of pieces of luggage the passenger - (0 .. 5) carried at the start of her journey
    private int NA;  // number of pieces of luggage the passenger - (0 .. 5) she has presently collected
}
