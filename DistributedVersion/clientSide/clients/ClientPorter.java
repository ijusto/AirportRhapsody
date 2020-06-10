package clientSide.clients;

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
        String serverHostName = null;  // name of the computational system where the server is
        int serverPortNumb = -1;  // server listening port number

        /* instantiation of the shared regions */
        reposStub = new GenReposInfoStub(serverHostName, serverPortNumb);
        tmpStorageAreaStub = new TemporaryStorageAreaStub(serverHostName, serverPortNumb);
        bagColPointStub = new BaggageColPointStub(serverHostName, serverPortNumb);
        arrivLoungeStub = new ArrivalLoungeStub(serverHostName, serverPortNumb);

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
