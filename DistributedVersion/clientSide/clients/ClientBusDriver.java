package clientSide.clients;

import clientSide.BusDriverStates;
import clientSide.SimulPar;
import clientSide.entities.BusDriver;
import clientSide.entities.Passenger;
import clientSide.entities.Porter;
import clientSide.sharedRegionsStubs.*;
import comInf.Bag;
import comInf.MemException;

import java.io.*;
import java.net.*;

public class ClientBusDriver {

    public static void main(final String[] args) throws MemException {

        GenReposInfo repos;
        BaggageColPointStub bagColPointStub;
        BaggageReclaimOfficeStub bagRecOfficeStub;
        TemporaryStorageAreaStub tmpStorageAreaStub;
        ArrivalLoungeStub arrivLoungeStub;
        ArrivalTermTransfQuayStub arrivalQuayStub;
        DepartureTermTransfQuayStub departureQuayStub;
        ArrivalTerminalExitStub arrivalTermStub;
        DepartureTerminalEntranceStub departureTermStub;
        String serverHostName;                               // nome do sistema computacional onde está o servidor
        int serverPortNumb;                                  // número do port de escuta do servidor

        String fileName = "log.txt";
        Bag.DestStat[][] bagAndPassDest = new Bag.DestStat[SimulPar.N_PASS_PER_FLIGHT][SimulPar.N_FLIGHTS];
        int[][] nBagsNA = new int[SimulPar.N_PASS_PER_FLIGHT][SimulPar.N_FLIGHTS];
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
                    // opt = (char) br.read();
                } while((opt != 'y') && (opt != 'n'));
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

        /* instantiation of the shared regions */
        repos = new GenReposInfo(fileName);
        bagRecOfficeStub = new BaggageReclaimOfficeStub(serverHostName, serverPortNumb);
        tmpStorageAreaStub = new TemporaryStorageAreaStub(serverHostName, serverPortNumb);
        departureQuayStub = new DepartureTermTransfQuayStub(serverHostName, serverPortNumb);
        bagColPointStub = new BaggageColPointStub(serverHostName, serverPortNumb);
        arrivalQuayStub = new ArrivalTermTransfQuayStub(serverHostName, serverPortNumb);
        arrivLoungeStub = new ArrivalLoungeStub(serverHostName, serverPortNumb);
        arrivalTermStub = new ArrivalTerminalExitStub(serverHostName, serverPortNumb);
        departureTermStub = new DepartureTerminalEntranceStub(serverHostName, serverPortNumb);
        arrivalTermStub.setDepartureTerminalRef(serverHostName, serverPortNumb);
        departureTermStub.setArrivalTerminalRef(serverHostName, serverPortNumb);
        arrivLoungeStub.setDepartureTerminalRef(serverHostName, serverPortNumb);

        /* instantiation of the entities */
        BusDriver busDriver;

        busDriver = new BusDriver(BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL, arrivalQuayStub, departureQuayStub,repos);

        repos.updateBDriverStat(BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL);

        busDriver.start();

        try {
            busDriver.join ();
        }
        catch (InterruptedException e) {
            System.out.print("Main Program - One thread of BusDriver was interrupted.");
        }
        System.out.println("O busDriver terminou.");

        System.out.println();
        bShopStub.shutdown ();

        repos.finalReport();
    }
}
