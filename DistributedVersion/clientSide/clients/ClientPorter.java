package clientSide.clients;

import clientSide.SimulPar;
import clientSide.entities.*;
import clientSide.sharedRegionsStubs.*;
import comInf.MemException;

public class ClientPorter {

    public static void main(final String[] args) throws MemException {

        GenReposInfoStub reposStub;
        BaggageColPointStub bagColPointStub;
        TemporaryStorageAreaStub tmpStorageAreaStub;
        ArrivalLoungeStub arrivLoungeStub;

        String fileName = "log.txt";

        /* instantiation of the shared regions */
        reposStub = new GenReposInfoStub(SimulPar.genReposInfoHost, SimulPar.genReposInfoPort);
        tmpStorageAreaStub = new TemporaryStorageAreaStub(SimulPar.tmpStorageAreaHost, SimulPar.depTerminalEntrancePort);
        bagColPointStub = new BaggageColPointStub(SimulPar.bgCollectionPointHost, SimulPar.bgCollectionPointPort);
        arrivLoungeStub = new ArrivalLoungeStub(SimulPar.arrivalLoungeHost, SimulPar.arrivalLoungePort);

        /* instantiation of the entities */
        Porter porter;

        porter = new Porter(PorterStates.WAITING_FOR_A_PLANE_TO_LAND, arrivLoungeStub, tmpStorageAreaStub,
                bagColPointStub);

        reposStub.updatePorterStat(PorterStates.WAITING_FOR_A_PLANE_TO_LAND);

        porter.start();

        try {
            porter.join();
        } catch (InterruptedException e) {
            System.out.print("Main Program - One thread of Porter was interrupted.");
        }

        reposStub.finalReport();
    }
}
