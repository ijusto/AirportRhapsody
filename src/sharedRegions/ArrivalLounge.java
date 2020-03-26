package sharedRegions;
import commonInfrastructures.Bag;
import entities.*;

import java.util.Queue;

public class ArrivalLounge {

    public Queue<Passenger> passengerQueue;

    /*
     * calling entity: entities.Passenger
     * <p>
     * functionality: choose between takeABus or one of these two: goCollectABag or goHome
     * @param   Si  situation of passenger
     * @return      true if final destination
     * @see         boolean
     */
    public synchronized boolean whatShouldIDo(Passenger currentPassenger){
        if(currentPassenger.getSt() == 'F'){
            return true;
        }
        else
            return false;
    }

    /*
     * calling entity: entities.Passenger
     * functionality: change state of entities.Passenger to AT_THE_ARRIVAL_TRANSFER_TERMINAL
     * @param
     * @return
     */
    public synchronized char takeABus(){
        // 'E' character return means end of state
        return 0;
    }

    /*
     * calling entity: entities.Passenger
     * functionality: change state of entities.Passenger to AT_THE_LUGGAGE_COLLECTION_POINT
     * @param
     */
    public synchronized boolean goCollectABag(){
        return false;
    }

    /*
     * calling entity: entities.Passenger
     * functionality: change state of entities.Passenger to EXITING_THE_ARRIVAL_TERMINAL
     * @param
     */
    public synchronized void goHome(){

    }

    /*
     * @param
     * calling entity: entities.Porter
     * functionality: change state of entities.Porter to WAITING_FOR_A_PLANE_TO_LAND
     */
    public synchronized char takeARest(){
        // bloqueia porter


        return 'E';
        // 'E' character return means end of state
    }

    /*
     * @param
     * calling entity: entities.Porter
     * functionality:
     */
    public synchronized Bag tryToCollectABag(){
        return new Bag();
    }

    /*
     * @param
     * calling entity: entities.Porter
     * functionality:
     */
    public synchronized void noMoreBagsToCollect(){

    }
}
