package clientSide.sharedRegionsStubs;

import clientSide.clients.ClientCom;
import clientSide.entities.Passenger;
import clientSide.entities.Porter;
import comInf.Bag;
import comInf.MemException;
import comInf.MemFIFO;
import comInf.Message;

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

    public void probPar(GenReposInfoStub reposStub) {

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ()){                                                // aguarda ligação
            try {
                Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (Message.PARAMSBAGCOLPNT, reposStub);
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType() != Message.ACK) {
            System.out.println("Arranque da simulação: Tipo inválido!");
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

    public boolean goCollectABag(int passengerId){

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ()) {                                 // aguarda ligação
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException e) {}
        }

        outMessage = new Message (Message.GOCOLLECTBAG, passengerId);        // pede a realização do serviço
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();

        if ((inMessage.getType () != Message.GCBDONE)) {
            System.out.println("Thread " + Thread.currentThread ().getName () + ": Tipo inválido!");
            System.out.println(inMessage.toString ());
            System.exit (1);
        }
        con.close ();

        return inMessage.msgBagCollected();
    }


    /* **************************************************Porter****************************************************** */

    /**
     *  Operation of carrying a bag from the plane's hold to the baggage collection point (raised by the Porter).
     */

    public void carryItToAppropriateStore(Bag bag){
        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ()) {                                 // aguarda ligação
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException e) {}
        }

        outMessage = new Message(Message.CARRYAPPSTORE, bag.getIntDestStat(), bag.getIdOwner());        // pede a realização do serviço
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();

        if ((inMessage.getType () != Message.ACK)) {
            System.out.println("Thread " + Thread.currentThread ().getName () + ": Tipo inválido!");
            System.out.println(inMessage.toString ());
            System.exit (1);
        }
        con.close ();
    }

    /**
     *
     */

    public void resetBaggageColPoint(){
        this.pHoldEmpty = true;
        this.treadmill = null;
    }

    /**
     *   Called by Porter in noMoreBagsToCollect.
     */

    public void noMoreBags() {
        // wake up Passengers in goCollectABag()
        notifyAll();
    }

    /* ************************************************* Getters ******************************************************/


    /**
     *   Signaling the empty state of the plane's hold.
     *
     *    @return
     */

    public boolean pHoldEmpty() {
        return pHoldEmpty;
    }

    /* ************************************************* Setters ******************************************************/

    /**
     *   ...
     *
     *    @param treadmill ...
     */

    public void setTreadmill(Map<Integer, MemFIFO<Bag>> treadmill) {
        this.treadmill = treadmill;
    }

    /**
     *
     */

    public void setPHoldEmpty(boolean pHoldEmpty){
        this.pHoldEmpty = pHoldEmpty;
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

}
