package sharedRegions;
import entities.Bag;
import entities.PassengerStates;
import entities.PorterStates;
import entities.Passenger;
import entities.Porter;

/**
 * ...
 *
 * @author Inês Justo
 * @author Miguel Lopes
 */

public class BaggageColPoint {

    /* TODO:
        add CAM (memoria associativa para guardar as malas - tapete rolante
    *
    * */

    /*
     *
     */
    private GenReposInfo repos;

    /*
     *
     */

    public BaggageColPoint(GenReposInfo repos){
        this.repos = repos;
    }

    /**
     *  ... (raised by the Passenger).
     *
     */

    public void goCollectABag(){

        Passenger passenger = (Passenger) Thread.currentThread();
        passenger.setSt(PassengerStates.AT_THE_LUGGAGE_COLLECTION_POINT);

    }

    /**
     *  ... (raised by the Passenger).
     *
     */

    public void reportMissingBags(){

        Passenger passenger = (Passenger) Thread.currentThread();
        passenger.setSt(PassengerStates.AT_THE_BAGGAGE_RECLAIM_OFFICE);

    }

    /**
     *  ... (raised by the Passenger).
     *
     */

    public void goHome(){

        Passenger passenger = (Passenger) Thread.currentThread();
        passenger.setSt(PassengerStates.EXITING_THE_ARRIVAL_TERMINAL);

    }

    /**
     *  ... (raised by the Porter).
     *
     */
    public void carryItToAppropriateStore(Bag bag){

        Porter porter = (Porter) Thread.currentThread();
        porter.setStat(PorterStates.AT_THE_LUGGAGE_BELT_CONVEYOR);

    }
}
