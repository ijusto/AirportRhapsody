package entities;
import sharedRegions.*;

/**
 * ...
 *
 * @author Inês Justo
 * @author Miguel Lopes
 */

public class Passenger extends Thread {

    public enum SituationPassenger {TRT,   // in transit
                                    FDT};  // final destination

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
     *  ...
     *
     *    @serialField arrivalLounge
     */

    private ArrivalLounge arrivalLounge;

    /**
     *  ...
     *
     *    @serialField transferQuay
     */

    private ArrivalTermTransfQuay arruvTransferQuay;

    /**
     *  ...
     *
     *    @serialField departureTransferQuay
     */

    private DepartureTermTransfQuay departureTransferQuay;

    /**
     *  ...
     *
     *    @serialField departureEntrance
     */

    private DepartureTerminalEntrance departureEntrance;

    /**
     *  ...
     *
     *    @serialField arrivalTerminalExit
     */

    private ArrivalTerminalExit arrivalTerminalExit;

    /**
     *  ...
     *
     *    @serialField baggageColPoint
     */

    private BaggageColPoint baggageColPoint;

    /**
     *  ...
     *
     *    @serialField baggageReclaimOffice
     */

    private BaggageReclaimOffice baggageReclaimOffice;

    /**
     *  Instantiation of the thread Passenger.
     *
     *    @param St state of passenger
     *    @param Si situation of passenger
     *    @param NR number of pieces of luggage the passenger carried at the start of her journey
     *    @param NA number of pieces of luggage the passenger she has presently collected
     *    @param id ...
     *    @param arrivalLounge ...
     *    @param arrivalTermTransfQuay ...
     *    @param departureTransferQuay ...
     *    @param departureEntrance ...
     *    @param arrivalTerminalExit ...
     *    @param baggageColPoint ...
     *    @param baggageReclaimOffice ...
     */

    public Passenger(PassengerStates St, SituationPassenger Si, int NR, int NA, int id, ArrivalLounge arrivalLounge,
                     ArrivalTermTransfQuay arrivalTermTransfQuay, DepartureTermTransfQuay departureTransferQuay,
                     DepartureTerminalEntrance departureEntrance, ArrivalTerminalExit arrivalTerminalExit,
                     BaggageColPoint baggageColPoint, BaggageReclaimOffice baggageReclaimOffice){
        this.St = St;
        this.Si = Si;
        this.NR = NR;
        this.NA = NA;
        this.id = id;

        this.arrivalLounge = arrivalLounge;
        this.arruvTransferQuay = arrivalTermTransfQuay;
        this.departureTransferQuay = departureTransferQuay;
        this.departureEntrance = departureEntrance;
        this.arrivalTerminalExit = arrivalTerminalExit;
        this.baggageColPoint = baggageColPoint;
        this.baggageReclaimOffice = baggageReclaimOffice;
    }

    /**
     *  Life cycle of the thread Passenger.
     */

    @Override
    public void run() {

        boolean isFinal = arrivalLounge.whatShouldIDo();
        boolean success = false;
        if (isFinal) {
            if (this.getNR() == 0) {
                arrivalTerminalExit.goHome();
            } else {
                for (int i = 0; i < this.getNR(); i++) {
                    success = baggageColPoint.goCollectABag(); // porter diz se ja nao existem malas e então seria false
                    if (!success) {
                        break;
                    }
                }
                if (!success) {
                    baggageReclaimOffice.reportMissingBags();
                }
                //arrivalTerminalExit.goHome()
            }
        } else {
            arruvTransferQuay.takeABus();
            arruvTransferQuay.enterTheBus();
            departureTransferQuay.leaveTheBus();
            departureEntrance.prepareNextLeg();
        }
    }

    /* ******************************************** Getters and Setters ***********************************************/

    /*
     *
     */

    public SituationPassenger getSi() {
        return Si;
    }

    /*
     *
     */

    public PassengerStates getSt() {
        return St;
    }

    /*
     *
     */

    public int getNA() {
        return NA;
    }

    /*
     *
     */

    public int getNR() {
        return NR;
    }

    /*
     *
     */

    public int getID() { return id;}

    /*
     *
     */

    public void setSt(PassengerStates st) {
        St = st;
    }

    /*
     *
     */

    public void setSi(SituationPassenger si) {
        Si = si;
    }

    /*
     *
     */

    public void setNR(int NR) {
        this.NR = NR;
    }

    /*
     *
     */

    public void setNA(int NA) {
        this.NA = NA;
    }

    /*
     *
     */

    public void setID(int id) {
        this.id = id;
    }
}
