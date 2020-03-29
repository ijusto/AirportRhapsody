package sharedRegions;
import  entities.*;
import genclass.GenericIO;
import main.SimulationParameters;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

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

    private ArrayList<Integer> passengersQueue;

    /*
     *
     */

    private ArrayList<Integer> busSeatOccupation;

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
     *
     */

    int missing_bags;

    /*
     *
     */

    StringBuilder log;

    /*
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

    public GenReposInfo(String fileName) throws FileNotFoundException{

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
        BN = 0;
        SR = 0;
        busQueue = 0;
        missing_bags = 0;

        passengerSituation = new String[SimulationParameters.N_PASS_PER_FLIGHT];
        totalLuggage = new int[SimulationParameters.N_PASS_PER_FLIGHT];
        collectedLuggage = new int[SimulationParameters.N_PASS_PER_FLIGHT];
        passengersQueue = new ArrayList<>();
        busSeatOccupation = new ArrayList<>();

        StringBuilder log = new StringBuilder();
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


    public synchronized void initializeCargoHold(int bn){
        BN = bn;
        printLog();
    }

    /**
     *   Update baggage stored in the cargo hold when porter retrieves the baggage.
     *
     */

    public synchronized void updateStoredBaggageCargoHold(){
        BN = BN - 1;
        printLog();
    }

    /**
     *  Update baggage stored in the Conveyor Belt when porter puts the baggage
     */

    public synchronized void updateStoredBaggageConveyorBeltInc(){
        CB = CB + 1;
        printLog();
    }

    /**
     *  Update baggage stored in the Conveyor Belt when passenger retrieves the baggage
     */

    public synchronized void updateStoredBaggageConveyorBeltDec(){
        CB = CB  - 1;
        printLog();
    }

    /**
     *  Update baggage stored in the Storage Room when porter puts the baggage
     */

    public synchronized void updateStoredBaggageStorageRoom(){
        SR = SR + 1;
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
     * @param ID
     */

    public synchronized void passengerQueueStateIn(int ID){
        passengersQueue.add(ID);
        printLog();
    }

    /**
     *
     * @param ID
     *
     */

    public synchronized void passengerQueueStateOut(int ID){
        int index = passengersQueue.indexOf(ID);
        passengersQueue.remove(index);
        printLog();
    }


    /**
     *   Update of the occupation state of the bus seat.
     *
     *   @param ID passenger.

     */

    public synchronized void busSeatStateIn(int ID){
        busSeatOccupation.add(ID);
        printLog();
    }

    /**
     *
     *
     *   @param ID passenger.
     */

    public synchronized void busSeatStateOut(int ID){
        int index = busSeatOccupation.indexOf(ID);
        busSeatOccupation.remove(index);
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
    }

    /**
     *   Update the passengers luggage at the start of the journey.
     *
     *   @param id id.
     *   @param passenger passenger.
     */

    public synchronized void numberOfPassangerLuggage(int id, Passenger passenger){
        totalLuggage[id] = passenger.getNR();
    }

    /**
     *   Update the number of luggage a passenger collected.
     *
     *   @param id id.
     *   @param passenger passenger.
     */

    public synchronized void baggageCollected(int id, Passenger passenger){
        collectedLuggage[id] = passenger.getNA();
    }

    public synchronized  void numberMissingBags(){
        missing_bags += 1;
    }

    /**
     *  Number of bags carried by a passenger at the end of the journey
     */

    public synchronized void finalReport(){

        log.append(String.format("\n\n\nFinal report" ));
        log.append(String.format("\nN. of passengers which have this airport as their final destination = %2d"));
        log.append(String.format("\nN. of passengers in transit = %2d"));
        log.append(String.format("\nN. of bags that should have been transported in the the planes hold = %2d"));
        log.append(String.format("\nN. of bags that were lost = %2d\n\n", missing_bags));
        printLog();
        printW.close();
    }

    /*
     *
     */

    private void printLog(){


        log.append("\t\tAIRPORT RHAPSODY - Description of the internal state of the problem\n");
        log.append("PLANE\t\t\tPORTER\t\t\t\t\t\tDRIVER");
        log.append(String.format("\n%3d %3d\t", FN, BN));
        log.append(String.format("\t%ss %3d %3d\t", portState[porterState.ordinal()], CB, SR));
        log.append(String.format("%s ", busState[busDriverState.ordinal()]));
        for(int j = 0; j < passengersQueue.size()-1; j++){
            log.append(String.format("%3d ", passengersQueue.get(j)));
        }

        for(int k = 0; k < busSeatOccupation.size()-1; k++){
            log.append(String.format("%3d ", busSeatOccupation.get(k)));
        }

        log.append("\n\t\t\t\tPASSENGERS\n");
        for (int i = 0; i< SimulationParameters.N_PASS_PER_FLIGHT; i++){
            log.append(String.format("%s %s %3d %3d", passState[passengerStates[i].ordinal()], passengerSituation[i],
                    totalLuggage[i], collectedLuggage[i]));
        }

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
        GenericIO.writeString(log.toString());
        printW.write(log.toString());
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