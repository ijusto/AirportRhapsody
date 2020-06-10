package clientSide.sharedRegionsStubs;

import clientSide.ClientCom;
import comInf.Bag;
import comInf.Message;
import clientSide.entities.Porter;

public class TemporaryStorageAreaStub {

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
     *  Temporary Storage Area Stub Instantiation.
     *
     *    @param hostName name of the computational system where the server is located
     *    @param port server listening port size
     */

    public TemporaryStorageAreaStub(String hostName, int port){
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
//        outMessage = new Message(Message.PARAMSTEMPSTORAREA, reposStub);
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

    /**
     *   Operation of carrying a bag from the plane's hold to the temporary storage area (raised by the Porter).
     */

    public void carryItToAppropriateStore(Bag bag){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Porter p = (Porter) Thread.currentThread();
                p.sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.CARRYTOAPPSTORE_TSA, bag.getIntDestStat(), bag.getIdOwner());
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK){
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /**
     *   Resets the stack of bags when all passengers from a flight leave the airport.
     */

    public void resetTemporaryStorageArea() {
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.RESETTSA);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
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

    public void shutdown(){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch(InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.SHUT);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        if(inMessage.getType() != Message.ACK){
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
}
