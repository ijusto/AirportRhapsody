package clientSide.clients;

import comInf.SimulPar;
import clientSide.entities.*;
import clientSide.sharedRegionsStubs.*;
import comInf.MemException;

/**
 *
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class ClientPorter {

    public static void main(final String[] args){
        System.out.println("Porter Client");
        GenReposInfoStub reposStub;
        BaggageColPointStub bagColPointStub;
        TemporaryStorageAreaStub tmpStorageAreaStub;
        ArrivalLoungeStub arrivLoungeStub;

        /* instantiation of the shared regions */
        reposStub = new GenReposInfoStub(SimulPar.genReposInfoHost, SimulPar.genReposInfoPort);
        tmpStorageAreaStub = new TemporaryStorageAreaStub(SimulPar.tmpStorageAreaHost, SimulPar.tmpStorageAreaPort);
        bagColPointStub = new BaggageColPointStub(SimulPar.bgCollectionPointHost, SimulPar.bgCollectionPointPort);
        arrivLoungeStub = new ArrivalLoungeStub(SimulPar.arrivalLoungeHost, SimulPar.arrivalLoungePort);

        /* instantiation of the entities */
        Porter porter;

        porter = new Porter(PorterStates.WAITING_FOR_A_PLANE_TO_LAND, arrivLoungeStub, tmpStorageAreaStub,
                bagColPointStub);

        reposStub.updatePorterStat(PorterStates.WAITING_FOR_A_PLANE_TO_LAND.ordinal());

        porter.start();

        try {
            porter.join();
        } catch (InterruptedException e) {
            System.out.print("Main Program - One thread of Porter was interrupted.");
        }

        while(!reposStub.finalReport(2));

        reposStub.shutdown();
        bagColPointStub.shutdown();
        tmpStorageAreaStub.shutdown();
        arrivLoungeStub.shutdown();
    }
}
