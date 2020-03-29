package entities;
import sharedRegions.ArrivalLounge;
import sharedRegions.TemporaryStorageArea;
import sharedRegions.BaggageColPoint;

/**
 * ...
 *
 * @author Inês Justo
 * @author Miguel Lopes
 */

public class Porter extends Thread {

    /**
     *  State of the porter
     *
     *    @serialField Stat
     */

    private PorterStates Stat;

    /**
     *  Number of pieces of luggage presently on the conveyor belt
     *
     *    @serialField CB
     */

    private int CB;

    /**
     *  Number of pieces of luggage belonging to passengers in transit presently stored at the storeroom
     *
     *    @serialField SR
     */

    private int SR;

    ArrivalLounge arrivalLounge;
    TemporaryStorageArea tempStore;
    BaggageColPoint bColPnt;

    /**
     *  Instantiation of the thread Passenger.
     *
     *    @param Stat state of the porter
     *    @param CB number of pieces of luggage presently on the conveyor belt
     *    @param SR number of pieces of luggage belonging to passengers in transit presently stored at the storeroom
     */

    public Porter(PorterStates Stat, int CB, int SR){
        this.Stat = Stat;
        this.CB = CB;
        this.SR = SR;
    }

    /**
     *  Life cycle of the thread Porter.
     */

    @Override
    public void run(){
        Bag bag;
        boolean planeHoldEmpty;

        while ( arrivalLounge.takeARest() != 'E' ){	// 'E' character return means end of state
            planeHoldEmpty = false;
            while (!planeHoldEmpty) {
                bag = arrivalLounge.tryToCollectABag();
                if (bag == null) {
                    planeHoldEmpty = true;
                    arrivalLounge.noMoreBagsToCollect();
                } else if (bag.getDestStat() == 'T'){    // 'T' means transit, 'F' means final
                    tempStore.carryItToAppropriateStore(bag);
                } else {
                    bColPnt.carryItToAppropriateStore(bag);
                }
            }
            arrivalLounge.noMoreBagsToCollect();
        }
    }

    /* ******************************************** Getters and Setters ***********************************************/

    /*
     *
     */

    public PorterStates getStat() {
        return this.Stat;
    }

    /*
     *
     */

    public void setStat(PorterStates stat) {
        this.Stat = stat;
    }

    /*
     *
     */

    public void setCB(int CB) {
        this.CB = CB;
    }

    /*
     *
     */

    public void setSR(int SR) {
        this.SR = SR;
    }
}
