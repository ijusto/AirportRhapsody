package informationSharingRegions;
import entities.Bag;

public class ArrivalLounge {

    /*
     * calling entity: entities.Passenger
     * <p>
     * functionality: choose between takeABus or one of these two: goCollectABag or goHome
     * @param   Si  situation of passenger
     * @return      true if final destination
     * @see         boolean
     */
    public boolean whatShouldIDo(int Si){
        return false;
    }

    /*
     * calling entity: entities.Passenger
     * functionality: change state of entities.Passenger to AT_THE_ARRIVAL_TRANSFER_TERMINAL
     * @param
     * @return
     */
    public char takeABus(){
        // 'E' character return means end of state
        return 0;
    }

    /*
     * calling entity: entities.Passenger
     * functionality: change state of entities.Passenger to AT_THE_LUGGAGE_COLLECTION_POINT
     * @param
     */
    public boolean goCollectABag(){
        return false;
    }

    /*
     * calling entity: entities.Passenger
     * functionality: change state of entities.Passenger to EXITING_THE_ARRIVAL_TERMINAL
     * @param
     */
    public void goHome(){

    }

    /*
     * @param
     * calling entity: entities.Porter
     * functionality: change state of entities.Porter to WAITING_FOR_A_PLANE_TO_LAND
     */
    public char takeARest(){
        // bloqueia porter


        return 'E';
        // 'E' character return means end of state
    }

    /*
     * @param
     * calling entity: entities.Porter
     * functionality:
     */
    public Bag tryToCollectABag(){
        return new Bag();
    }

    /*
     * @param
     * calling entity: entities.Porter
     * functionality:
     */
    public void noMoreBagsToCollect(){

    }
}
