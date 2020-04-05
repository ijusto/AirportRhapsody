package main;

import commonInfrastructures.MemException;
import entities.*;
import sharedRegions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *   Main program.
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class AirportConcurrentVersion {

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

        String fileName = "log.txt";
        char[][] destStat = new char[SimulationParameters.N_PASS_PER_FLIGHT][SimulationParameters.N_FLIGHTS];
        int[][] nBags = new int[SimulationParameters.N_PASS_PER_FLIGHT][SimulationParameters.N_FLIGHTS];
        int[][] nBagsPHold = new int[SimulationParameters.N_PASS_PER_FLIGHT][SimulationParameters.N_FLIGHTS];
        char opt;

        File loggerFile = new File(fileName);
        try {
            if (loggerFile.createNewFile()) {
                System.out.print("File created: " + loggerFile.getName());
            } else {
                do {
                    System.out.print("There is already a file with this name. Delete it (y - yes; n - no)? ");
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    opt = 'y';
                    //opt = (char)br.read();
                } while( (opt != 'y') && (opt != 'n'));
                if(opt == 'y'){
                    loggerFile.delete();
                    loggerFile.createNewFile();
                    System.out.print("File created: " + loggerFile.getName());
                }
            }
        } catch (IOException e) {
            System.out.print("An error occurred.");
            e.printStackTrace();
        }

        for(int land = 0; land < SimulationParameters.N_FLIGHTS; land++){
            for(int nPass = 0; nPass < SimulationParameters.N_PASS_PER_FLIGHT; nPass++){
                destStat[nPass][land] = (Math.random() < 0.4) ? 'F' : 'T';
                nBags[nPass][land] = (Math.random() < 0.5) ? 2 : (Math.random() < 0.5) ? 1 : 0;
                nBagsPHold[nPass][land] = nBags[nPass][land];
                if(nBagsPHold[nPass][land] > 0) {
                    if (Math.random() < 0.2) {
                        nBagsPHold[nPass][land] -= 1;
                        if ((nBagsPHold[nPass][land] > 0) && (Math.random() < 0.2)) {
                            nBagsPHold[nPass][land] -= 1;
                        }
                    }
                }
            }
        }

        /* instantiation of the shared regions */
        repos = new GenReposInfo(fileName);
        bagColPoint = new BaggageColPoint(repos);
        bagRecOffice = new BaggageReclaimOffice(repos);
        tmpStorageArea = new TemporaryStorageArea(repos);
        arrivLounge = new ArrivalLounge(repos, bagColPoint, destStat, nBagsPHold);
        arrivalQuay = new ArrivalTermTransfQuay(repos);
        departureQuay = new DepartureTermTransfQuay(repos);
        arrivalTerm = new ArrivalTerminalExit(repos, arrivLounge, arrivalQuay);
        departureTerm = new DepartureTerminalEntrance(repos, arrivLounge, arrivalQuay);
        arrivLounge.setDepartureTerminalRef(departureTerm);
        arrivLounge.setArrivalTerminalRef(arrivalTerm);
        arrivalTerm.setDepartureTerminalRef(departureTerm);
        departureTerm.setArrivalTerminalRef(arrivalTerm);

        /* instantiation of the entities */
        Passenger[][] passengers = new Passenger[SimulationParameters.N_PASS_PER_FLIGHT][SimulationParameters.N_FLIGHTS];
        Porter porter;
        BusDriver busDriver;

        porter = new Porter(PorterStates.WAITING_FOR_A_PLANE_TO_LAND, arrivLounge, tmpStorageArea,
                bagColPoint);
        repos.updatePorterStat(PorterStates.WAITING_FOR_A_PLANE_TO_LAND);

        busDriver = new BusDriver(BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL, arrivalQuay,
                departureQuay,repos);
        repos.updateBDriverStat(BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL);

        porter.start();
        busDriver.start();

        for(int land = 0; land < SimulationParameters.N_FLIGHTS; land++){
            for(int nPass = 0; nPass < SimulationParameters.N_PASS_PER_FLIGHT; nPass++){
                Passenger.SituationPassenger Si = (destStat[nPass][land] == 'F') ? Passenger.SituationPassenger.FDT :
                        Passenger.SituationPassenger.TRT;
                passengers[nPass][land] = new Passenger(PassengerStates.AT_THE_DISEMBARKING_ZONE, Si,
                        nBags[nPass][land], 0, nPass, arrivLounge, arrivalQuay, departureQuay,
                        departureTerm, arrivalTerm, bagColPoint, bagRecOffice);

                repos.updatePassSt(passengers[nPass][land].getPassengerID(), PassengerStates.AT_THE_DISEMBARKING_ZONE);
                repos.getPassSi(passengers[nPass][land].getPassengerID(),passengers[nPass][land].getSi().toString());
            }

            for(int nPass = 0; nPass < SimulationParameters.N_PASS_PER_FLIGHT; nPass++){
                passengers[nPass][land].start();
            }

            for(int nPass = 0; nPass < SimulationParameters.N_PASS_PER_FLIGHT; nPass++) {
                try {
                    passengers[nPass][land].join();
                } catch (InterruptedException e) {
                    System.out.print("Main Program - One thread of Passenger " + nPass + " from flight " +
                            land + " was interrupted.");
                }
            }

            if(land < SimulationParameters.N_FLIGHTS - 1) {
                bagColPoint.resetBaggageColPoint();
                System.out.print("\nHelp1");
                tmpStorageArea.resetTemporaryStorageArea();
                System.out.print("\nHelp2");
                arrivLounge.resetArrivalLounge(destStat, nBagsPHold);
                System.out.print("\nHelp3");
                arrivalQuay.resetArrivalTermTransfQuay();
                System.out.print("\nHelp4");
                departureQuay.resetDepartureTermTransfQuay();
                System.out.print("\nHelp5");
                arrivalTerm.resetArrivalTerminalExit();
                System.out.print("\nHelp6");
                System.out.print("\nHelp7");
            } else {
                arrivLounge.setEndDay();
                arrivalQuay.setEndDay();
            }
            System.out.print("\nHelp");
        }
        System.out.print("before porter join");

        try {
            porter.join();
        } catch (InterruptedException e) {
            System.out.print("Main Program - One thread of Porter was interrupted.");
        }
        System.out.print("after porter join / before bus driver join");
        try {
            busDriver.join();
        } catch (InterruptedException e) {
            System.out.print("Main Program - One thread of BusDriver was interrupted.");
        }


        repos.finalReport();

        System.out.print("\n after final report");

    }
}
