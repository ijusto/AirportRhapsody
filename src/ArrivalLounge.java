public class ArrivalLounge {

    public boolean whatShouldIDo(int Si){
        /*
         * params: Si - situation of passenger
         * calling entity: Passenger
         * functionality: choose between takeABus or one of these two: goCollectABag or goHome
         */
        return false;
    }

    public void takeABus(){
        /*
         * params:
         * calling entity: Passenger
         * functionality: change state of Passenger to AT_THE_ARRIVAL_TRANSFER_TERMINAL
         */
    }

    public void goCollectABag(){
        /*
         * params:
         * calling entity: Passenger
         * functionality: change state of Passenger to AT_THE_LUGGAGE_COLLECTION_POINT
         */
    }

    public void goHome(){
        /*
         * params:
         * calling entity: Passenger
         * functionality: change state of Passenger to EXITING_THE_ARRIVAL_TERMINAL
         */
    }
}
