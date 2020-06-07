package clientSide.clients;

import clientSide.BusDriverStates;
import clientSide.PassengerStates;
import clientSide.PorterStates;
import clientSide.SimulPar;
import clientSide.entities.BusDriver;
import clientSide.entities.Passenger;
import clientSide.entities.Porter;
import clientSide.sharedRegionsStubs.*;
import comInf.Bag;
import comInf.MemException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientPassenger {

    public static void main(final String[] args) throws MemException {

        GenReposInfoStub reposStub;
        BaggageColPointStub bagColPointStub;
        BaggageReclaimOfficeStub bagRecOfficeStub;
        TemporaryStorageAreaStub tmpStorageAreaStub;
        ArrivalLoungeStub arrivLoungeStub;
        ArrivalTermTransfQuayStub arrivalQuayStub;
        DepartureTermTransfQuayStub departureQuayStub;
        ArrivalTerminalExitStub arrivalTermStub;
        DepartureTerminalEntranceStub departureTermStub;

        String fileName = "log.txt";
        int[][] bagAndPassDest = new int[SimulPar.N_PASS_PER_FLIGHT][SimulPar.N_FLIGHTS];
        int[][] nBagNR = new int[SimulPar.N_PASS_PER_FLIGHT][SimulPar.N_FLIGHTS];
        int[][] nBagsNA = new int[SimulPar.N_PASS_PER_FLIGHT][SimulPar.N_FLIGHTS];
        char opt;
        String serverHostName = null;                               // nome do sistema computacional onde está o servidor
        int serverPortNumb = -1;                                  // número do port de escuta do servidor
        BufferedReader br;
        /* Obtenção dos parâmetros do problema */



        File loggerFile = new File(fileName);
        try {
            if (loggerFile.createNewFile()) {
                System.out.print("File created: " + loggerFile.getName());
            } else {
                do {
                    System.out.print("There is already a file with this name. Delete it (y - yes; n - no)? ");
                    br = new BufferedReader(new InputStreamReader(System.in));
                    opt = 'y';
                    // opt = (char) br.read();
                } while((opt != 'y') && (opt != 'n'));
                if(opt == 'y'){
                    loggerFile.delete();
                    loggerFile.createNewFile();
                    System.out.print("File created: " + loggerFile.getName());
                }

                System.out.print("Nome do sistema computacional onde está o servidor? ");
                serverHostName = br.readLine();
                System.out.print("Número do port de escuta do servidor? ");
                serverPortNumb = br.read();
            }
        } catch (IOException e) {
            System.out.print("An error occurred.");
            e.printStackTrace();
        }

        for(int land = 0; land < SimulPar.N_FLIGHTS; land++){
            for(int nPass = 0; nPass < SimulPar.N_PASS_PER_FLIGHT; nPass++){
                // create destination for the passenger and his bags
                bagAndPassDest[nPass][land] = (Math.random() < 0.4) ? Bag.DestStat.FINAL.ordinal() : Bag.DestStat.TRANSIT.ordinal();

                // number of bags the passenger had at the beginning of his journey
                nBagNR[nPass][land] = (Math.random() < 0.5) ? 2 : (Math.random() < 0.5) ? 1 : 0;

                // number of bags of the passenger that weren't lost
                nBagsNA[nPass][land] = nBagNR[nPass][land];
                if(nBagsNA[nPass][land] > 0 && Math.random() < 0.2) {
                    // lose a bag 20% of the times
                    nBagsNA[nPass][land] -= 1;
                    // lose two bags bag 4% of the times
                    if ((nBagsNA[nPass][land] > 0) && (Math.random() < 0.2)) {
                        nBagsNA[nPass][land] -= 1;
                    }
                }
            }
        }

        /* instantiation of the shared regions */
        reposStub = new GenReposInfoStub(serverHostName, serverPortNumb);
        reposStub.probPar(fileName);
        bagRecOfficeStub = new BaggageReclaimOfficeStub(serverHostName, serverPortNumb);
        bagRecOfficeStub.probPar(reposStub);
        tmpStorageAreaStub = new TemporaryStorageAreaStub(serverHostName, serverPortNumb);
        tmpStorageAreaStub.probPar(reposStub);
        departureQuayStub = new DepartureTermTransfQuayStub(serverHostName, serverPortNumb);
        departureQuayStub.probPar(reposStub);
        bagColPointStub = new BaggageColPointStub(serverHostName, serverPortNumb);
        bagColPointStub.probPar(reposStub);
        arrivalQuayStub = new ArrivalTermTransfQuayStub(serverHostName, serverPortNumb);
        arrivalQuayStub.probPar(reposStub);
        arrivLoungeStub = new ArrivalLoungeStub(serverHostName, serverPortNumb);
        arrivLoungeStub.probPar(reposStub, bagColPointStub, arrivalQuayStub, bagAndPassDest, nBagsNA);
        arrivalTermStub = new ArrivalTerminalExitStub(serverHostName, serverPortNumb);
        arrivalTermStub.probPar(reposStub, arrivLoungeStub, arrivalQuayStub);
        departureTermStub = new DepartureTerminalEntranceStub(serverHostName, serverPortNumb);
        departureTermStub.probPar(reposStub, arrivLoungeStub, arrivalQuayStub);
        arrivalTermStub.setDepartureTerminalRef(departureTermStub);
        departureTermStub.setArrivalTerminalRef(arrivalTermStub);
        arrivLoungeStub.setDepartureTerminalRef(departureTermStub);

        /* instantiation of the entities */
        Passenger[][] passengers = new Passenger[SimulPar.N_PASS_PER_FLIGHT][SimulPar.N_FLIGHTS];
        Porter porter;
        BusDriver busDriver;

        porter = new Porter(PorterStates.WAITING_FOR_A_PLANE_TO_LAND, arrivLoungeStub, tmpStorageAreaStub,
                bagColPointStub);

        reposStub.updatePorterStat(PorterStates.WAITING_FOR_A_PLANE_TO_LAND);

        busDriver = new BusDriver(BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL, arrivalQuayStub,departureQuayStub,
                reposStub);

        reposStub.updateBDriverStat(BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL);

        porter.start();
        busDriver.start();

        for(int flight = 0; flight < SimulPar.N_FLIGHTS; flight++){
            for(int nPass = 0; nPass < SimulPar.N_PASS_PER_FLIGHT; nPass++){
                Passenger.SiPass Si = (bagAndPassDest[nPass][flight] == Bag.DestStat.FINAL.ordinal()) ? Passenger.SiPass.FDT
                        : Passenger.SiPass.TRT;
                passengers[nPass][flight] = new Passenger(PassengerStates.AT_THE_DISEMBARKING_ZONE, Si,
                        nBagNR[nPass][flight], 0, nPass, arrivLoungeStub, arrivalQuayStub, departureQuayStub,
                        departureTermStub, arrivalTermStub, bagColPointStub, bagRecOfficeStub);

                reposStub.updatePassSt(passengers[nPass][flight].getPassengerID(), PassengerStates.AT_THE_DISEMBARKING_ZONE);
                reposStub.getPassSi(passengers[nPass][flight].getPassengerID(),passengers[nPass][flight].getSi().toString());
            }

            for(int nPass = 0; nPass < SimulPar.N_PASS_PER_FLIGHT; nPass++){
                passengers[nPass][flight].start();
            }

            for(int nPass = 0; nPass < SimulPar.N_PASS_PER_FLIGHT; nPass++) {
                try {
                    passengers[nPass][flight].join();
                } catch (InterruptedException e) {
                    System.out.print("Main Program - One thread of Passenger " + nPass + " from flight " + flight +
                            " was interrupted.");
                }
            }

            bagColPointStub.resetBaggageColPoint();
            tmpStorageAreaStub.resetTemporaryStorageArea();
            arrivalQuayStub.resetArrivalTermTransfQuay();
            arrivLoungeStub.resetArrivalLounge(bagAndPassDest, nBagsNA);
            departureQuayStub.resetDepartureTermTransfQuay();
            arrivalTermStub.resetArrivalTerminalExit();
        }

        try {
            porter.join();
        } catch (InterruptedException e) {
            System.out.print("Main Program - One thread of Porter was interrupted.");
        }

        try {
            busDriver.join();
        } catch (InterruptedException e) {
            System.out.print("Main Program - One thread of BusDriver was interrupted.");
        }
        reposStub.finalReport();
    }
}

