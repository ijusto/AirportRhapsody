package entities;
import informationSharingRegions.ArrivalLounge;
import informationSharingRegions.TemporaryStorageArea;
import informationSharingRegions.BaggageCollectionPoint;

public class Porter {
    private static final int WAITING_FOR_A_PLANE_TO_LAND = 0;
    private static final int AT_THE_PLANES_HOLD = 1;
    private static final int AT_THE_LUGGAGE_BELT_CONVEYOR = 2;
    private static final int AT_THE_STOREROOM = 3;

    private enum State { WAITING_FOR_A_PLANE_TO_LAND,
                         AT_THE_PLANES_HOLD,
                         AT_THE_LUGGAGE_BELT_CONVEYOR,
                         AT_THE_STOREROOM
                        }

    private State Stat;  //Stat – state of the porter
    private int CB;  // CB - number of pieces of luggage presently on the conveyor belt
    private int SR;  // SR - number of pieces of luggage belonging to passengers in transit presently stored at the storeroom
    ArrivalLounge arrivalLounge;

    TemporaryStorageArea tempStore;

    BaggageCollectionPoint bColPnt;

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
