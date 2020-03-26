package sharedRegions;
import commonInfrastructures.Bag;

public class BaggageCollectionPoint {
    public void goCollectABag(){
        /*
         * params:
         * calling entity: entities.Passenger
         * functionality: change state of entities.Passenger to AT_THE_LUGGAGE_COLLECTION_POINT
         */
    }

    public void reportMissingBags(){
        /*
         * params:
         * calling entity: entities.Passenger
         * functionality: change state of entities.Passenger to AT_THE_BAGGAGE_RECLAIM_OFFICE
         */
    }

    public void goHome(){
        /*
         * params:
         * calling entity: entities.Passenger
         * functionality: change state of entities.Passenger to EXITING_THE_ARRIVAL_TERMINAL
         */
    }

    public void carryItToAppropriateStore(Bag bag){}
}
