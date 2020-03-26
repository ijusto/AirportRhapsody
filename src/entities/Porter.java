package entities;
import commonInfrastructures.Bag;
import sharedRegions.ArrivalLounge;
import sharedRegions.TemporaryStorageArea;
import sharedRegions.BaggageCollectionPoint;

/**
 * ...
 *
 * @author Inês Justo
 * @author Miguel Lopes
 */

public class Porter extends Thread {
    private static final int WAITING_FOR_A_PLANE_TO_LAND = 0;
    private static final int AT_THE_PLANES_HOLD = 1;
    private static final int AT_THE_LUGGAGE_BELT_CONVEYOR = 2;
    private static final int AT_THE_STOREROOM = 3;

    private enum State { WAITING_FOR_A_PLANE_TO_LAND,
                         AT_THE_PLANES_HOLD,
                         AT_THE_LUGGAGE_BELT_CONVEYOR,
                         AT_THE_STOREROOM
                        }

    /**
     *  State of the porter
     *
     *    @serialField Stat
     */

    private State Stat;

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
    BaggageCollectionPoint bColPnt;

    /**
     *  Instantiation of the thread Passenger.
     *
     *    @param Stat state of the porter
     *    @param CB number of pieces of luggage presently on the conveyor belt
     *    @param SR number of pieces of luggage belonging to passengers in transit presently stored at the storeroom
     */

    public Porter(State Stat, int CB, int SR){
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
}
