package clientSide.clients;

import clientSide.entities.*;
import clientSide.SimulPar;
import clientSide.entities.BusDriver;
import clientSide.sharedRegionsStubs.*;
import comInf.Bag;
import comInf.MemException;

import java.io.*;
import java.net.*;

public class ClientBusDriver {

    public static void main(final String[] args) throws MemException {

        GenReposInfoStub reposStub;
        ArrivalTermTransfQuayStub arrivalQuayStub;
        DepartureTermTransfQuayStub departureQuayStub;

        String fileName = "log.txt";
        String serverHostName = null;  // name of the computational system where the server is
        int serverPortNumb = -1;  // server listening port number


        /* instantiation of the shared regions */
        reposStub = new GenReposInfoStub(serverHostName, serverPortNumb);
        departureQuayStub = new DepartureTermTransfQuayStub(serverHostName, serverPortNumb);
        arrivalQuayStub = new ArrivalTermTransfQuayStub(serverHostName, serverPortNumb);

        /* instantiation of the entities */
        BusDriver busDriver;

        busDriver = new BusDriver(BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL, arrivalQuayStub,departureQuayStub,
                                    reposStub);

        reposStub.updateBDriverStat(BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL);

        busDriver.start();

        try {
            busDriver.join();
        } catch (InterruptedException e) {
            System.out.print("Main Program - One thread of BusDriver was interrupted.");
        }
        reposStub.finalReport();
    }
}
