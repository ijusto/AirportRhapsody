package sharedRegions;
import  entities.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Queue;

/**
 * The General Repository of Information works solely as the place where the visible internal state of the problem
 * is stored. The visible internal state is defined by the set of variables whose value is printed in the logging file.
 * Whenever an entity (porter, passenger, bus driver) executes an operation that changes the values of some of these
 * variables, the fact must be reported so that a new line group is printed in the logging file. The report operation
 * must be atomic, that is, when two or more variables are changed, the report operation must be unique so that the new
 * line group reflects all the changes that have taken place.
 *
 * @author Inês Justo
 * @author Miguel Lopes
 */

public class GenReposInfo {

    private final String[] state = ["ATDZ", "ATLCP", "ETAT", "ATBRO", "ATATT", "TT", "ATDTT", "ETDT", "PATAT", "DF",
                                    "PATDT", "DB"];
    ;
    /**
     *  Flight number
     */

    private int FN;

    /**
     *  Number of pieces of luggage presently at the plane's hold
     */

    private int BN;

    /**
     *  Number of pieces of luggage presently at the conveyors belt
     */

    private int CB;


    /**
     *  Number of pieces of luggage from transit passengers
     */

    private int SR;

    /*
     *   Porters
     */

    private PorterStates porterStates;
    private Queue<Porter> porters;

    /*
     *   BusDrivers
     */
    private BusDriverStates busDriverStates;
    private int busQueue;

    /*
     *   Passengers
     */
    private PassengerStates[] passengerStates;
    private int passengersQueue;

    /*
     *   Arrival Lounge
     */

    /*
     *   Arrival Terminal Exit
     */

    /*
     *   Arrival Terminal Transfer Quay
     */

    /*
     *  Baggage Collection Point
     */

    /*
     *   Baggage Reclaim Office
     */

    /*
     *   Departure Terminal Entrance
     */

    /*
     *   Departure Terminal Transfer Quay
     */

    /*
     *   Temporary Storage Area
     */

    /**
     *
     */

    private String fileName;

    /**
     *
     */

    private PrintWriter printW;

    /**
     *
     */

    public GenReposInfo(String fileName){


        this.fileName = fileName;
    }

    /**
     *  Update flight number after the previous flight is finished
     */

    public synchronized updateFlightNumber(int flight){
        FN = flight + 1;
        printLog();
    }

    /**
     *  Update baggage stored in the cargo hold when porter retrieves the baggage
     */

    public synchronized  updateStoredBaggage(int baggage){
        BN = BN - baggage;
        printLog();
    }

    /**
     *  Update the Porter state
     */
    public synchronized void updatePorterState(PorterStates porterState){
        if(porterStates != porterState){
            porterStates = porterState;
        }
        printLog();
    }

    /**
     *  Update the Passenger State
     */
    public synchronized void updatePassengerState(int id, PassengerStates passengerState){
        if(passengerStates[id] != passengerState){
            passengerState[id] = passengerState;
        }
        printLog();
    }

    /**
     *  Update the Bus Driver State
     */
    public synchronized void updateBusDriverState(BusDriverStates busDriverState){
        if(busDriverStates != busDriverState){
            busDriverStates = busDriverState;
        }
        printLog();
    }

    /**
     *  Update the number of bags in the conveyor belt
     */

    public synchronized  void updateConveyorsBelt(int cb){
        CB = CB + cb;
        printLog();
    }

    /**
     *  Update the queue of passengers
     */

    public synchronized  void passengerQueueStateIn(Passenger passenger){
        passengersQueue++;
        passenger.setSt(PassengerStates.AT_THE_DISEMBARKING_ZONE);
        printLog();
    }

    public synchronized  void passengerQueueStateOut(Passenger passenger){
        passengersQueue--;
        printLog();
    }

    /**
     *  Update of the occupation state of the bus seat
     */
    public synchronized  void busSeatStateIn(Passenger passenger, BusDriver busDriver){
        busQueue++;
        passenger.setSt(PassengerStates.TERMINAL_TRANSFER);
        busDriver.setStat(BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL);
        printLog();
    }

    public synchronized  void busSeatStateOut(Passenger passenger, BusDriver busDriver){
        busQueue--;
        passenger.setSt(PassengerStates.AT_THE_DEPARTURE_TRANSFER_TERMINAL);
        busDriver.setStat(BusDriverStates.PARKING_AT_THE_DEPARTURE_TERMINAL);
        printLog();
    }

    /**
     *  Update the passenger situation (in transit or final)
     */

    public synchronized  void updatePassengerSituation(){

        printLog();
    }

    /**
     *  Update the passengers luggage at the start of the journey
     */

    public synchronized  void numberOfPassangerLuggage(){

        printLog();
    }

    /**
     *  Update the number of luggage a passenger collected
     */

    public synchronized  void BaggageCollected(){

        printLog();
    }

    /**
     *  Number of bags carried by a passanger at the end of the jorney
     */

    public synchronized  void finalPassangeBags(){

        printLog();
    }


    private void printLog(){
        /*
        String log =
        String.format("\t\tAIRPORT RHAPSODY - Description of the internal state of the problem" +
        "\nPLANE\tPORTER\t\t\tDRIVER" +
        "\n%3d %3d\t%3d %3d %3d\t\t%3d %3d %3d %3d %3d %3d %3d %3d %3d %3d" +
        "\n\t\t\t\tPASSENGERS" +
        "\n%3d %3d %3d %3d %3d %3d %3d %3d %3d %3d %3d %3d %3d %3d %3d %3d %3d %3d %3d %3d %3d %3d %3d %3d " +
        "\nFinal report" +
        "\nN. of passengers which have this airport as their final destination = %2d" +
        "\nN. of passengers in transit = %2d" +
        "\nN. of bags that should have been transported in the the planes hold = %2d" +
        "\nN. of bags that were lost = %2d", );
         */

        /*
              AIRPORT RHAPSODY - Description of the internal state of the problem
        PLANE       PORTER          DRIVER
        FN BN       Stat CB SR      Stat Q1 Q2 Q3 Q4 Q5 Q6 S1 S2 S3
                                    PASSENGERS
        St1 Si1 NR1 NA1 St2 Si2 NR2 NA2 St3 Si3 NR3 NA3 St4 Si4 NR4 NA4 St5 Si5 NR5 NA5 St6 Si6 NR6 NA6
        ## ## #### ## ## #### # # # # # # # # #
        ### ### # # ### ### # # ### ### # # ### ### # # ### ### # # ### ### # #
        */

        /*
            Final report
            N. of passengers which have this airport as their final destination = ##
            N. of passengers in transit = ##
            N. of bags that should have been transported in the the planes hold = ##
            N. of bags that were lost = ##
        */
        System.out.println(log);
        printW.write(log);
        printW.flush();
    }
}