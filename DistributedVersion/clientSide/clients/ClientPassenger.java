package clientSide.clients;

import comInf.SimulPar;
import clientSide.entities.*;
import clientSide.entities.Passenger;
import clientSide.sharedRegionsStubs.*;
import comInf.Bag;
import comInf.MemException;

/**
 *
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class ClientPassenger {

    public static void main(final String[] args){
        System.out.println("Passenger Client");
        GenReposInfoStub reposStub;
        BaggageColPointStub bagColPointStub;
        BaggageReclaimOfficeStub bagRecOfficeStub;
        TemporaryStorageAreaStub tmpStorageAreaStub;
        ArrivalLoungeStub arrivLoungeStub;
        ArrivalTermTransfQuayStub arrivalQuayStub;
        DepartureTermTransfQuayStub departureQuayStub;
        ArrivalTerminalExitStub arrivalTermStub;
        DepartureTerminalEntranceStub departureTermStub;

        int[][] bagAndPassDest = new int[SimulPar.N_PASS_PER_FLIGHT][SimulPar.N_FLIGHTS];
        int[][] nBagNR = new int[SimulPar.N_PASS_PER_FLIGHT][SimulPar.N_FLIGHTS];
        int[][] nBagsNA = new int[SimulPar.N_PASS_PER_FLIGHT][SimulPar.N_FLIGHTS];



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
        reposStub = new GenReposInfoStub(SimulPar.genReposInfoHost, SimulPar.genReposInfoPort);
        bagRecOfficeStub = new BaggageReclaimOfficeStub(SimulPar.bgrOfficeHost, SimulPar.bgrOfficePort);
        tmpStorageAreaStub = new TemporaryStorageAreaStub(SimulPar.tmpStorageAreaHost, SimulPar.tmpStorageAreaPort);
        departureQuayStub = new DepartureTermTransfQuayStub(SimulPar.depTTQuayHost, SimulPar.depTTQuayPort);
        bagColPointStub = new BaggageColPointStub(SimulPar.bgCollectionPointHost, SimulPar.bgCollectionPointPort);
        arrivalQuayStub = new ArrivalTermTransfQuayStub(SimulPar.arrivalTTQuayHost, SimulPar.arrivalTTQuayPort);
        arrivLoungeStub = new ArrivalLoungeStub(SimulPar.arrivalLoungeHost, SimulPar.arrivalLoungePort);
        arrivalTermStub = new ArrivalTerminalExitStub(SimulPar.arrivalTermExitHost, SimulPar.arrivalTermExitPort);
        departureTermStub = new DepartureTerminalEntranceStub(SimulPar.depTerminalEntranceHost, SimulPar.depTerminalEntrancePort);
        arrivalTermStub.setDepartureTerminalRef(departureTermStub);
        departureTermStub.setArrivalTerminalRef(arrivalTermStub);
        arrivLoungeStub.setDepartureTerminalRef(departureTermStub);

        /* instantiation of the entities */
        Passenger[][] passengers = new Passenger[SimulPar.N_PASS_PER_FLIGHT][SimulPar.N_FLIGHTS];

        for(int flight = 0; flight < SimulPar.N_FLIGHTS; flight++){
            for(int nPass = 0; nPass < SimulPar.N_PASS_PER_FLIGHT; nPass++){
                Passenger.SiPass Si = (bagAndPassDest[nPass][flight] == Bag.DestStat.FINAL.ordinal()) ? Passenger.SiPass.FDT
                        : Passenger.SiPass.TRT;
                passengers[nPass][flight] = new Passenger(PassengerStates.AT_THE_DISEMBARKING_ZONE, Si,
                        nBagNR[nPass][flight], 0, nPass, arrivLoungeStub, arrivalQuayStub, departureQuayStub,
                        departureTermStub, arrivalTermStub, bagColPointStub, bagRecOfficeStub);

                reposStub.updatePassSt(passengers[nPass][flight].getPassengerID(), PassengerStates.AT_THE_DISEMBARKING_ZONE.ordinal());
                reposStub.getPassSi(passengers[nPass][flight].getPassengerID(),passengers[nPass][flight].getSi().ordinal());
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

        while(!reposStub.finalReport());

        reposStub.shutdown();
        bagColPointStub.shutdown();
        bagRecOfficeStub.shutdown();
        tmpStorageAreaStub.shutdown();
        arrivLoungeStub.shutdown();
        arrivalQuayStub.shutdown();
        departureQuayStub.shutdown();
        arrivalTermStub.shutdown();
        departureTermStub.shutdown();
    }
}

