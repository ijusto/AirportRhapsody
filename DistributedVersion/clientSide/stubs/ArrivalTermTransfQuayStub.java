package clientSide.stubs;

import clientSide.BusDriverStates;
import clientSide.PassengerStates;
import clientSide.entities.BusDriver;
import clientSide.entities.Passenger;
import comInf.MemException;
import comInf.MemFIFO;

public class ArrivalTermTransfQuayStub {

    /**
     *  Nome do sistema computacional onde está localizado o servidor
     *    @serialField serverHostName
     */

    private String serverHostName = null;

    /**
     *  Número do port de escuta do servidor
     *    @serialField serverPortNumb
     */

    private int serverPortNumb;

    /**
     *  Instanciação do stub à barbearia.
     *
     *    @param hostName nome do sistema computacional onde está localizado o servidor
     *    @param port número do port de escuta do servidor
     */

    public ArrivalTermTransfQuayStub (String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /* ************************************************Passenger***************************************************** */

    /**
     *   Operation of taking a Bus (raised by the Passenger).
     *   Before blocking, the passenger wakes up the bus driver, if the passenger's place in the waiting line equals
     *   the bus capacity, and is waken up by the operation announcingBusBoarding of the driver to mimic her entry
     *   in the bus.
     */

    public synchronized void takeABus() {

        Passenger passenger = (Passenger) Thread.currentThread();
        assert(passenger.getSt() == PassengerStates.AT_THE_DISEMBARKING_ZONE);
        passenger.setSt(PassengerStates.AT_THE_ARRIVAL_TRANSFER_TERMINAL);
        repos.updatePassSt(passenger.getPassengerID(), PassengerStates.AT_THE_ARRIVAL_TRANSFER_TERMINAL);
        repos.printLog();

        try {
            waitingLine.write(passenger);
            repos.pJoinWaitingQueue(passenger.getPassengerID());
        } catch (MemException e) {
            e.printStackTrace();
        }

        if(waitingLine.getNObjects() == main.SimulPar.BUS_CAP){
            // wake up Bus Driver in parkTheBus()
            notifyAll();
        }

        while(!this.allowBoardBus || this.getNPassAllowedToEnterValue() >= main.SimulPar.BUS_CAP){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.incDecNPassAllowedToEnterCounter(true);

        repos.printLog();
    }

    /**
     *   Operation of entering the bus (raised by the Passenger).
     *   If the passenger that raises this operation is the last passenger to enter the bus, he notifies the bus driver
     *   in announcingBusBoarding, who is waiting for all the passenger to enter.
     */

    public synchronized void enterTheBus(){

        Passenger passenger = (Passenger) Thread.currentThread();
        assert(passenger.getSt() == PassengerStates.AT_THE_ARRIVAL_TRANSFER_TERMINAL);
        assert(this.getNPassOnTheBusValue() < main.SimulPar.BUS_CAP);

        passenger.setSt(PassengerStates.TERMINAL_TRANSFER);
        repos.updatePassSt(passenger.getPassengerID(),PassengerStates.TERMINAL_TRANSFER);

        try{
            this.waitingLine.read();
            repos.pLeftWaitingQueue(passenger.getPassengerID());

            boolean last = this.incDecNPassOnTheBusCounter(true);

            // last passenger to enter the bus wakes up the bus driver
            if(last || this.waitingLine.isEmpty()){
                // wake up Bus driver in announcingBusBoarding()
                notifyAll();
            }
        } catch (MemException e) {
            e.printStackTrace();
        }

        repos.printLog();
    }

    /* ************************************************Bus Driver**************************************************** */

    /**
     *   Operation of checking if all the flights ended (raised by the BusDriver).
     *
     *     @return <li> 'F', if end of day </li>
     *             <li> 'R', otherwise </li>
     */

    public synchronized char hasDaysWorkEnded(){

        BusDriver busDriver = (BusDriver) Thread.currentThread();
        assert(busDriver.getStat() == BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL);

        // if the last flight arrived and all passengers left the airport, end the bus driver life cycle
        if(this.endDay){

            repos.printLog();
            return 'F';
        }

        repos.printLog();
        return 'R';
    }

    /**
     *   Operation of parking the bus (raised by the BusDriver).
     *   The Bus Driver is waits for a notification of the third (bus capacity) passenger to join the waiting line
     *   or for the parking time to come to an end. If the time has come to start driving and there is a passenger in
     *   the line or he was notified by the third passenger in the line, he wakes up. Otherwise, he keeps waiting.
     */

    public synchronized void parkTheBus(){

        BusDriver busDriver = (BusDriver) Thread.currentThread();
        assert(busDriver.getStat() == BusDriverStates.DRIVING_BACKWARD);
        busDriver.setStat(BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL);

        repos.updateBDriverStat(BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL);

        repos.printLog();
        this.resetNPassOnTheBus();

        while(waitingLine.getNObjects() != main.SimulPar.BUS_CAP){ // if false waken up by takeABus()
            try {
                // wait until the departure time has been reached and verify again if there are passengers waiting
                wait(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // if the last flight arrived and all passengers left the airport, stop waiting
            if(this.endDay){
                break;
            }

            // if there are passengers waiting
            if(!this.waitingLine.isEmpty()){
                break;
            }
        }

    }

    /**
     *   Operation of announcing the bus boarding to the passengers in the waiting line (raised by the BusDriver).
     *   The bus driver signals the bus driver's will to let the passengers enter the bus and waits for the last
     *   passenger to notify him after he enters.
     */

    public synchronized void announcingBusBoarding(){

        BusDriver busDriver = (BusDriver) Thread.currentThread();
        assert(busDriver.getStat() == BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL);

        this.allowBoardBus = true;

        // wake up Passengers in takeABus()
        notifyAll();

        while(!(this.getNPassOnTheBusValue() == main.SimulPar.BUS_CAP || this.waitingLine.isEmpty())){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.allowBoardBus = false;
        this.resetNPassAllowedToEnter();
        busDriver.setNPassOnTheBus(this.getNPassOnTheBusValue());

    }

    /**
     *   Sets the value of the counter to zero.
     */

    public void resetNPassAllowedToEnter(){
        synchronized (lockNPassAllowedToEnterCounter) { // Locks on the private Object
            nPassAllowedToEnter = 0;
        }
    }

    /**
     *   Operation of incrementing/decrementing the counter.
     *
     *    @return <li>true, if the value of the counter after the operation is the limit.</li>
     *            <li>false, otherwise.</li>
     */

    public boolean incDecNPassAllowedToEnterCounter(boolean inc) {
        synchronized (lockNPassAllowedToEnterCounter) {
            if(inc) {
                nPassAllowedToEnter++;
            } else {
                nPassAllowedToEnter--;
            }
            return nPassAllowedToEnter == main.SimulPar.N_PASS_PER_FLIGHT;
        }
    }

    /**
     *   Sets the value of the counter to zero.
     */

    public void resetNPassOnTheBus(){
        synchronized (lockNPassOnTheBusCounter) { // Locks on the private Object
            nPassOnTheBus = 0;
        }
    }

    /**
     *   Operation of incrementing/decrementing the counter.
     *
     *    @return <li>true, if the value of the counter after the operation is the limit.</li>
     *            <li>false, otherwise.</li>
     */

    public boolean incDecNPassOnTheBusCounter(boolean inc) {
        synchronized (lockNPassOnTheBusCounter) {
            if(inc) {
                nPassOnTheBus++;
            } else {
                nPassOnTheBus--;
            }
            return nPassOnTheBus == main.SimulPar.BUS_CAP;
        }
    }

    /**
     *   Resets that need to be done after the passengers leave the airport.
     */

    public synchronized void resetArrivalTermTransfQuay() throws MemException {
        this.waitingLine = new MemFIFO<>(new Passenger [main.SimulPar.N_PASS_PER_FLIGHT]);  // FIFO instantiation
        this.resetNPassOnTheBus();
        this.resetNPassAllowedToEnter();
        this.allowBoardBus = false;
    }

    /* ************************************************* Getters ******************************************************/

    /**
     *   Gets the value of the counter of passengers that were allowed to enter the bus.
     *
     *    @return Value of passengers that were allowed to enter the bus.
     */

    public int getNPassAllowedToEnterValue(){
        synchronized (lockNPassAllowedToEnterCounter) {
            return nPassAllowedToEnter;
        }
    }

    /**
     *   Gets the value of the counter of passengers on the bus.
     *
     *    @return Value of passengers on the bus.
     */

    public int getNPassOnTheBusValue(){
        synchronized (lockNPassOnTheBusCounter) {
            return nPassOnTheBus;
        }
    }

    /* ************************************************* Setters ******************************************************/

    /**
     *   Sets the signal of the end of the day and wakes up the bus driver.
     */

    public synchronized void setEndDay(){
        this.endDay = true;
        notifyAll();
    }
}
