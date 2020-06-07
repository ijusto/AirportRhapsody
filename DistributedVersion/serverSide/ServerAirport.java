package serverSide;

import serverSide.sharedRegions.GenReposInfo;
import serverSide.interfaces.*;
import comInf.Bag;
import comInf.MemException;
import serverSide.sharedRegions.*;

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

public class ServerAirport {

    /**
     *  Número do port de escuta do serviço a ser prestado (4000, por defeito)
     *
     *    @serialField portNumb
     */

    private static final int portNumb = 22001;
    public static boolean waitConnection;                              // sinalização de actividade

    /**
     *  Programa principal.
     */

    public static void main(final String[] args) throws MemException {

        GenReposInfo repos;
        BaggageColPoint bagColPoint;
        BaggageColPointInterface bagColPointInterface;
        BaggageReclaimOffice bagRecOffice;
        BaggageReclaimOfficeInterface bagRecOfficeInterface;
        TemporaryStorageArea tmpStorageArea;
        TemporaryStorageAreaInterface tmpStorageAreaInterface;
        ArrivalLounge arrivLounge;
        ArrivalLoungeInterface arrivLoungeInterface;
        ArrivalTermTransfQuay arrivalQuay;
        ArrivalTermTransfQuayInterface arrivalQuayInterface;
        DepartureTermTransfQuay departureQuay;
        DepartureTermTransfQuayInterface departureQuayInterface;
        ArrivalTerminalExit arrivalTerm;
        ArrivalTerminalExitInterface arrivalTermInterface;
        DepartureTerminalEntrance departureTerm;
        DepartureTerminalEntranceInterface departureTermInterface;

        ServerCom scon, sconi;                               // canais de comunicação
        ClientProxy cliProxy;                                // thread agente prestador do serviço

        /* estabelecimento do servico */

        scon = new ServerCom (portNumb);                     // criação do canal de escuta e sua associação
        scon.start ();                                       // com o endereço público
        bShop = new BarberShop ();                           // activação do serviço
        bShopInter = new BarberShopInterface (bShop);        // activação do interface com o serviço
        GenericIO.writelnString ("O serviço foi estabelecido!");
        GenericIO.writelnString ("O servidor esta em escuta.");

        String fileName = "log.txt";
        Bag.DestStat[][] bagAndPassDest = new Bag.DestStat[SimulPar.N_PASS_PER_FLIGHT][SimulPar.N_FLIGHTS];
        int[][] nBagNR = new int[SimulPar.N_PASS_PER_FLIGHT][SimulPar.N_FLIGHTS];
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

        for(int land = 0; land < SimulPar.N_FLIGHTS; land++){
            for(int nPass = 0; nPass < SimulPar.N_PASS_PER_FLIGHT; nPass++){
                // create destination for the passenger and his bags
                bagAndPassDest[nPass][land] = (Math.random() < 0.4) ? Bag.DestStat.FINAL : Bag.DestStat.TRANSIT;

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
        repos = new GenReposInfo(fileName);
        bagRecOffice = new BaggageReclaimOffice(repos);
        tmpStorageArea = new TemporaryStorageArea(repos);
        departureQuay = new DepartureTermTransfQuay(repos);
        bagColPoint = new BaggageColPoint(repos);
        arrivalQuay = new ArrivalTermTransfQuay(repos);
        arrivLounge = new ArrivalLounge(repos, bagColPoint, arrivalQuay, bagAndPassDest, nBagsNA);
        arrivalTerm = new ArrivalTerminalExit(repos, arrivLounge, arrivalQuay);
        departureTerm = new DepartureTerminalEntrance(repos, arrivLounge, arrivalQuay);
        arrivalTerm.setDepartureTerminalRef(departureTerm);
        departureTerm.setArrivalTerminalRef(arrivalTerm);
        arrivLounge.setDepartureTerminalRef(departureTerm);

        /* instantiation of the entities */
        Passenger[][] passengers = new Passenger[SimulPar.N_PASS_PER_FLIGHT][SimulPar.N_FLIGHTS];
        Porter porter;
        BusDriver busDriver;

        porter = new Porter(PorterStates.WAITING_FOR_A_PLANE_TO_LAND, arrivLounge, tmpStorageArea, bagColPoint);

        repos.updatePorterStat(PorterStates.WAITING_FOR_A_PLANE_TO_LAND);

        busDriver = new BusDriver(BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL, arrivalQuay,departureQuay,repos);

        repos.updateBDriverStat(BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL);

        porter.start();
        busDriver.start();

        for(int flight = 0; flight < SimulPar.N_FLIGHTS; flight++){
            for(int nPass = 0; nPass < SimulPar.N_PASS_PER_FLIGHT; nPass++){
                Passenger.SiPass Si = (bagAndPassDest[nPass][flight] == Bag.DestStat.FINAL) ? Passenger.SiPass.FDT
                                                                                            : Passenger.SiPass.TRT;
                passengers[nPass][flight] = new Passenger(PassengerStates.AT_THE_DISEMBARKING_ZONE, Si,
                        nBagNR[nPass][flight], 0, nPass, arrivLounge, arrivalQuay, departureQuay,
                        departureTerm, arrivalTerm, bagColPoint, bagRecOffice);

                repos.updatePassSt(passengers[nPass][flight].getPassengerID(), PassengerStates.AT_THE_DISEMBARKING_ZONE);
                repos.getPassSi(passengers[nPass][flight].getPassengerID(),passengers[nPass][flight].getSi().toString());
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

            bagColPoint.resetBaggageColPoint();
            tmpStorageArea.resetTemporaryStorageArea();
            arrivalQuay.resetArrivalTermTransfQuay();
            arrivLounge.resetArrivalLounge(bagAndPassDest, nBagsNA);
            departureQuay.resetDepartureTermTransfQuay();
            arrivalTerm.resetArrivalTerminalExit();
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
        repos.finalReport();
    }
}
