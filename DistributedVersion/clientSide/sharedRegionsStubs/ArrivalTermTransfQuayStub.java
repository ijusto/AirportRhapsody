package clientSide.sharedRegionsStubs;

import clientSide.ClientCom;
import clientSide.entities.BusDriver;
import clientSide.entities.Porter;
import comInf.Message;
import clientSide.entities.Passenger;

import java.io.Serializable;

/**
 *
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class ArrivalTermTransfQuayStub implements Serializable {

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
     *  Arrival Terminal Transfer Quay Stub Instantiation.
     *
     *    @param hostName name of the computational system where the server is located
     *    @param port server listening port size
     */

    public ArrivalTermTransfQuayStub (String hostName, int port) {
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
//        outMessage = new Message(Message.PARAMSATTQUAY, reposStub);
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

    /* ************************************************Passenger***************************************************** */

    /**
     *   Operation of taking a Bus (raised by the Passenger).
     *   Before blocking, the passenger wakes up the bus driver, if the passenger's place in the waiting line equals
     *   the bus capacity, and is waken up by the operation announcingBusBoarding of the driver to mimic her entry
     *   in the bus.
     */

    public void takeABus(int id) {

        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;
        Passenger p = (Passenger) Thread.currentThread();
        while(!con.open()){  // waiting for the connection to be established
            try {
                p.sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.TAKEABUS, id, p.getSt().ordinal());
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        //System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.TAKEABUSDONE){
            System.out.println("Tipo inválido: " + inMessage.getType() + "na função takeABus");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();

    }

    /**
     *   Operation of entering the bus (raised by the Passenger).
     *   If the passenger that raises this operation is the last passenger to enter the bus, he notifies the bus driver
     *   in announcingBusBoarding, who is waiting for all the passenger to enter.
     */

    public void enterTheBus(int id){

        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;
        Passenger p = (Passenger) Thread.currentThread();
        while(!con.open()){  // waiting for the connection to be established
            try {
                p.sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.ENTERBUS, id, p.getSt().ordinal());
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        //System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.ACK) {
            System.out.println("Tipo inválido: " + inMessage.getType() + "na função enterTheBus");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /* ************************************************Bus Driver**************************************************** */

    /**
     *   Operation of checking if all the flights ended (raised by the BusDriver).
     *
     *     @return <li> 'F', if end of day </li>
     *             <li> 'R', otherwise </li>
     */

    public char hasDaysWorkEnded(){

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;
        BusDriver b = (BusDriver) Thread.currentThread();
        while(!con.open()){  // waiting for the connection to be established
            try {
                b.sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.WORKENDED, b.getStat().ordinal());
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        //System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if ((inMessage.getType() != Message.CONTDAYS) && (inMessage.getType() != Message.ENDBUSDRIVER)) {
            System.out.println("Tipo inválido: " + inMessage.getType() + "na função hasDaysWorkEnded");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();

        return (inMessage.getType() == Message.ENDBUSDRIVER) ? 'F' : 'R';
    }

    /**
     *   Operation of parking the bus (raised by the BusDriver).
     *   The Bus Driver is waits for a notification of the third (bus capacity) passenger to join the waiting line
     *   or for the parking time to come to an end. If the time has come to start driving and there is a passenger in
     *   the line or he was notified by the third passenger in the line, he wakes up. Otherwise, he keeps waiting.
     */

    public void parkTheBus(){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;
        BusDriver b = (BusDriver) Thread.currentThread();
        while(!con.open()){  // waiting for the connection to be established
            try {
                b.sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.PARKBUS, b.getStat().ordinal());
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        //System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.PBDONE){
            System.out.println("Tipo inválido: " + inMessage.getType() + "na função parkTheBus");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /**
     *   Operation of announcing the bus boarding to the passengers in the waiting line (raised by the BusDriver).
     *   The bus driver signals the bus driver's will to let the passengers enter the bus and waits for the last
     *   passenger to notify him after he enters.
     */

    public void announcingBusBoarding(){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;
        BusDriver b = (BusDriver) Thread.currentThread();
        while(!con.open()){  // waiting for the connection to be established
            try {
                b.sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.ANNOUCEBUSBORADING, b.getStat().ordinal());
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        //System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.ABBDONE){
            System.out.println("Tipo inválido: " + inMessage.getType() + "na função announcingBusBoarding");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        b.setNPassOnTheBus(inMessage.getNPassOnTheBus());
        con.close();
    }

    /**
     *   Resets that need to be done after the passengers leave the airport.
     */

    public void resetArrivalTermTransfQuay() {
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.RESETATQ);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        //System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.ACK){
            System.out.println("Tipo inválido: " + inMessage.getType() + "na função resetArrivalTermTransfQuay");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }


    /* ************************************************* Setters ******************************************************/

    /**
     *   Sets the signal of the end of the day and wakes up the bus driver.
     */

    public void setEndDay(){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.SETENDDAY);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        //System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.ACK){
            System.out.println("Tipo inválido: " + inMessage.getType() + "na função setEndDay");
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
        //System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.ACK){
            System.out.println("Tipo inválido: " + inMessage.getType() + "na função shutdown");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
}
