package sharedRegions;

import commonInfrastructures.MemException;
import commonInfrastructures.MemFIFO;
import commonInfrastructures.MemStack;
import entities.*;
import main.SimulPar;

import java.util.HashMap;
import java.util.Map;

/**
 *   ...
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

    /*
     *   Stack of bags on the plane's hold.
     */

    private MemStack<Bag> pHoldBagStack;

    /*
     *   Number of passengers that already arrived.
     */

    private int nPassAtArrivL;

    /*
     *
     */

    private boolean endDay;

    /*
     *   Number of the current flight.
     */

    private int currentFlight;

    /**
     *
     */
    private boolean pHEmpty;

    /*
     *   Departure Terminal Entrance.
     */

    private DepartureTerminalEntrance depTerm;

    /**
     *
     */

    private static final Object lock = new Object();


    /**
     *   Instantiation of the Arrival Lounge.
     *
     *     @param repos general repository of information.
     *     @param bagColPoint baggage collection point.
     *     @param destStat destination state of the bags.
     *     @param nBagsPHold number of bags per passenger and flight.
     */

    public ArrivalLounge(GenReposInfo repos, BaggageColPoint bagColPoint, Bag.DestStat[][] destStat, int[][] nBagsPHold)
            throws MemException {

        this.repos = repos;

        this.currentFlight = 0;
        repos.updateFlightNumber(this.currentFlight);

        Map<Integer, MemFIFO<Bag>> treadmill = new HashMap<>();
        Map<Integer, Integer> nBagsPerPass = new HashMap<>();
        int nTotalBags = 0;
        for(int nPass = 0; nPass < SimulPar.N_PASS_PER_FLIGHT; nPass++){
            nTotalBags += nBagsPHold[nPass][this.currentFlight];
            nBagsPerPass.put(nPass, nBagsPHold[nPass][this.currentFlight]);
        }
        repos.initializeCargoHold(nTotalBags);
        this.pHoldBagStack = new MemStack<> (new Bag [nTotalBags]);     // stack instantiation
        for(int nPass = 0; nPass < SimulPar.N_PASS_PER_FLIGHT; nPass++){
            for(int bag = 0; bag < nBagsPHold[nPass][this.currentFlight]; bag++){
                this.pHoldBagStack.write(new Bag(destStat[nPass][this.currentFlight], nPass));
            }
            MemFIFO<Bag> bagPassFIFO =  new MemFIFO<>(new Bag [nBagsPerPass.get(nPass)]);        // FIFO instantiation
            treadmill.put(nPass, bagPassFIFO);
        }

        this.bagColPoint = bagColPoint;
        this.bagColPoint.setPHoldNotEmpty();
        this.bagColPoint.setTreadmill(treadmill);

        this.nPassAtArrivL = 0;

        this.pHEmpty = false;
        endDay = false;

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

        // increment passengers that arrive so the porter knows when to wake up in takeARest()
        this.nPassAtArrivL += 1;

        // update logger
        this.repos.updatesPassNR(currentPassenger.getPassengerID(), currentPassenger.getNR());
        this.repos.numberNRTotal(currentPassenger.getNR());
        this.repos.newPass(currentPassenger.getSi());

        if(this.nPassAtArrivL == SimulPar.N_PASS_PER_FLIGHT) {
            // wake up Porter in takeARest()
            notifyAll();
        }
        repos.printLog();
        return currentPassenger.getSi() == Passenger.SiPass.FDT;
    }

    /* **************************************************Porter****************************************************** */

    /**
     *   Operation of taking a rest (raised by the Porter).
     *   The porter is waken up by the operation whatShouldIDo of the last of the passengers to reach the arrival
     *   lounge.
     *
     *     @return <li> 'E', if end of state </li>
     *             <li> 'R', otherwise </li>
     */

    public synchronized char takeARest(){
        /*
         *   Blocked Entity: Porter
         *   Freeing Entity: Passenger
         *   Freeing Method: whatShouldIDo()
         *   Freeing Condition: Last passenger to reach the arrival lounge
         *   Blocked Entity Reaction: tryToCollectABag()
         *
         *   Freeing Condition: No more passengers in the airport
         *   Blocked Entity Reaction: finish the thread
         */

        Porter porter = (Porter) Thread.currentThread();
        assert(porter.getStat() == PorterStates.WAITING_FOR_A_PLANE_TO_LAND);

        if(this.currentFlight == SimulPar.N_FLIGHTS - 1 && this.endDay){
            return 'E';
        } else {
            while (this.nPassAtArrivL < SimulPar.N_PASS_PER_FLIGHT || this.pHEmpty) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (this.currentFlight == SimulPar.N_FLIGHTS - 1 && this.endDay) {
                    repos.printLog();
                    return 'E';
                }
            }
        }
        repos.printLog();
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

        // update logger
        repos.updatePorterStat(PorterStates.AT_THE_PLANES_HOLD);

        try {
            Bag tmpBag = pHoldBagStack.read();

            // update logger
            repos.removeBagFromCargoHold();

            repos.printLog();
            return tmpBag;
        } catch (MemException e) {

            repos.printLog();
            return null;
        }

    }

    /**
     *   ... (raised by the Porter).
     *
     */

    public synchronized void noMoreBagsToCollect(){

        Porter porter = (Porter) Thread.currentThread();
        assert(porter.getStat() == PorterStates.AT_THE_PLANES_HOLD);
        porter.setStat(PorterStates.WAITING_FOR_A_PLANE_TO_LAND);

        // update logger
        repos.updatePorterStat(PorterStates.WAITING_FOR_A_PLANE_TO_LAND);

        this.pHEmpty = true;
        depTerm.noMoreBags();

        // change allBagsCollected so the passengers know there are no more bags arriving the bcColPoint
        bagColPoint.setAllBagsCollected();
        // notify passenger in goCollectABag()
        bagColPoint.noMoreBags();

        repos.printLog();
    }

    /**
     *
     *    @param destStat
     *    @param nBagsPHold
     *    @throws MemException
     */

    public synchronized void resetArrivalLounge(Bag.DestStat[][] destStat, int[][] nBagsPHold)
            throws MemException {

        // update flight number
        this.currentFlight += 1;

        // update logger
        repos.updateFlightNumber(this.currentFlight);

        Map<Integer, MemFIFO<Bag>> treadmill = new HashMap<>();
        Map<Integer, Integer> nBagsPerPass = new HashMap<>();

        int nSRprev = this.pHoldBagStack.getPointer();
        int nTotalBags = nSRprev;
        for(int nPass = 0; nPass < SimulPar.N_PASS_PER_FLIGHT; nPass++){
            nTotalBags += nBagsPHold[nPass][this.currentFlight];
            nBagsPerPass.put(nPass, nBagsPHold[nPass][this.currentFlight]);
        }

        // update logger
        repos.initializeCargoHold(nTotalBags);
        MemStack<Bag> tempStack = null;
        if(nSRprev != 0) {
            tempStack = new MemStack<>(new Bag[nSRprev]);
            for (int nSRbag = 0; nSRbag < nSRprev; nSRbag++) {
                tempStack.write(this.pHoldBagStack.read());
            }
        }

        // plane's hold baggage stack instantiation
        this.pHoldBagStack = new MemStack<> (new Bag [nTotalBags]);
        if(nSRprev != 0) {
            for (int nSRbag = 0; nSRbag < nSRprev; nSRbag++) {
                this.pHoldBagStack.write(tempStack.read());
            }
        }

        for(int nPass = 0; nPass < SimulPar.N_PASS_PER_FLIGHT; nPass++){
            for(int bag = 0; bag < nBagsPHold[nPass][this.currentFlight]; bag++){
                this.pHoldBagStack.write(new Bag(destStat[nPass][this.currentFlight], nPass));
            }

            // instantiation of the passenger's bag FIFO for the treadmill in the baggage collection point
            MemFIFO<Bag> bagPassFIFO =  new MemFIFO<>(new Bag [nBagsPerPass.get(nPass)]);
            treadmill.put(nPass, bagPassFIFO);
        }

        this.bagColPoint.setPHoldNotEmpty();
        this.bagColPoint.setTreadmill(treadmill);

        // reset the number of passengers that arrived the airport
        this.nPassAtArrivL = 0;

        this.pHEmpty = false;

        notifyAllPassExited();


        repos.printLog();
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
     *
     */

    public synchronized void notifyAllPassExited(){ notifyAll(); }

    /**
     *
     */

    public synchronized void setEndDay(){
        notifyAll();
        this.endDay = true;
    }
}
