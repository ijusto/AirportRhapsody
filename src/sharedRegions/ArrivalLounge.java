package sharedRegions;
import commonInfrastructures.Bag;
import entities.*;

import java.util.Queue;

/**
 * ...
 *
 * @author Inês Justo
 * @author Miguel Lopes
 */

public class ArrivalLounge {

    public Queue<Passenger> passengerQueue;

    /**
     *  Operation of deciding what to do next (raised by the Passenger). <p> functionality: choose between takeABus or one of these two: goCollectABag or goHome
     *
     *    @param currentPassenger situation of passenger
     *
     *    @return <li> true, if final destination
     *            <li> false, otherwise
     */

    public synchronized boolean whatShouldIDo(Passenger currentPassenger){
        if(currentPassenger.getSt() == 'F'){
            return true;
        }
        else
            return false;
    }

    /**
     *  Operation of taking a Bus (raised by the Passenger). <p> functionality: change state of entities.Passenger to AT_THE_ARRIVAL_TRANSFER_TERMINAL
     *
     *    @return idk
     */

    public synchronized char takeABus(){

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

    /**
     *  Operation of taking a rest (raised by the Porter). <p> functionality: change state of entities.Porter to WAITING_FOR_A_PLANE_TO_LAND
     *
     *    @return <li> 'E', if end of state
     *            <li> false, otherwise
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
