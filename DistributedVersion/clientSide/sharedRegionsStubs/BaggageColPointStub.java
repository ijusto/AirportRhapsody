package clientSide.sharedRegionsStubs;

import clientSide.clients.ClientCom;
import clientSide.entities.Passenger;
import clientSide.entities.Porter;
import comInf.Bag;
import comInf.MemException;
import comInf.MemFIFO;
import comInf.Message;
import old.GenReposInfo;

import java.util.Map;

public class BaggageColPointStub {

    /**
     *  Nome do sistema computacional onde está localizado o servidor
     *    @serialField serverHostName
     */

    private String serverHostName = null;

    /**
     *  Número do port de escuta do servidor
     *    @serialField serverPortNumb
     */

    private int serverPortNumb;

    /**
     *  Instanciação do stub à barbearia.
     *
     *    @param hostName nome do sistema computacional onde está localizado o servidor
     *    @param port número do port de escuta do servidor
     */

    public BaggageColPointStub (String hostName, int port) {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     *  Fornecer parâmetros do problema (solicitação do serviço).
     *
     */

    public void probPar (GenReposInfo repos)//(String fName, int nIter)
    {

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ()){                                                // aguarda ligação
            try {
                Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        /*
        outMessage = new Message (Message.SETNFIC, fName, nIter);
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType() != Message.NFICDONE) {
            System.out.println("Arranque da simulação: Tipo inválido!");
            System.out.println(inMessage.toString ());
            System.exit (1);
        }
         */
        con.close ();
    }

    /**
     *  Fazer o shutdown do servidor (solicitação do serviço).
     */

    public void shutdown ()
    {
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ()){                                                // aguarda ligação
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException e) {}
        }
        outMessage = new Message (Message.SHUT);
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK) {
            System.out.println("Thread " + Thread.currentThread ().getName () + ": Tipo inválido!");
            System.out.println(inMessage.toString ());
            System.exit (1);
        }
        con.close ();
    }


    /* ************************************************Passenger***************************************************** */

    /**
     *   Operation of trying to collect a bag (raised by the Passenger).
     *   The passenger is waken up by the operations carryItToAppropriateStore and noMoreBagsToCollect of the porter when
     *   he places on the conveyor belt a bag she owns, the former, or when he signals that there are no more pieces
     *   of luggage in the plane hold, the latter, and makes a transition when either she has in her possession all the
     *   bags she owns, or was signaled that there are no more bags in the plane hold
     *
     */

    public boolean goCollectABag(){

        Passenger passenger = (Passenger) Thread.currentThread();
        assert(passenger.getSt() == PassengerStates.AT_THE_DISEMBARKING_ZONE);
        passenger.setSt(PassengerStates.AT_THE_LUGGAGE_COLLECTION_POINT);

        // update logger
        repos.updatePassSt(passenger.getPassengerID(),PassengerStates.AT_THE_LUGGAGE_COLLECTION_POINT);

        /*
          Blocked Entity: Passenger
          Freeing Entity: Porter

          Freeing Method: carryItToAppropriateStore()
          Freeing Condition: porter bring their bag
          Blocked Entity Reactions: -> if all bags collected: goHome() else goCollectABag()

          Freeing Method: noMoreBagsToCollect()
          Freeing Condition: no more pieces of luggage
          Blocked Entity Reaction: reportMissingBags()
        */
        repos.printLog();

        do {

            if(this.pHoldEmpty() && this.treadmill.get(passenger.getPassengerID()).isEmpty()) {
                return false;
            }

            if(!this.treadmill.get(passenger.getPassengerID()).isEmpty()){
                try {
                    this.treadmill.get(passenger.getPassengerID()).read();
                    passenger.setNA(passenger.getNA() + 1);

                    repos.updatesPassNA(passenger.getPassengerID(), passenger.getNA());
                    repos.pGetsABag();

                    return true;
                } catch (MemException e) {
                    e.printStackTrace();
                }
            } else if(this.pHoldEmpty()){
                return false;
            }

            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } while(true);

    }


    /* **************************************************Porter****************************************************** */

    /**
     *  Operation of carrying a bag from the plane's hold to the baggage collection point (raised by the Porter).
     */

    public void carryItToAppropriateStore(Bag bag){
        Porter porter = (Porter) Thread.currentThread();
        assert(porter.getStat() == PorterStates.AT_THE_PLANES_HOLD);
        assert(this.treadmill.containsKey(bag.getIdOwner()));
        porter.setStat(PorterStates.AT_THE_LUGGAGE_BELT_CONVEYOR);
        repos.updatePorterStat(PorterStates.AT_THE_LUGGAGE_BELT_CONVEYOR);

        try {
            this.treadmill.get(bag.getIdOwner()).write(bag);
            repos.incBaggageCB();
            notifyAll();  // wake up Passengers in goCollectABag()
        } catch (MemException e) {
            e.printStackTrace();
        }

        repos.printLog();
    }

    /**
     *
     */

    public synchronized void resetBaggageColPoint(){
        this.pHoldEmpty = true;
        this.treadmill = null;
    }

    /**
     *   Called by Porter in noMoreBagsToCollect.
     */

    public synchronized void noMoreBags() {
        // wake up Passengers in goCollectABag()
        notifyAll();
    }

    /* ************************************************* Getters ******************************************************/


    /**
     *   Signaling the empty state of the plane's hold.
     *
     *    @return
     */

    public synchronized boolean pHoldEmpty() {
        return pHoldEmpty;
    }

    /* ************************************************* Setters ******************************************************/

    /**
     *   ...
     *
     *    @param treadmill ...
     */

    public synchronized void setTreadmill(Map<Integer, MemFIFO<Bag>> treadmill) {
        this.treadmill = treadmill;
    }

    /**
     *
     */

    public synchronized void setPHoldEmpty(boolean pHoldEmpty){
        this.pHoldEmpty = pHoldEmpty;
    }
}
