package sharedRegions;
import  entities.*;
import genclass.GenericIO;
import main.SimulationParameters;

import java.io.*;
import java.util.Arrays;

/**
 *   The General Repository of Information works solely as the place where the visible internal state of the problem
 *   is stored. The visible internal state is defined by the set of variables whose value is printed in the logging file.
 *   Whenever an entity (porter, passenger, bus driver) executes an operation that changes the values of some of these
 *   variables, the fact must be reported so that a new line group is printed in the logging file. The report operation
 *   must be atomic, that is, when two or more variables are changed, the report operation must be unique so that the
 *   new line group reflects all the changes that have taken place.
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class GenReposInfo {

    /*
     *
     */

    private final String[] passState = {"ATDZ", "ATLCP", "ETAT", "ATBRO", "ATATT", "TT", "ATDTT", "ETDT"};

    /*
     *
     */

    private final String[] portState = {"WFPL", "APH", "ALBC", "ASR"};

    /*
     *
     */

    private final String[] busState  = {"PATAT", "DF", "DB", "PATDT"};

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

    private PorterStates porterState;

    /*
     *   BusDrivers
     */

    private BusDriverStates busDriverState;

    /*
     *
     */

    private int busQueue;

    /*
     *   Passengers' States
     */

    private PassengerStates[] passengerStates;

    /*
     *
     */

    private int passengersQueue;

    /*
     *
     */

    private String[] passengerSituation;

    /*
     *
     */

    private int[] totalLuggage;

    /*
     *
     */

    private int[] collectedLuggage;

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
     *   Instantiation of the General Repository of Information.
     *
     *   @param fileName ...
     */

    public GenReposInfo(String fileName){

        try {
            this.printW = new PrintWriter(fileName);
        } catch (IOException e) {
            GenericIO.writeString("An error occurred.");
            e.printStackTrace();
        }

        this.fileName = fileName;

        porterState = PorterStates.WAITING_FOR_A_PLANE_TO_LAND;
        passengerStates = new PassengerStates[SimulationParameters.N_PASS_PER_FLIGHT];
        Arrays.fill(passengerStates, PassengerStates.AT_THE_ARRIVAL_TRANSFER_TERMINAL);
        busDriverState = BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL;

        FN = 0;
        passengersQueue = 0;
        busQueue = 0;

    }

    /**
     *   Update flight number after the previous flight is finished.
     *
     *   @param flight flight.
     */
    public synchronized void updateFlightNumber(int flight){
        FN = flight + 1;
        printLog();
    }

    /**
     *   Update baggage stored in the cargo hold when porter retrieves the baggage.
     *
     *   @param baggage baggage.
     */
    public synchronized void updateStoredBaggage(int baggage){
        BN = BN - baggage;
        printLog();
    }

    /**
     *   Update the Porter state.
     *
     *   @param porterState porterState.
     */
    public synchronized void updatePorterState(PorterStates porterState){
        if(this.porterState != porterState){
            this.porterState = porterState;
        }
        printLog();
    }

    /**
     *   Update the Passenger State.
     *
     *   @param id id.
     *   @param passengerState passengerState.
     */

    public synchronized void updatePassengerState(int id, PassengerStates passengerState){
        if(passengerStates[id] != passengerState){
            passengerStates[id] = passengerState;
        }
        printLog();
    }

    /**
     *   Update the Bus Driver State.
     *
     *   @param busDriverState busDriverState.
     */

    public synchronized void updateBusDriverState(BusDriverStates busDriverState){
        if(this.busDriverState != busDriverState){
            this.busDriverState = busDriverState;
        }
        printLog();
    }

    /**
     *   Update the number of bags in the conveyor belt.
     *
     *   @param cb CB.
     */

    public synchronized void updateConveyorsBelt(int cb){
        CB = CB + cb;
        printLog();
    }

    /**
     *   Update the queue of passengers.
     *
     *   @param passenger passenger.
     */

    public synchronized void passengerQueueStateIn(Passenger passenger){
        passengersQueue++;
        passenger.setSt(PassengerStates.AT_THE_DISEMBARKING_ZONE);
        printLog();
    }

    /**
     *
     *
     *   @param passenger passenger.
     */

    public synchronized void passengerQueueStateOut(Passenger passenger){
        passengersQueue--;
        printLog();
    }

    /**
     *   Update of the occupation state of the bus seat.
     *
     *   @param passenger passenger.
     *   @param busDriver bus driver.
     */

    public synchronized void busSeatStateIn(Passenger passenger, BusDriver busDriver){
        busQueue++;
        passenger.setSt(PassengerStates.TERMINAL_TRANSFER);
        busDriver.setStat(BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL);
        printLog();
    }

    /**
     *
     *
     *   @param passenger passenger.
     *   @param busDriver bus driver.
     */

    public synchronized void busSeatStateOut(Passenger passenger, BusDriver busDriver){
        busQueue--;
        passenger.setSt(PassengerStates.AT_THE_DEPARTURE_TRANSFER_TERMINAL);
        busDriver.setStat(BusDriverStates.PARKING_AT_THE_DEPARTURE_TERMINAL);
        printLog();
    }

    /**
     *   Get the passenger situation (in transit or final).
     *
     *   @param id id.
     *   @param passenger passenger.
     */

    public synchronized void getPassengerSituation(int id, Passenger passenger){
        passengerSituation[id] = passenger.getSi().toString();
        printLog();
    }

    /**
     *   Update the passengers luggage at the start of the journey.
     *
     *   @param id id.
     *   @param passenger passenger.
     */

    public synchronized void numberOfPassangerLuggage(int id, Passenger passenger){
        totalLuggage[id] = passenger.getNR();
        printLog();
    }

    /**
     *   Update the number of luggage a passenger collected.
     *
     *   @param id id.
     *   @param passenger passenger.
     */

    public synchronized void baggageCollected(int id, Passenger passenger){
        collectedLuggage[id] = passenger.getNR();
        printLog();
    }

    /**
     *  Number of bags carried by a passenger at the end of the journey
     */

    public synchronized void finalReport(){

        printLog();
        printW.close();
    }

    /*
     *
     */

    private void printLog(){

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
        "\nN. of bags that were lost = %2d");


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


    /* ******************************************** Getters and Setters ***********************************************/

    /*
     *
     */

    public int getFN() {
        return FN;
    }
}