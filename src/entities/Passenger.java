package entities;
import sharedRegions.ArrivalLounge;
import sharedRegions.ArrivalTermTransfQuay;
import sharedRegions.DepartureTermTransfQuay;
import sharedRegions.DepartureTerminalEntrance;

/**
 * ...
 *
 * @author Inês Justo
 * @author Miguel Lopes
 */

public class Passenger extends Thread {

    /*
    private static final int TRT = 0;  // in transit
    private static final int FDT = 1;  // has this airport as her final destination

    private static final int AT_THE_DISEMBARKING_ZONE = 0;
    private static final int AT_THE_LUGGAGE_COLLECTION_POINT = 1;
    private static final int EXITING_THE_ARRIVAL_TERMINAL = 2;
    private static final int AT_THE_BAGGAGE_RECLAIM_OFFICE = 3;

    private static final int AT_THE_ARRIVAL_TRANSFER_TERMINAL = 4;
    private static final int TERMINAL_TRANSFER = 5;
    private static final int AT_THE_DEPARTURE_TRANSFER_TERMINAL = 6;
    private static final int ENTERING_THE_DEPARTURE_TERMINAL = 7;

    private enum State {
                         AT_THE_DISEMBARKING_ZONE,
                         AT_THE_LUGGAGE_COLLECTION_POINT,
                         EXITING_THE_ARRIVAL_TERMINAL,
                         AT_THE_BAGGAGE_RECLAIM_OFFICE,
                         AT_THE_ARRIVAL_TRANSFER_TERMINAL,
                         TERMINAL_TRANSFER,
                         AT_THE_DEPARTURE_TRANSFER_TERMINAL,
                         ENTERING_THE_DEPARTURE_TERMINAL
                        };
    */

    public enum SituationPassenger {TRT, FDT};

    /**
     *  State of the passenger
     *
     *    @serialField St
     */

    private PassengerStates St;

    /**
     *  Situation of passenger
     *
     *    @serialField Si
     */

    private SituationPassenger Si;

    /**
     *  Number of pieces of luggage the passenger carried at the start of her journey
     *
     *    @serialField NR
     */

    private int NR;

    /**
     *  Number of pieces of luggage the passenger she has presently collected
     *
     *    @serialField NA
     */

    private int NA;

    /**
     *  Passenger ID
     *
     *    @serialField id
     */

    private int id;

    /**
     *  Instantiation of the thread Passenger.
     *
     *    @param St state of passenger
     *    @param Si situation of passenger
     *    @param NR number of pieces of luggage the passenger carried at the start of her journey
     *    @param NA number of pieces of luggage the passenger she has presently collected
     */

    public Passenger(PassengerStates St, SituationPassenger Si, int NR, int NA, int id){
        this.St = St;
        this.Si = Si;
        this.NR = NR;
        this.NA = NA;
        this.id = id;

    }

    /*
     *  functionality: sets up the max number of bags of the passenger
     *    @param   maxBags4Passenger  max number of bags of the passenger
     */

    // Passageiro tem que dizer quantas malas vai dar ao entities.Porter
    public void setUpPassenger(int maxBags4Passenger){
        //originar ou nao a perda de malas

        /* nr is passed in arrival lounge, delete next instantiation */
        this.NR = (Math.random()*maxBags4Passenger < 0.5) ? 0 : (Math.random()*maxBags4Passenger < 0.5) ? 1 : 2;

        if(NR > 0){
            double will_miss = (Math.random()*NR < 0.5) ? 0 : (Math.random()*NR < 0.5) ? 1 : 2;
        }
    }

    public SituationPassenger getSi() {
        return Si;
    }

    public PassengerStates getSt() {
        return St;
    }

    public int getNA() {
        return NA;
    }

    public int getNR() {
        return NR;
    }

    ArrivalLounge arrivalLounge;
    ArrivalTermTransfQuay transferQuay;
    DepartureTermTransfQuay departureTransferQuay;
    DepartureTerminalEntrance departureEntrance;
    int maxBags4Passenger = 2;

    /**
     *  Life cycle of the thread Passenger.
     */

    @Override
    public void run() {

        this.setUpPassenger(maxBags4Passenger);
        boolean isFinal = arrivalLounge.whatShouldIDo();
        boolean success = false;
        if (isFinal) {
            if (this.getNR() == 0) {
                arrivalLounge.goHome();
            } else {
                for (int i = 0; i < this.getNR(); i++) {
                    success = arrivalLounge.goCollectABag(); // porter diz se ja nao existem malas e então seria false
                    if (!success) {
                        break;
                    }
                }
                if (!success) {
                    //reportMissingBags()
                }
                //goHome()
            }
        } else {
            arrivalLounge.takeABus();
            transferQuay.enterTheBus();
            departureTransferQuay.leaveTheBus();
            departureEntrance.prepareNextLeg();
        }
    }

    public void setSt(PassengerStates st) {
        St = st;
    }

    public void setSi(SituationPassenger si) {
        Si = si;
    }

    public void setNR(int NR) {
        this.NR = NR;
    }

    public void setNA(int NA) {
        this.NA = NA;
    }

    public void setID(int id) {
        this.id = id;
    }
}
