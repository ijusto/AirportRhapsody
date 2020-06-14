package clientSide.sharedRegionsStubs;

import clientSide.ClientCom;
import clientSide.entities.Passenger;
import clientSide.entities.Porter;
import comInf.Bag;
import comInf.MemFIFO;
import comInf.Message;

import java.io.Serializable;
import java.util.Map;

/**
 *
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class BaggageColPointStub implements Serializable {

    /**
     *  Name of the computer system where the server is located
     *    @serialField serverHostName
     */

    private String serverHostName = null;

    /**
     *  Server listening port number
     *    @serialField serverPortNumb
     */

    private int serverPortNumb;

    /**
     *  Baggage Collection Point Stub Instantiation.
     *
     *    @param hostName name of the computational system where the server is located
     *    @param port server listening port size
     */

    public BaggageColPointStub(String hostName, int port){
        serverHostName = hostName;
        serverPortNumb = port;
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

    public boolean goCollectABag(int id){

        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;
        Passenger p = (Passenger) Thread.currentThread();
        while(!con.open()){  // waiting for the connection to be established
            try {
                p.sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.GOCOLLECTBAG, id, p.getSt().ordinal());
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.GCBDONE){
            System.out.println("Tipo inválido: " + inMessage.getType() + "na função goCollectABag");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();

        return inMessage.msgBagCollected();
    }


    /* **************************************************Porter****************************************************** */

    /**
     *  Operation of carrying a bag from the plane's hold to the baggage collection point (raised by the Porter).
     */

    public void carryItToAppropriateStore(Bag bag){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;
        Porter p = (Porter) Thread.currentThread();
        while(!con.open()){  // waiting for the connection to be established
            try {
               p.sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.CARRYAPPSTORE, bag.getIntDestStat(), bag.getIdOwner(), p.getStatPorter().ordinal());
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.ACK){
            System.out.println("Tipo inválido: " + inMessage.getType() + "na função carryItToAppropriateStore");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /**
     *
     */

    public void resetBaggageColPoint(){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.RESETBCP);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.ACK){
            System.out.println("Tipo inválido: " + inMessage.getType() + "na função resetBaggageColPoint");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /**
     *   Called by Porter in noMoreBagsToCollect.
     */

    public void noMoreBags() {
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.NOMOREBAGS);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.ACK){
            System.out.println("Tipo inválido: " + inMessage.getType() + "na função noMoreBags");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /* ************************************************* Setters ******************************************************/

    /**
     *   ...
     *
     *    @param
     */

    public void setTreadmill(int[] nBagsPerPass) {
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.SETTREADMILL, nBagsPerPass);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.ACK){
            System.out.println("Tipo inválido: " + inMessage.getType() + "na função setTreadmill");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /**
     *
     */

    public void setPHoldEmpty(boolean pHoldEmpty){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.SETPHEMPTY, pHoldEmpty);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.ACK){
            System.out.println("Tipo inválido: " + inMessage.getType() + "na função setPHoldEmpty");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }


    /**
     *  Shut down the server (service request).
     */

    public void shutdown(){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.SHUT);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.ACK){
            System.out.println("Tipo inválido: " + inMessage.getType() + "na função shutdown");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
}
