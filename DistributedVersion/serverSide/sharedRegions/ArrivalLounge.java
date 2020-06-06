package serverSide.sharedRegions;

import clientSide.SimulPar;
import comInf.Bag;

import java.util.HashMap;
import java.util.Map;

/**
 *   Arrival Lounge.
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class ArrivalLounge {

    /**
     *   General repository of information.
     */

    private GenReposInfo repos;

    /**
     *   Baggage Collection Point.
     */

    private BaggageColPoint bagColPoint;

    /**
     *   Arrival Terminal Transfer Quay.
     */

    private ArrivalTermTransfQuay arrQuay;

    /**
     *   Stack of bags on the plane's hold.
     */

    private MemStack<Bag> pHoldBagStack;

    /*
     *   Counter of passengers that already arrived.
     */

    private int nPassAtArrivL;

    /*
     *   Signals the end of the day.
     */

    private boolean endDay;

    /*
     *   Number of the current flight.
     */

    private int currentFlight;

    /**
     *   Signals the plane's hold with no bags.
     */

    private boolean pHEmpty;

    /**
     *   Signals the porter is in takeARest.
     */

    private volatile boolean porterSleep;

    /**
     *   Departure Terminal Entrance.
     */

    private DepartureTerminalEntrance depTerm;

    /**
     *   Object used for synchronization.
     */

    private static final Object lockNnPassAtArrivLCounter = new Object();

    /**
     *   Instantiation of the Arrival Lounge.
     *
     *     @param repos general repository of information.
     *     @param bagColPoint baggage collection point.
     *     @param destStat destination state of the bags.
     *     @param nBagsPHold number of bags per passenger and flight.
     */

    public ArrivalLounge(/*GenReposInfo repos, BaggageColPoint bagColPoint, ArrivalTermTransfQuay arrQuay,
                         Bag.DestStat[][] destStat, int[][] nBagsPHold*/)
            throws MemException {

        this.repos = repos;

        this.currentFlight = 0;
        this.repos.updateFlightNumber(this.currentFlight);
        this.arrQuay = arrQuay;

        Map<Integer, MemFIFO<Bag>> treadmill = new HashMap<>();
        Map<Integer, Integer> nBagsPerPass = new HashMap<>();
        int nTotalBags = 0;
        for(int nPass = 0; nPass < SimulPar.N_PASS_PER_FLIGHT; nPass++){
            this.repos.passengerExit(nPass);
            nTotalBags += nBagsPHold[nPass][this.currentFlight];
            nBagsPerPass.put(nPass, nBagsPHold[nPass][this.currentFlight]);
        }
        this.repos.initializeCargoHold(nTotalBags);
        this.pHoldBagStack = new MemStack<> (new Bag [nTotalBags]);     // stack instantiation
        for(int nPass = 0; nPass < SimulPar.N_PASS_PER_FLIGHT; nPass++){
            for(int bag = 0; bag < nBagsPHold[nPass][this.currentFlight]; bag++){
                this.pHoldBagStack.write(new Bag(destStat[nPass][this.currentFlight], nPass));
            }
            MemFIFO<Bag> bagPassFIFO =  new MemFIFO<>(new Bag [nBagsPerPass.get(nPass)]);        // FIFO instantiation
            treadmill.put(nPass, bagPassFIFO);
        }

        this.bagColPoint = bagColPoint;
        this.bagColPoint.setPHoldEmpty(false);
        this.bagColPoint.setTreadmill(treadmill);

        resetNPassAtArrivL();

        this.pHEmpty = false;
        endDay = false;
        this.repos.printLog();
        porterSleep = false;
    }

    /* **************************************************Passenger*************************************************** */

    /**
     *   Operation of deciding what to do next (raised by the Passenger).
     *   <p> Head start delay, that represents the time before the passenger chooses between what to do when arriving to
     *   the airport.
     *
     *     @return <li> true, if final destination
     *             <li> false, otherwise
     */

    public synchronized boolean whatShouldIDo(){

        Passenger currentPassenger = (Passenger) Thread.currentThread();
        assert(currentPassenger.getSt() == PassengerStates.AT_THE_DISEMBARKING_ZONE);

        // update logger
        this.repos.updatesPassNR(currentPassenger.getPassengerID(), currentPassenger.getNR());
        this.repos.numberNRTotal(currentPassenger.getNR());
        this.repos.newPass(currentPassenger.getSi());
        this.repos.updatePassSt(currentPassenger.getPassengerID(), PassengerStates.AT_THE_DISEMBARKING_ZONE);
        this.repos.printLog();

        // increment passengers that arrive so the porter knows when to wake up in takeARest()
        boolean last = this.incDecNPassAtArrivLCounter(true);


        if(this.getNPassAtArrivLValue() == 1) {
            this.depTerm.resetDepartureTerminalExit();
        }

        if(last) {
            // wake up Porter in takeARest()
            notifyAll();
        }
        return currentPassenger.getSi() == Passenger.SiPass.FDT;
    }

    /* **************************************************Porter****************************************************** */

    /**
     *   Operation of taking a rest (raised by the Porter).
     *   The porter is waken up by the operation whatShouldIDo of the last of the passengers to reach the arrival
     *   lounge or when the day is over.
     *
     *     @return <li> 'E', if end of state </li>
     *             <li> 'R', otherwise </li>
     */

    public synchronized char takeARest(){

        porterSleep = true;
        notifyAll(); // notify Reset (avoid deadlock of passengers entering and notifying before the porter sleeps)

        Porter porter = (Porter) Thread.currentThread();
        assert(porter.getStat() == PorterStates.WAITING_FOR_A_PLANE_TO_LAND);
        repos.printLog();

        if(this.endDay){
            porterSleep = false;
            return 'E';
        } else {
            while (this.getNPassAtArrivLValue() < SimulPar.N_PASS_PER_FLIGHT || this.pHEmpty) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (this.endDay) {
                    porterSleep = false;
                    return 'E';
                }
            }
        }

        porterSleep = false;
        return 'R';
    }

    /**
     *   Operation of trying to collect a bag from the Plane's hold (raised by the Porter).
     *
     *     @return <li> a bag, if there is still bags on the Plane's hold.</li>
     *             <li> null, otherwise. </li>
     */

    public synchronized Bag tryToCollectABag(){

        Porter porter = (Porter) Thread.currentThread();
        assert(porter.getStat() == PorterStates.WAITING_FOR_A_PLANE_TO_LAND);
        porter.setStat(PorterStates.AT_THE_PLANES_HOLD);

        repos.updatePorterStat(PorterStates.AT_THE_PLANES_HOLD);

        try {
            Bag tmpBag = pHoldBagStack.read();
            repos.removeBagFromCargoHold();
            repos.printLog();
            return tmpBag;
        } catch (MemException e) {
            repos.printLog();
            return null;
        }
    }

    /**
     *   Operation of waking up the passengers that are waiting for their bag at the baggage collection point or the
     *   passengers that want to prepare the next leg and are waiting for all the bags to be collected (raised by the
     *   Porter).
     */

    public synchronized void noMoreBagsToCollect(){

        Porter porter = (Porter) Thread.currentThread();
        assert(porter.getStat() == PorterStates.AT_THE_PLANES_HOLD);
        porter.setStat(PorterStates.WAITING_FOR_A_PLANE_TO_LAND);
        repos.updatePorterStat(PorterStates.WAITING_FOR_A_PLANE_TO_LAND);

        this.pHEmpty = true;
        // notify passenger in prepareNextLeg()
        depTerm.noMoreBags();
        bagColPoint.setPHoldEmpty(true);
        // notify passenger in goCollectABag()
        bagColPoint.noMoreBags();
        repos.printLog();
    }

    /**
     *   Resets that need to be done after the passengers leave the airport.
     *   Notifies the porter and bus driver of the end of the day.
     *    @param bagAndPassDest Destination states of the bags of the passengers.
     *    @param nBagsNA Numbers of bags per passenger that arrive the plane's hold.
     *    @throws MemException Exception.
     */

    public synchronized void resetArrivalLounge(Bag.DestStat[][] bagAndPassDest, int[][] nBagsNA)
            throws MemException {

        while(!porterSleep){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.currentFlight += 1;

        if(this.currentFlight == SimulPar.N_FLIGHTS){
            this.setEndDay();
            this.arrQuay.setEndDay();
        } else {
            repos.updateFlightNumber(this.currentFlight);

            Map<Integer, MemFIFO<Bag>> treadmill = new HashMap<>();
            Map<Integer, Integer> nBagsPerPass = new HashMap<>();

            // int nSRprev = this.pHoldBagStack.getPointer();
            int nTotalBags = 0; //nSRprev;
            for (int nPass = 0; nPass < SimulPar.N_PASS_PER_FLIGHT; nPass++) {
                nTotalBags += nBagsNA[nPass][this.currentFlight];
                nBagsPerPass.put(nPass, nBagsNA[nPass][this.currentFlight]);
            }

            repos.initializeCargoHold(nTotalBags);
            //MemStack<Bag> tempStack = null;
            //if (nSRprev != 0) {
            //    tempStack = new MemStack<>(new Bag[nSRprev]);
            //    for (int nSRbag = 0; nSRbag < nSRprev; nSRbag++) {
            //        tempStack.write(this.pHoldBagStack.read());
            //    }
            //}

            // plane's hold baggage stack instantiation
            this.pHoldBagStack = new MemStack<>(new Bag[nTotalBags]);
            //if (nSRprev != 0) {
            //    for (int nSRbag = 0; nSRbag < nSRprev; nSRbag++) {
            //        this.pHoldBagStack.write(tempStack.read());
            //    }
            //}

            for (int nPass = 0; nPass < SimulPar.N_PASS_PER_FLIGHT; nPass++) {
                for (int bag = 0; bag < nBagsNA[nPass][this.currentFlight]; bag++) {
                    this.pHoldBagStack.write(new Bag(bagAndPassDest[nPass][this.currentFlight], nPass));
                }

                // instantiation of the passenger's bag FIFO for the treadmill in the baggage collection point
                MemFIFO<Bag> bagPassFIFO = new MemFIFO<>(new Bag[nBagsPerPass.get(nPass)]);
                treadmill.put(nPass, bagPassFIFO);
            }

            this.bagColPoint.setPHoldEmpty(false);
            this.bagColPoint.setTreadmill(treadmill);

            // reset the number of passengers that arrived the airport
            this.resetNPassAtArrivL();

            this.pHEmpty = false;
        }
        repos.printLog();
    }

    /**
     *   Operation of incrementing/decrementing the counter.
     *
     *    @return <li>true, if the value of the counter after the operation is the limit.</li>
     *            <li>false, otherwise.</li>
     */

    public boolean incDecNPassAtArrivLCounter(boolean inc) {
        synchronized (lockNnPassAtArrivLCounter) {
            if(inc) {
                nPassAtArrivL++;
            } else {
                nPassAtArrivL--;
            }
            return nPassAtArrivL == SimulPar.N_PASS_PER_FLIGHT;
        }
    }

    /**
     *   Sets the value of the counter to zero.
     */

    public void resetNPassAtArrivL(){
        synchronized (lockNnPassAtArrivLCounter) { // Locks on the private Object
            nPassAtArrivL = 0;
        }
    }

    /* ************************************************* Getters ******************************************************/

    /**
     *   Getter for the value of the of passengers that are currently ate the Arrival Lounge.
     *
     *    @return the value of the counter.
     */

    public int getNPassAtArrivLValue(){
        synchronized (lockNnPassAtArrivLCounter) {
            return nPassAtArrivL;
        }
    }

    /* ************************************************* Setters ******************************************************/

    /**
     *   Sets the Departure Terminal Entrance Reference.
     *
     *    @param departureTerm Departure Terminal Entrance.
     */

    public synchronized void setDepartureTerminalRef(DepartureTerminalEntrance departureTerm){
        this.depTerm = departureTerm;
    }

    /**
     *   Signals the end of the day and notifies the Porter if he is waiting in takeARest().
     */

    public synchronized void setEndDay(){
        this.endDay = true;
        notifyAll();
    }
}
