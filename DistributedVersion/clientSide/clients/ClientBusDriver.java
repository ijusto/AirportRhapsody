package clientSide.clients;

import clientSide.entities.*;
import comInf.SimulPar;
import clientSide.entities.BusDriver;
import clientSide.sharedRegionsStubs.*;
import comInf.MemException;

public class ClientBusDriver {

    public static void main(final String[] args) throws MemException {

        GenReposInfoStub reposStub;
        ArrivalTermTransfQuayStub arrivalQuayStub;
        DepartureTermTransfQuayStub departureQuayStub;

        String fileName = "log.txt";

        /* instantiation of the shared regions */
        reposStub = new GenReposInfoStub(SimulPar.genReposInfoHost, SimulPar.genReposInfoPort);
        departureQuayStub = new DepartureTermTransfQuayStub(SimulPar.depTTQuayHost, SimulPar.depTTQuayPort);
        arrivalQuayStub = new ArrivalTermTransfQuayStub(SimulPar.arrivalTTQuayHost, SimulPar.arrivalTTQuayPort);

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
