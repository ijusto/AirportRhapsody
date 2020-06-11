package serverSide.sharedRegions;

import clientSide.entities.*;
import comInf.Bag;
import comInf.SimulPar;

import javax.script.SimpleBindings;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *   The General Repository of Information works solely as the place where the visible internal state of the problem
 *   is stored. The visible internal state is defined by the set of variables whose value is printed in the logging file.
 *   Whenever an entity (porter, passenger, bus driver) executes an operation that changes the values of some of these
 *   variables, the fact must be reported so that a new line group is printed in the logging file.
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class GenReposInfo {

    /*
     *   Array of passenger's states representing strings.
     */

    private final String[] passState = {"WSD", "LCP", "EAT", "BRO", "ATT", "TT", "DTT", "EDT"};

    /*
     *   Array of porter's states representing strings.
     */

    private final String[] portState = {"WPL", "APH", "LBC", "ASR"};

    /*
     *   Array of bus driver's states representing strings.
     */

    private final String[] busState  = {"PAT", "DF", "PDT", "DB"};

    /**
     *   Flight number.
     */

    private int FN;

    /**
     *   Number of pieces of luggage presently at the plane's hold.
     */

    private int BN;

    /**
     *   Number of pieces of luggage presently at the conveyors belt.
     */

    private int CB;

    /**
     *   Number of pieces of luggage from transit passengers.
     */

    private int SR;

    /*
     *   Porter's state.
     */

    private PorterStates porterState;

    /*
     *   Bus driver's state.
     */

    private BusDriverStates busDriverState;

    /*
     *   Passengers' States.
     */

    private PassengerStates[] passengerStates;

    /*
     *   ArrayList of passengers waiting for the bus driver.
     */

    private ArrayList<Integer> passWaitingQueue;

    /*
     *   ArrayList of passengers seating on the bus.
     */

    private ArrayList<Integer> busSeatOccupation;

    /*
     *   Situation of all the passengers.
     */

    private String[] passSituation;

    /*
     *   Array with the number of pieces of luggage that each passenger had at the start of the journey.
     */

    private int[] totalLuggage;

    /*
     *   Array with the number of pieces of luggage that each passenger collected by the end of the passenger's life
     *   cycle.
     */

    private int[] collectedLuggage;

    /*
     *   Number of missed bags.
     */

    int missing_bags;

    /*
     *   Number of passengers in transit.
     */

    int transPassTotal;

    /*
     *   Number of passengers which have this airport as their final destination.
     */

    int finalPassTotal;

    /*
     *   Number of pieces of luggage all passengers combined had at the start of the journey.
     */

    int nrTotal;

    /**
     *   BufferedWriter to write the log to a file.
     */

    private BufferedWriter bwt;

    /**
     *   FileWriter to write the log to a file.
     */

    private FileWriter fw;

    /**
     *   StringBuilder used to append all the Strings to log.
     */

    private StringBuilder log;


    /**
     *   Instantiation of the General Repository of Information.
     *
     *    @param fileName Name of the log file.
     */

    public GenReposInfo(String fileName) {

        try {
            this.fw = new FileWriter(fileName, true);
            this.bwt = new BufferedWriter(this.fw);
        } catch (IOException e) {
            System.out.print("An error occurred.");
            e.printStackTrace();
        }

        porterState = PorterStates.WAITING_FOR_A_PLANE_TO_LAND;
        passengerStates = new PassengerStates[SimulPar.N_PASS_PER_FLIGHT];
        Arrays.fill(passengerStates, PassengerStates.AT_THE_DISEMBARKING_ZONE);
        busDriverState = BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL;

        FN = BN = SR = missing_bags = nrTotal = transPassTotal = finalPassTotal = 0;

        passSituation = new String[SimulPar.N_PASS_PER_FLIGHT];
        totalLuggage = new int[SimulPar.N_PASS_PER_FLIGHT];
        collectedLuggage = new int[SimulPar.N_PASS_PER_FLIGHT];
        passWaitingQueue = new ArrayList<>();
        busSeatOccupation = new ArrayList<>();

        log = new StringBuilder();

        print_header();
    }

    /**
     *   Logs the header of the info.
     */

    public synchronized void print_header(){
        log.append("\n\n\t\t\t\t\tAIRPORT RHAPSODY - Description of the internal state of the problem");
        log.append("\n PLANE      PORTER                 DRIVER                                                      " +
                "         PASSENGERS");
    }

    /**
     *   Logs the alterations.
     */

    public synchronized void printLog(){
        log.append("\nFN|BN| |Stat|CB|SR| |Stat|Q1|Q2|Q3|Q4|Q5|Q6|S1|S2|S3| |St1|Si1|NR1|NA1|St2|Si2|NR2|NA2|St3|Si3|"+
                        "NR3|NA3|St4|Si4|NR4|NA4|St5|Si5|NR5|NA5|St6|Si6|NR6|NA6");
        log.append(String.format("\n%2d|%2d|", FN, BN));
        log.append(String.format(" |%4s|%2d|%2d| |", portState[porterState.ordinal()], CB, SR));
        log.append(String.format("%4s|",  busState[busDriverState.ordinal()]));

        for(int nPass = 0; nPass < SimulPar.N_PASS_PER_FLIGHT; nPass++){
            String passId = (nPass > passWaitingQueue.size() - 1)
                    ? "--" : String.format("%2s", passWaitingQueue.get(nPass));
            log.append(String.format("%s|", passId));
        }

        for(int seat = 0; seat < SimulPar.BUS_CAP; seat++){
            String occupStat = (seat > busSeatOccupation.size() - 1)
                                    ? "--" : String.format("%2s", busSeatOccupation.get(seat));
            log.append(String.format("%s|", occupStat));
        }

        log.append(" ");

        for (int i = 0; i< SimulPar.N_PASS_PER_FLIGHT; i++){
            String psi = (passSituation[i] != null) ? passSituation[i] :  "---";
            String pst = (passengerStates[i] != null) ? passState[passengerStates[i].ordinal()] : "---";

            log.append(String.format("|%3s|%3s|%3d|%3d", pst, psi, totalLuggage[i], collectedLuggage[i]));
        }

        System.out.print(log.toString());
        try {
            bwt.write(log.toString());
            bwt.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.setLength(0);
    }

    /**
     *   Logs the final report of the simulation.
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
            bwt.write(log.toString());
            bwt.flush();
            fw.close();
            bwt.close();
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
    }

    /**
     *   Initializes the number of pieces of luggage at the plane's hold.
     *
     *    @param bn Number of pieces of luggage presently at the plane's hold.
     */

    public synchronized void initializeCargoHold(int bn){ BN = bn; }

    /**
     *   Update baggage stored in the cargo hold when porter retrieves the baggage.
     */

    public synchronized void removeBagFromCargoHold(){
        BN = BN - 1;
    }

    /* **************************************************Porter****************************************************** */

    /**
     *   Update baggage stored in the Conveyor Belt when porter adds bags.
     */

    public synchronized void incBaggageCB(){
        CB = CB + 1;
    }

    /**
     *   Update baggage stored in the Conveyor Belt when passenger gets the baggage.
     */

    public synchronized void pGetsABag(){
        CB = CB - 1;
    }

    /**
     *   Update baggage stored in the Storage Room when porter puts the baggage
     */

    public synchronized void saveBagInSR(){
        SR = SR + 1;
    }

    /**
     *   Update the Porter state.
     *
     *    @param porterState Porter's state.
     */

    public synchronized void updatePorterStat(int porterState){
        if(this.porterState != PorterStates.values()[porterState]){
            this.porterState = PorterStates.values()[porterState];
        }
    }

    /* ************************************************Bus Driver**************************************************** */

    /**
     *   Update the Bus Driver State.
     *
     *   @param busDriverState Bus driver's state.
     */

    public synchronized void updateBDriverStat(int busDriverState){
        if(this.busDriverState != BusDriverStates.values()[busDriverState]){
            this.busDriverState = BusDriverStates.values()[busDriverState];
        }
    }

    /**
     *   Update the queue of passengers.
     *
     *    @param id Passenger's id.
     */

    public synchronized void pJoinWaitingQueue(int id){
        passWaitingQueue.add(id);
    }

    /**
     *   Update of the occupation state of the bus seat.
     *
     *    @param id Passenger's id.
     */

    public synchronized void pLeftWaitingQueue(int id){
        passWaitingQueue.remove(passWaitingQueue.indexOf(id));
        busSeatOccupation.add(id);
    }

    /**
     *   Removes the passenger id from the bus seat.
     *
     *    @param id Passenger's id.
     */

    public synchronized void freeBusSeat(int id){
        busSeatOccupation.remove(busSeatOccupation.indexOf(id));
    }

    /* **************************************************Passenger*************************************************** */

    /**
     *   Increments the number of passengers from a certain passenger situation.
     *
     *    @param passSi Passenger's situation.
     */

    public synchronized  void newPass(int passSi){
        if(Passenger.SiPass.values()[passSi] == Passenger.SiPass.TRT){
            transPassTotal += 1;
        } else if(Passenger.SiPass.values()[passSi] == Passenger.SiPass.FDT) {
            finalPassTotal += 1;
        }
    }

    /**
     *   Update the Passenger State.
     *
     *    @param id Passenger's id.
     *    @param passengerState Passenger's state.
     */

    public synchronized void updatePassSt(int id, int passengerState){
        if(passengerStates[id] != PassengerStates.values()[passengerState]){
            passengerStates[id] = PassengerStates.values()[passengerState];
        }
    }

    /**
     *   Get the passenger situation (in transit or final).
     *
     *    @param id Passenger's id.
     *    @param si Passenger's situation.
     */

    public synchronized void getPassSi(int id, int si){
        passSituation[id] = Passenger.SiPass.values()[si].toString();
    }

    /**
     *   Update the passengers luggage at the start of the journey.
     *
     *   @param id Passenger's id.
     *   @param nr Number of pieces of luggage the passenger had at the start of the journey.
     */

    public synchronized void updatesPassNR(int id, int nr){
        totalLuggage[id] = nr;
    }

    /**
     *   Update the number of luggage a passenger collected.
     *
     *   @param id Passenger's id.
     *   @param na Number of pieces of luggage the passenger has collected.
     */

    public synchronized void updatesPassNA(int id, int na){
        collectedLuggage[id] = na;
    }

    /**
     *   Resets all the info of a passenger with a certain id.
     *
     *    @param id Passenger's id.
     */

    public synchronized void passengerExit(int id){
        this.passSituation[id] = null;
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