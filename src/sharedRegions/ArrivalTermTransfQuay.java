package sharedRegions;

import entities.BusDriverStates;
import entities.PassengerStates;
import entities.BusDriver;
import entities.Passenger;

/**
 * ...
 *
 * @author Inês Justo
 * @author Miguel Lopes
 */

public class ArrivalTermTransfQuay {

    /*
    * TODO:
        add FIFO (cais de chegadas precisa de guardar a fila de passageiros que querem entrar no autocarro)
    * */

    /*
     *
     */
    private GenReposInfo repos;

    /*
     *
     */

    public ArrivalTermTransfQuay(GenReposInfo repos){
        this.repos = repos;
    }

    /**
     *  ... (raised by the Passenger).
     *
     */

    public void enterTheBus(){

        Passenger passenger = (Passenger) Thread.currentThread();
        passenger.setSt(PassengerStates.TERMINAL_TRANSFER);

    }

    /**
     *  ... (raised by the BusDriver).
     *
     */

    public char hasDaysWorkEnded(){

        BusDriver busDriver = (BusDriver) Thread.currentThread();
        busDriver.setStat(BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL);

        return 'F';
    }

    /**
     *  ... (raised by the BusDriver).
     *
     */

    public void announcingBusBoarding(){
        /*
          Blocked Entity: Driver
          Freeing Entity: Passenger
          Freeing Method: enterTheBus()
          Freeing Condition: Last passenger in queue
          Blocked Entity Reaction: goToDepartureTerminal()
        */

        BusDriver busDriver = (BusDriver) Thread.currentThread();
        busDriver.setStat(BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL);

    }

    /**
     *  ... (raised by the BusDriver).
     *
     */

    public void parkTheBus(){
        /*
          Blocked Entity: Driver
          Freeing Entity: Passenger
          Freeing Method: takeABus()
          Freeing Condition: place in waiting queue = bus capacity
          Blocked Entity Reactions: announcingBusBoarding()

          Freeing Entity: Driver
          Freeing Method: time
          Freeing Condition: at least 1 passenger in queue
          Blocked Entity Reaction: announcingBusBoarding()
        */

        BusDriver busDriver = (BusDriver) Thread.currentThread();
        busDriver.setStat(BusDriverStates.PARKING_AT_THE_ARRIVAL_TERMINAL);

    }
}
