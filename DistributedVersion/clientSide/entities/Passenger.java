package clientSide.entities;

import clientSide.PassengerStates;
import clientSide.sharedRegionsStubs.*;

/**
 *   Passenger thread.
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class Passenger extends Thread {

    /**
     *   Enum containing all the situations a passenger can be in.
     */

    public enum SiPass {TRT,   // in transit
                        FDT}   // final destination

    /**
     *   State of the passenger.
     *
     *    @serialField St
     */

    private PassengerStates St;

    /**
     *   Situation of passenger.
     *
     *    @serialField Si
     */

    private SiPass Si;

    /**
     *   Number of pieces of luggage the passenger carried at the start of her journey.
     *
     *    @serialField NR
     */

    private int NR;

    /**
     *   Number of pieces of luggage the passenger she has presently collected.
     *
     *    @serialField NA
     */

    private int NA;

    /**
     *   Passenger ID.
     *
     *    @serialField id
     */

    private int id;

    /**
     *   Arrival Lounge Stub.
     *
     *    @serialField arrivalLoungeStub
     */

    private ArrivalLoungeStub arrivalLoungeStub;

    /**
     *   Arrival Terminal Transfer Quay Stub.
     *
     *    @serialField transferQuayStub
     */

    private ArrivalTermTransfQuayStub arrivTransferQuayStub;

    /**
     *  ...
     *
     *    @serialField departureTransferQuayStub
     */

    private DepartureTermTransfQuayStub departureTransferQuayStub;

    /**
     *  ...
     *
     *    @serialField departureEntranceStub
     */

    private DepartureTerminalEntranceStub departureEntranceStub;

    /**
     *  ...
     *
     *    @serialField arrivalTerminalExitStub
     */

    private ArrivalTerminalExitStub arrivalTerminalExitStub;

    /**
     *  ...
     *
     *    @serialField baggageColPointStub
     */

    private BaggageColPointStub baggageColPointStub;

    /**
     *  ...
     *
     *    @serialField baggageReclaimOfficeStub
     */

    private BaggageReclaimOfficeStub baggageReclaimOfficeStub;

    /**
     *  Instantiation of the thread Passenger.
     *
     *    @param St State of passenger.
     *    @param Si Situation of passenger.
     *    @param NR Number of pieces of luggage the passenger carried at the start of her journey.
     *    @param NA Number of pieces of luggage the passenger she has presently collected.
     *    @param id passenger's id.
     *    @param arrivalLoungeStub Arrival Lounge Stub.
     *    @param arrivalTermTransfQuayStub Arrival Terminal Transfer Quay Stub.
     *    @param departureTransferQuayStub Departure Transfer Quay Stub.
     *    @param departureEntranceStub Departure Entrance Stub.
     *    @param arrivalTerminalExitStub Arrival Terminal Exit Stub.
     *    @param baggageColPointStub Baggage Collection Point Stub.
     *    @param baggageReclaimOfficeStub Baggage Reclaim Office Stub.
     */

    public Passenger(PassengerStates St, SiPass Si, int NR, int NA, int id, ArrivalLoungeStub arrivalLoungeStub,
                     ArrivalTermTransfQuayStub arrivalTermTransfQuayStub, DepartureTermTransfQuayStub departureTransferQuayStub,
                     DepartureTerminalEntranceStub departureEntranceStub, ArrivalTerminalExitStub arrivalTerminalExitStub,
                     BaggageColPointStub baggageColPointStub, BaggageReclaimOfficeStub baggageReclaimOfficeStub){
        this.St = St;
        this.Si = Si;
        this.NR = NR;
        this.NA = NA;
        this.id = id;

        this.arrivalLoungeStub = arrivalLoungeStub;
        this.arrivTransferQuayStub = arrivalTermTransfQuayStub;
        this.departureTransferQuayStub = departureTransferQuayStub;
        this.departureEntranceStub = departureEntranceStub;
        this.arrivalTerminalExitStub = arrivalTerminalExitStub;
        this.baggageColPointStub = baggageColPointStub;
        this.baggageReclaimOfficeStub = baggageReclaimOfficeStub;
    }

    /**
     *   Life cycle of the thread Passenger.
     */

    @Override
    public void run() {

        boolean isFinal = arrivalLoungeStub.whatShouldIDo(id);
        boolean success = false;
        if (isFinal) {
            if (this.getNR() != 0) {
                for (int i = 0; i < this.getNR(); i++) {
                    success = baggageColPointStub.goCollectABag(id);
                    if (!success) {
                        break;
                    }
                }
                if (!success) {
                    baggageReclaimOfficeStub.reportMissingBags(id);
                }
            }
            arrivalTerminalExitStub.goHome(id);
        } else {
            arrivTransferQuayStub.takeABus(id);
            arrivTransferQuayStub.enterTheBus(id);
            departureTransferQuayStub.leaveTheBus(id);
            departureEntranceStub.prepareNextLeg(id);
        }
    }

    /* ******************************************** Getters and Setters ***********************************************/

    /**
     * Get Passenger Situation
     */

    public SiPass getSi() {
        return Si;
    }

    /**
     * Get Passenger State
     */

    public PassengerStates getSt() {
        return St;
    }

    /**
     * Get Passenger number of pieces of luggage he has presently collected
     */

    public int getNA() {
        return NA;
    }

    /**
     * Get Passenger number of pieces of luggage he carried at the start of the journey
     */

    public int getNR() {
        return NR;
    }

    /**
     * Get Passenger ID
     */

    public int getPassengerID() { return id;}

    /**
     * Set Passenger State
     */

    public void setSt(PassengerStates st) {
        St = st;
    }

    /**
     * Set Passenger number of pieces of luggage he has presently collected
     */

    public void setNA(int NA) {
        this.NA = NA;
    }

}
