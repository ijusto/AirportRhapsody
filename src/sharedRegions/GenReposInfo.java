package sharedRegions;

import entities.BusDriverStates;
import entities.Passenger;
import entities.PassengerStates;
import entities.PorterStates;
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

    /*
     *
     */

    int transPassTotal;

    /*
     *
     */

    int finalPassTotal;

    /*
     *   Number of pieces of luggage all passengers combined had at the start of the journey.
     */

    int nrTotal;

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
     *   @param fileName Name of the log file.
     */

    public GenReposInfo(String fileName) {

        try {
            this.fw = new FileWriter(fileName, true);
            this.bWritter = new BufferedWriter(this.fw);
        } catch (IOException e) {
            System.out.print("An error occurred.");
            e.printStackTrace();
        }

        porterState = PorterStates.WAITING_FOR_A_PLANE_TO_LAND;
        passengerStates = new PassengerStates[SimulationParameters.N_PASS_PER_FLIGHT];
        Arrays.fill(passengerStates, PassengerStates.AT_THE_ARRIVAL_TRANSFER_TERMINAL);
        busDriverState = BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL;

        FN = 0;
        BN = 0;
        SR = 0;
        missing_bags = 0;
        nrTotal = 0;
        transPassTotal = 0;
        finalPassTotal = 0;

        passengerSituation = new String[SimulationParameters.N_PASS_PER_FLIGHT];
        totalLuggage = new int[SimulationParameters.N_PASS_PER_FLIGHT];
        collectedLuggage = new int[SimulationParameters.N_PASS_PER_FLIGHT];
        passWaitingQueue = new ArrayList<>();
        busSeatOccupation = new ArrayList<>();

        log = new StringBuilder();

        print_header();

    }


    /**
     *
     */

    public void print_header(){
        log.append("\n\n\t\t\t\t\tAIRPORT RHAPSODY - Description of the internal state of the problem");
        log.append("\n PLANE      PORTER                 DRIVER                                                      " +
                "         PASSENGERS");
    }

    /**
     *
     */

    private void printLog(){
        log.append("\nFN|BN| |Stat|CB|SR| |Stat|Q1|Q2|Q3|Q4|Q5|Q6|S1|S2|S3| |St1|Si1|NR1|NA1|St2|Si2|NR2|NA2|St3|Si3|NR3|NA3|St4|Si4|NR4|NA4|St5|Si5|NR5|NA5|St6|Si6|NR6|NA6");
        //log.append("\n|FN|BN|  |Stat|CB|SR|  |Stat|Q1|Q2|Q3|Q4|Q5|Q6|S1|S2|S3|  |St1|Si1|NR1|NA1| " +
        //        "|St2|Si2|NR2|NA2| |St3|Si3|NR3|NA3| |St4|Si4|NR4|NA4| |St5|Si5|NR5|NA5| |St6|Si6|NR6|NA6|");

        log.append(String.format("\n%2d|%2d|", FN, BN));
        //log.append(String.format("\n|%2d|%2d|", FN, BN));

        log.append(String.format(" |%4s|%2d|%2d| |", portState[porterState.ordinal()], CB, SR));
        //log.append(String.format("  |%4s|%2d|%2d|  |", portState[porterState.ordinal()], CB, SR));

        log.append(String.format("%4s|",  busState[busDriverState.ordinal()]));
        //log.append(String.format("%4s|",  busState[busDriverState.ordinal()]));
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
            //log.append(String.format("%s|", occupStat));
        }

        log.append(" ");
        //log.append(" ");
        for (int i = 0; i< SimulationParameters.N_PASS_PER_FLIGHT; i++){
            String psi = "---";
            String pst =  "---";

            if(passengerSituation[i] != null){
                psi =  passengerSituation[i];
            }
            if(passState[passengerStates[i].ordinal()] != null){
                pst = passState[passengerStates[i].ordinal()];
            }
            log.append(String.format("|%3s|%3s|%3d|%3d",
            //log.append(String.format(" |%3s|%3s|%3d|%3d|",
                    pst, psi,
                    totalLuggage[i], collectedLuggage[i]));
        }

        System.out.print(log.toString());
        try {
            bWritter.write(log.toString());
            bWritter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.setLength(0);
    }

    /**
     *
     */

    public synchronized void finalReport(){
        log.append("\n\n\nFinal report");
        log.append(String.format("\nN. of passengers which have this airport as their final destination = %3d",
                this.finalPassTotal));
        log.append(String.format("\nN. of passengers in transit = %2d", this.transPassTotal));
        log.append(String.format("\nN. of bags that should have been transported in the the planes hold = %3d",
                this.nrTotal));
        log.append(String.format("\nN. of bags that were lost = %3d\n\n", missing_bags));


        System.out.print(log.toString());

        try {
            bWritter.write(log.toString());
            bWritter.flush();
            fw.close();
            bWritter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* **************************************************Plane******************************************************* */

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
     *   Initializes the number of pieces of luggage at the plane's hold.
     *
     *    @param bn Number of pieces of luggage presently at the plane's hold.
     */

    public synchronized void initializeCargoHold(int bn){
        BN = bn;
        printLog();
    }

    /**
     *   Update baggage stored in the cargo hold when porter retrieves the baggage.
     *
     */

    public synchronized void removeBagFromCargoHold(){
        BN = BN - 1;
        printLog();
    }

    /* **************************************************Porter****************************************************** */

    /**
     *   Update baggage stored in the Conveyor Belt when porter adds bags.
     */

    public synchronized void incBaggageCB(){
        CB = CB + 1;
        printLog();
    }

    /**
     *   Update baggage stored in the Conveyor Belt when passenger gets the baggage.
     */

    public synchronized void pGetsABag(){
        CB = CB - 1;
        printLog();
    }

    /**
     *   Update baggage stored in the Storage Room when porter puts the baggage
     */

    public synchronized void saveBagInSR(){
        SR = SR + 1;
        printLog();
    }

    /**
     *   Update the Porter state.
     *
     *    @param porterState porterState.
     */

    public synchronized void updatePorterStat(PorterStates porterState){
        if(this.porterState != porterState){
            this.porterState = porterState;
        }
        printLog();
    }

    /* ************************************************Bus Driver**************************************************** */

    /**
     *   Update the Bus Driver State.
     *
     *   @param busDriverState busDriverState.
     */

    public synchronized void updateBDriverStat(BusDriverStates busDriverState){
        if(this.busDriverState != busDriverState){
            this.busDriverState = busDriverState;
        }
        printLog();
    }

    /**
     *   Update the queue of passengers.
     *
     *    @param id Passenger ID.
     */

    public synchronized void pJoinWaitingQueue(int id){
        passWaitingQueue.add(id);
        printLog();
    }

    /**
     *
     *    @param id Passenger ID.
     */

    public synchronized void pLeftWaitingQueue(int id){
        passWaitingQueue.remove(passWaitingQueue.indexOf(id));
        printLog();
    }

    /**
     *   Update of the occupation state of the bus seat.
     *
     *   @param id passenger.
     */

    public synchronized void occupyBusSeat(int id){
        busSeatOccupation.add(id);
        printLog();
    }

    /**
     *   Removes the passenger id from the bus seat.
     *
     *    @param id Passenger ID.
     */

    public synchronized void freeBusSeat(int id){
        busSeatOccupation.remove(busSeatOccupation.indexOf(id));
        printLog();
    }

    /* **************************************************Passenger*************************************************** */

    /**
     *   Increments the number of passengers from a certain passenger situation.
     *
     *    @param passSi Passenger situation.
     */

    public synchronized  void newPass(Passenger.SituationPassenger passSi){
        if(passSi == Passenger.SituationPassenger.TRT){
            transPassTotal += 1;
        } else if(passSi == Passenger.SituationPassenger.FDT) {
            finalPassTotal += 1;
        }
    }

    /**
     *   Update the Passenger State.
     *
     *   @param id id.
     *   @param passengerState passengerState.
     */

    public synchronized void updatePassSt(int id, PassengerStates passengerState){
        if(passengerStates[id] != passengerState){
            passengerStates[id] = passengerState;
            printLog();
        }
    }

    /**
     *   Get the passenger situation (in transit or final).
     *
     *   @param id Passenger ID.
     *   @param si Passenger Situation.
     */

    public synchronized void getPassSi(int id, String si){
        passengerSituation[id] = si;
    }

    /**
     *   Update the passengers luggage at the start of the journey.
     *
     *   @param id id.
     *   @param nr Number of pieces of luggage the passenger had at the start of the journey.
     */

    public synchronized void updatesPassNR(int id, int nr){
        totalLuggage[id] = nr;
    }

    /**
     *   Update the number of luggage a passenger collected.
     *
     *   @param id Passenger ID.
     *   @param na Number of pieces of luggage the passenger has collected.
     */

    public synchronized void updatesPassNA(int id, int na){
        collectedLuggage[id] = na;
    }

    /**
     *
     *    @param id Passenger ID.
     */

    public synchronized void passengerExit(int id){
        this.passengerSituation[id] = null;
        this.passState[passengerStates[id].ordinal()] = null;
        this.totalLuggage[id] = 0;
        this.collectedLuggage[id] = 0;
    }

    /* ***********************************************Final Report*************************************************** */

    /**
     *   Updates the number of reported missing bags.
     */

    public synchronized void missingBagReported(){
        missing_bags += 1;
    }

    /**
     *   Updates the number of pieces of luggage all passengers combined had at the start of the journey.
     *
     *    @param nr Number of pieces of luggage all passengers combined had at the start of the journey.
     */

    public synchronized void numberNRTotal(int nr){
        nrTotal += nr;
    }

}