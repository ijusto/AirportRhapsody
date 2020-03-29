package entities;
import sharedRegions.ArrivalLounge;
import sharedRegions.BaggageColPoint;
import sharedRegions.TemporaryStorageArea;

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
     *  ...
     *
     *    @serialField arrivalLounge
     */

    ArrivalLounge arrivalLounge;

    /**
     *  ...
     *
     *    @serialField tempStore
     */

    TemporaryStorageArea tempStore;

    /**
     *  ...
     *
     *    @serialField bColPnt
     */

    BaggageColPoint bColPnt;

    /**
     *  Instantiation of the thread Porter.
     *
     *    @param Stat state of the porter
     *    @param CB number of pieces of luggage presently on the conveyor belt
     *    @param SR number of pieces of luggage belonging to passengers in transit presently stored at the storeroom
     *    @param arrivalLounge  ...
     *    @param tempStore ...
     *    @param bColPnt ....
     */

    public Porter(PorterStates Stat, ArrivalLounge arrivalLounge, TemporaryStorageArea tempStore,
                  BaggageColPoint bColPnt){
        this.Stat = Stat;
        this.arrivalLounge = arrivalLounge;
        this.tempStore = tempStore;
        this.bColPnt = bColPnt;
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
            // arrivalLounge.noMoreBagsToCollect();
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

}
