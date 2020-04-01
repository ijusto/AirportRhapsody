package sharedRegions;

import entities.BusDriverStates;
import entities.Passenger;
import entities.PassengerStates;
import entities.PorterStates;
import genclass.GenericIO;
import main.SimulationParameters;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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

    private final String[] passState = {"ADZ", "LCP", "EAT", "BRO", "ATT", "TT", "DTT", "EDT"};

    /*
     *
     */

    private final String[] portState = {"WPL", "APH", "LBC", "ASR"};

    /*
     *
     */

    private final String[] busState  = {"PAT", "DF", "PDT", "DB"};

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

    private ArrayList<Integer> passWaitingQueue;

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

    /**
     *
     */

    private BufferedWriter bWritter;

    /**
     *
     */

    private FileWriter fw;

    /**
     *
     */

    private StringBuilder log;

    /**
     *   Instantiation of the General Repository of Information.
     *
     *   @param fileName ...
     */

    public GenReposInfo(String fileName) {

        try {
            this.fw = new FileWriter(fileName, true);
            this.bWritter = new BufferedWriter(this.fw);
        } catch (IOException e) {
            GenericIO.writeString("An error occurred.");
            e.printStackTrace();
        }

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
        passWaitingQueue = new ArrayList<>();
        busSeatOccupation = new ArrayList<>();

        log = new StringBuilder();

        print_header();

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
        passWaitingQueue.add(ID);
        printLog();
    }

    /**
     *
     * @param ID
     *
     */

    public synchronized void passengerQueueStateOut(int ID){
        int index = passWaitingQueue.indexOf(ID);
        passWaitingQueue.remove(index);
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

    public synchronized void numberOfPassengerLuggage(int id, Passenger passenger){
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
     *
     */
    public synchronized void passengerExit(int id){
        this.passengerSituation[id] = null;
        this.passState[passengerStates[id].ordinal()] = null;
        this.totalLuggage[id] = 0;
        this.collectedLuggage[id] = 0;
    }

    /**
     *  Number of bags carried by a passenger at the end of the journey
     */

    public synchronized void finalReport(){

        log.append("\n\n\nFinal report");
        log.append(String.format("\nN. of passengers which have this airport as their final destination = %2d", 1));
        log.append(String.format("\nN. of passengers in transit = %2d", 2));
        log.append(String.format("\nN. of bags that should have been transported in the the planes hold = %2d", 3));
        log.append(String.format("\nN. of bags that were lost = %2d\n\n", missing_bags));
        printLog();
        try {
            fw.close();
            bWritter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     *
     */

    public void print_header(){
        log.append("\n\n\t\t\t\t\tAIRPORT RHAPSODY - Description of the internal state of the problem");
        log.append("\n PLANE      PORTER                 DRIVER                                                               PASSENGERS");
    }

    /*
     *
     */

    private void printLog(){
        log.append("\n|FN|BN|  |Stat|CB|SR|  |Stat|Q1|Q2|Q3|Q4|Q5|Q6|S1|S2|S3|  |St1|Si1|NR1|NA1| " +
                "|St2|Si2|NR2|NA2| |St3|Si3|NR3|NA3| |St4|Si4|NR4|NA4| |St5|Si5|NR5|NA5| |St6|Si6|NR6|NA6|");
        log.append(String.format("\n|%2d|%2d|", FN, BN));
        log.append(String.format("  |%4s|%2d|%2d|  |", portState[porterState.ordinal()], CB, SR));
        log.append(String.format("%4s|",  busState[busDriverState.ordinal()]));
        for(int j = 0; j < SimulationParameters.N_PASS_PER_FLIGHT; j++){
            String passId;
            if(j > passWaitingQueue.size() - 1){
                passId = "--";
            } else {
                passId = String.format("%2s", passWaitingQueue.get(j));
            }
            log.append(String.format("%s|", passId));
        }
        for(int k = 0; k < SimulationParameters.BUS_CAP; k++){
            String occupStat;
            if(k > busSeatOccupation.size() - 1){
                occupStat = "--";
            } else {
                occupStat = String.format("%2s", busSeatOccupation.get(k));
            }
            log.append(String.format("%s|", occupStat));
        }
        log.append(" ");
        for (int i = 0; i< SimulationParameters.N_PASS_PER_FLIGHT; i++){
            String psi = "---";
            String pst =  "---";

            if(passengerSituation[i] != null){
                psi =  passengerSituation[i];
            }
            if(passState[passengerStates[i].ordinal()] != null){
                pst = passState[passengerStates[i].ordinal()];
            }
            log.append(String.format(" |%3s|%3s|%3d|%3d|",
                    pst, psi,
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
        try {
            bWritter.write(log.toString());
            bWritter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.setLength(0);
    }


    /* ******************************************** Getters and Setters ***********************************************/

    /*
     *
     */

    public int getFN() {
        return FN;
    }
}