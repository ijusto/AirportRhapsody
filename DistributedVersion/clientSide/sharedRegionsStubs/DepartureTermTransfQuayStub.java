package clientSide.sharedRegionsStubs;

import clientSide.ClientCom;
import clientSide.entities.*;
import comInf.Message;

/**
 *
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class DepartureTermTransfQuayStub {

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
     *  Departure Terminal Transfer Quay Stub Instantiation.
     *
     *    @param hostName name of the computational system where the server is located
     *    @param port server listening port size
     */

    public DepartureTermTransfQuayStub(String hostName, int port){
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     *  Provide parameters of the problem (service request).
     *
     */

//    public void probPar(GenReposInfoStub reposStub){
//
//        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
//        Message inMessage, outMessage;
//
//        while(!con.open()){  // waiting for the connection to be established
//            try {
//                Thread.currentThread().sleep((long) 10);
//            } catch (InterruptedException ignored){}
//        }
//
//        // asks for the service to be done
//        outMessage = new Message(Message.PARAMSDEPTTQUAY, reposStub);
//        con.writeObject(outMessage);
//
//        inMessage = (Message) con.readObject();
//        if (inMessage.getType() != Message.ACK){
//            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
//            System.out.println(inMessage.toString());
//            System.exit(1);
//        }
//        con.close();
//    }

    public void leaveTheBus(int id){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;
        Passenger p = (Passenger) Thread.currentThread();
        while(!con.open()){  // waiting for the connection to be established
            try {
                p.sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.LEAVEBUS, id);  //pede report missing bags
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.LBDONE){
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /* *************************************************Bus Driver*************************************************** */

    /**
     *   BusDriver informs the passengers they can leave the bus (raised by the BusDriver).
     *   Then he waits for a notification of the last passenger to leave the bus.
     */
    public void parkTheBusAndLetPassOff() {
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;
        BusDriver b = (BusDriver) Thread.currentThread();
        while(!con.open()){  // waiting for the connection to be established
            try {
                b.sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.PBLPO, b.getStat().ordinal());  //pede report missing bags
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.PBLPODONE){
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /**
     *   Resets the counter of passengers on the bus.
     */

    public synchronized void resetDepartureTermTransfQuay(){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;
        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.RESETDTTQ);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.ACK){
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /**
     *  Shut down the server (service request).
     */

    public void shutdown ()
    {
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
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
}
