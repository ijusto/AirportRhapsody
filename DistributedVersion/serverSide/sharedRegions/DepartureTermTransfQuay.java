package serverSide.sharedRegions;

import clientSide.entities.BusDriverStates;
import clientSide.entities.PassengerStates;
import clientSide.sharedRegionsStubs.GenReposInfoStub;
import comInf.CommonProvider;

/**
 *   Departure Terminal Transfer Quay.
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class DepartureTermTransfQuay {

    /*
     *   General Repository of Information Stub.
     */

    private GenReposInfoStub reposStub;

    /*
     *   Signaling the bus driver will to let the passengers enter the bus.
     */
    private boolean busDoorsOpen;

    /*
     *   Counter of passengers on the bus.
     */

    private int nPassOnTheBus;

    /**
     *   Object used for synchronization.
     */

    private static final Object lockNPassOnTheBus = new Object();

    /**
     *   Object used for synchronization.
     */

    private static final Object lockBusDoorsOpen = new Object();


    /**
     *   Instantiation of the Departure Terminal Transfer Quay.
     *
     *     @param reposStub general repository of information Stub.
     */

    public DepartureTermTransfQuay(GenReposInfoStub reposStub){
        this.reposStub = reposStub;
        youCantLeaveTheBus();
        this.resetNPassOnTheBusValue();
    }

    /* ************************************************Passenger***************************************************** */

    /**
     *   Operation of leaving the bus (raised by the Passenger).
     *   Before leaving, the passenger waits for a notification of the bus driver to let her/him leave.
     */

    public synchronized void leaveTheBus(int id){
        CommonProvider passenger = (CommonProvider) Thread.currentThread();
        assert(passenger.getPassStat(id) == PassengerStates.TERMINAL_TRANSFER);
        passenger.setStatPass(id, PassengerStates.AT_THE_DEPARTURE_TRANSFER_TERMINAL);
        reposStub.updatePassSt(id,PassengerStates.AT_THE_DEPARTURE_TRANSFER_TERMINAL.ordinal());

        while(!this.canPassLeaveTheBus()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        boolean last = this.incDecCounter(false);

        reposStub.freeBusSeat(id);
        reposStub.printLog();

        // if the passenger is the last to exit the bus
        if(last){
            // wake up Bus Driver in parkTheBusAndLetPassOff()
            notifyAll();
        }
    }

    /* *************************************************Bus Driver*************************************************** */

    /**
     *   BusDriver informs the passengers they can leave the bus (raised by the BusDriver).
     *   Then he waits for a notification of the last passenger to leave the bus.
     */

    public synchronized void parkTheBusAndLetPassOff() {

        CommonProvider busDriver = (CommonProvider) Thread.currentThread();
        assert(busDriver.getBDStat() == BusDriverStates.DRIVING_FORWARD);
        busDriver.setBDStat(BusDriverStates.PARKING_AT_THE_DEPARTURE_TERMINAL);
        reposStub.updateBDriverStat(BusDriverStates.PARKING_AT_THE_DEPARTURE_TERMINAL.ordinal());
        reposStub.printLog();

        this.setNPassOnTheBusValue(busDriver.getNPassOnTheBus());

        this.pleaseLeaveTheBus();
        notifyAll();  // wake up Passengers in leaveTheBus()

        while(this.getNPassOnTheBusValue() != 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        busDriver.setNPassOnTheBus(this.getNPassOnTheBusValue());
        youCantLeaveTheBus();
    }

    /**
     *   Operation of incrementing/decrementing the counter.
     *
     *    @return <li>true, if the value of the counter after the operation is the limit.</li>
     *            <li>false, otherwise.</li>
     */

    public boolean incDecCounter(boolean inc) {
        synchronized (lockNPassOnTheBus) {
            if(inc) {
                nPassOnTheBus++;
            } else {
                nPassOnTheBus--;
            }
            return nPassOnTheBus == 0;
        }
    }

    /**
     *   Sets the value of the counter to zero.
     */

    public void resetNPassOnTheBusValue(){
        synchronized (lockNPassOnTheBus) { // Locks on the private Object
            nPassOnTheBus = 0;
        }
    }

    /**
     *   Resets the counter of passengers on the bus.
     */

    public synchronized void resetDepartureTermTransfQuay(){
        this.resetNPassOnTheBusValue();
    }

    /* ************************************************* Getters ******************************************************/

    /**
     *   Signaling the bus driver will to let the passengers enter the bus.
     *
     *    @return <li>true, if the bus driver wants the passengers to leave the bus</li>
     *            <li>false, otherwise</li>
     */

    public boolean canPassLeaveTheBus() {
        synchronized (lockBusDoorsOpen) {
            return this.busDoorsOpen;
        }
    }

    /**
     *   Getter for the value of the of ths counter of passengers on the bus.
     *
     *    @return the value of the counter.
     */

    public int getNPassOnTheBusValue(){
        synchronized (lockNPassOnTheBus) {
            return nPassOnTheBus;
        }
    }

    /* ************************************************* Setters ******************************************************/

    /**
     *   Sets the bus driver will to let the passengers enter the bus to true.
     */

    public void pleaseLeaveTheBus() {
        synchronized (lockBusDoorsOpen) {
            this.busDoorsOpen = true;
        }
    }

    public void youCantLeaveTheBus(){
        synchronized (lockBusDoorsOpen) {
            this.busDoorsOpen = false;
        }
    }

    /**
     *   Sets the number of passengers on the bus.
     *
     *   @param nPassOnTheBus Counter of passengers on the bus.
     */

    public void setNPassOnTheBusValue(int nPassOnTheBus){
        synchronized (lockNPassOnTheBus) {
            this.nPassOnTheBus = nPassOnTheBus;
        }
    }

}
