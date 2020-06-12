package clientSide.entities;
import clientSide.sharedRegionsStubs.ArrivalTermTransfQuayStub;
import clientSide.sharedRegionsStubs.DepartureTermTransfQuayStub;
import clientSide.sharedRegionsStubs.GenReposInfoStub;

/**
 *   Bus driver thread.
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class BusDriver extends Thread{

    /**
     *   General Repository of Information Stub.
     *
     *    @serialField reposStub
     */

    GenReposInfoStub reposStub;

    /**
     *   Arrival Terminal Transfer Quay Stub.
     *
     *    @serialField arrivalTerminalQuayStub
     */

    ArrivalTermTransfQuayStub arrivalTerminalQuayStub;

    /**
     *   Departure Terminal Transfer Quay Stub.
     *
     *    @serialField departureTransferQuayStub
     */

    DepartureTermTransfQuayStub departureTransferQuayStub;

    /**
     *   State of the driver.
     *
     *    @serialField Stat
     */

    private BusDriverStates Stat;

    /**
     *   Number of passengers on the bus.
     *
     *    @serialField nPass
     */

    private int nPassOnTheBus;

    /**
     *   Instantiation of the thread BusDriver.
     *
     *    @param Stat State of the driver.
     *    @param arrivalTerminalQuayStub Arrival Terminal Transfer Quay Stub.
     *    @param departureTransferQuayStub Departure Terminal Transfer Quay Stub.
     *    @param reposStub General Repository of Information.
     */

    public BusDriver(BusDriverStates Stat, ArrivalTermTransfQuayStub arrivalTerminalQuayStub,
                     DepartureTermTransfQuayStub departureTransferQuayStub, GenReposInfoStub reposStub){
        this.Stat = Stat;
        this.arrivalTerminalQuayStub = arrivalTerminalQuayStub;
        this.departureTransferQuayStub = departureTransferQuayStub;
        this.reposStub = reposStub;
        this.nPassOnTheBus = 0;
    }

    /**
     *   Life cycle of the thread BusDriver.
     */

    @Override
    public void run() {
        while(arrivalTerminalQuayStub.hasDaysWorkEnded() != 'F'){
            arrivalTerminalQuayStub.announcingBusBoarding();
            goToDepartureTerminal();
            departureTransferQuayStub.parkTheBusAndLetPassOff();
            goToArrivalTerminal();
            arrivalTerminalQuayStub.parkTheBus();
        }
    }

    /**
     *   Operation of changing quay, from the departure terminal quay to the arrival terminal quay.
     */

    public synchronized void goToArrivalTerminal() {
        assert(this.getStat() == BusDriverStates.PARKING_AT_THE_DEPARTURE_TERMINAL);
        this.setStat(BusDriverStates.DRIVING_BACKWARD);
        reposStub.updateBDriverStat(BusDriverStates.DRIVING_BACKWARD.ordinal());

        // simulates the bus going from the departure terminal to the arrival terminal
        try {
            sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *   Operation of changing quay, from the arrival terminal quay to the departure terminal quay.
     */

    public synchronized void goToDepartureTerminal(){
        assert(this.getStat() == BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL);
        this.setStat(BusDriverStates.DRIVING_FORWARD);
        reposStub.updateBDriverStat(BusDriverStates.DRIVING_FORWARD.ordinal());
        reposStub.printLog();

        // simulates the bus going from the departure arrival to the departure terminal
        try {
            sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /* ************************************************* Getters ******************************************************/

    /**
     *   Gets the state of the BusDriver.
     *
     *    @return State of the BusDriver.
     */


    public BusDriverStates getStat() {
        return Stat;
    }


    /**
     *   Gets the number os passengers on the bus.
     *
     *    @return number os passengers on the bus.
     */


    public int getNPassOnTheBus() {
        return this.nPassOnTheBus;
    }

    /* ************************************************* Setters ******************************************************/

    /**
     *   Sets the Bus Driver State.
     *
     *    @param stat Bus Driver State.
     */


    public void setStat(BusDriverStates stat) {
        Stat = stat;
    }

    /**
     *   Sets the Number os passengers on the bus.
     *
     *    @param nPassOnTheBus Number os passengers on the bus.
     */


    public void setNPassOnTheBus(int nPassOnTheBus) {
        this.nPassOnTheBus = nPassOnTheBus;
    }

}
