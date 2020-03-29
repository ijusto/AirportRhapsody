package main;

import commonInfrastructures.MemException;
import entities.*;
import genclass.GenericIO;
import sharedRegions.*;

import java.io.File;
import java.io.IOException;

/**
 *   Main program.
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class AirportConcurrentVersion {

    /**
     *   Minimum milliseconds to wakeup.
     *   Affects ...(see functions)
     */
    public static final int minSleep = 5;

    /**
     *   Maximum milliseconds to wakeup.
     *   Affects ...(see functions)
     */
    public static final int maxSleep = 10;

    private static final int maxBags4Passenger = 2;

    public static void main(final String[] args) throws MemException {

        GenReposInfo repos;
        BaggageColPoint bagColPoint;
        BaggageReclaimOffice bagRecOffice;
        TemporaryStorageArea tmpStorageArea;
        ArrivalLounge arrivLounge;
        ArrivalTermTransfQuay arrivalQuay;
        DepartureTermTransfQuay departureQuay;
        ArrivalTerminalExit arrivalTerm;
        DepartureTerminalEntrance departureTerm;
        TemporaryStorageArea tempStore;

        String fileName = "log.txt";
        char[][] destStat = new char[SimulationParameters.N_PASS_PER_FLIGHT][SimulationParameters.N_FLIGHTS];
        int[][] nBags = new int[SimulationParameters.N_PASS_PER_FLIGHT][SimulationParameters.N_FLIGHTS];
        int[][] nBagsPHold = new int[SimulationParameters.N_PASS_PER_FLIGHT][SimulationParameters.N_FLIGHTS];
        char opt;

        File loggerFile = new File(fileName);
        try {
            if (loggerFile.createNewFile()) {
                GenericIO.writeString("File created: " + loggerFile.getName());
            } else {
                do {
                    GenericIO.writeString("There is already a file with this name. Delete it (y - yes; n - no)? ");
                    opt = GenericIO.readlnChar();
                } while( (opt != 'y') && (opt != 'n'));
                if(opt == 'y'){
                    loggerFile.delete();
                    loggerFile.createNewFile();
                    GenericIO.writeString("File created: " + loggerFile.getName());
                }
            }
        } catch (IOException e) {
            GenericIO.writeString("An error occurred.");
            e.printStackTrace();
        }

        for(int land = 0; land < SimulationParameters.N_FLIGHTS; land++){
            for(int nPass = 0; nPass < SimulationParameters.N_PASS_PER_FLIGHT; nPass++){
                destStat[nPass][land] = (Math.random() < 0.4) ? 'F' : 'T';
                nBags[nPass][land] = (Math.random() < 0.5) ? 2 : (Math.random() < 0.5) ? 1 : 0;
                nBagsPHold[nPass][land] = nBags[nPass][land];
                if(nBagsPHold[nPass][land] > 0){
                    if(Math.random() < 0.2){
                        nBagsPHold[nPass][land] -= 1;
                        if( (nBagsPHold[nPass][land] > 0) && (Math.random() < 0.2)){
                            nBagsPHold[nPass][land] -= 1;
                        }
                    }
                }
            }
        }

        /*
         *   instantiation of the shared regions
         */
        repos = new GenReposInfo(fileName);
        bagColPoint = new BaggageColPoint(repos);
        bagRecOffice = new BaggageReclaimOffice(repos);
        tmpStorageArea = new TemporaryStorageArea(repos);
        arrivLounge = new ArrivalLounge(destStat, nBagsPHold, bagColPoint, repos);
        arrivalQuay = new ArrivalTermTransfQuay(repos);
        departureQuay = new DepartureTermTransfQuay(repos);
        arrivalTerm = new ArrivalTerminalExit(arrivLounge, arrivalQuay, repos);
        departureTerm = new DepartureTerminalEntrance(arrivLounge, arrivalQuay, repos);
        arrivalTerm.setDepartureTerminalRef(departureTerm);
        departureTerm.setArrivalTerminalRef(arrivalTerm);

        /*
         *   instantiation of the entities
         */
        Passenger[][] passengers = new Passenger[SimulationParameters.N_PASS_PER_FLIGHT][SimulationParameters.N_FLIGHTS];
        Porter[] porters = new Porter[SimulationParameters.N_FLIGHTS];
        BusDriver[] busDrivers = new BusDriver[SimulationParameters.N_FLIGHTS];

        for(int land = 0; land < SimulationParameters.N_FLIGHTS; land++){
            for(int nPass = 0; nPass < SimulationParameters.N_PASS_PER_FLIGHT; nPass++){
                Passenger.SituationPassenger Si = (destStat[nPass][land] == 'F') ? Passenger.SituationPassenger.FDT :
                        Passenger.SituationPassenger.TRT;
                passengers[nPass][land] = new Passenger(PassengerStates.AT_THE_DISEMBARKING_ZONE, Si,
                        nBags[nPass][land], 0, nPass, arrivLounge, arrivalQuay, departureQuay,
                        departureTerm, arrivalTerm, bagColPoint, bagRecOffice);
            }
            porters[land] = new Porter(PorterStates.WAITING_FOR_A_PLANE_TO_LAND, arrivLounge, tmpStorageArea,
                                        bagColPoint);
            busDrivers[land] = new BusDriver(BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL, arrivalQuay,
                                                departureQuay);

            for(int nPass = 0; nPass < SimulationParameters.N_PASS_PER_FLIGHT; nPass++){
                passengers[nPass][land].start();
            }
            porters[land].start();
            busDrivers[land].start();

            for(int nPass = 0; nPass < SimulationParameters.N_PASS_PER_FLIGHT; nPass++) {
                try {
                    passengers[nPass][land].join();
                } catch (InterruptedException e) {
                    GenericIO.writeString("Main Program - One thread of Passenger " + nPass + " from flight " +
                            land + " was interrupted.");
                }
            }

            try {
                porters[land].join();
            } catch (InterruptedException e) {
                GenericIO.writeString("Main Program - One thread of Porter from flight " + land +
                        " was interrupted.");
            }

            try {
                busDrivers[land].join();
            } catch (InterruptedException e) {
                GenericIO.writeString("Main Program - One thread of BusDriver from flight " + land +
                        " was interrupted.");
            }
        }

        repos.finalReport();
    }
}
