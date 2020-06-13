package clientSide.sharedRegionsStubs;

import clientSide.ClientCom;
import comInf.Message;
import clientSide.entities.Passenger;

/**
 *
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class ArrivalTerminalExitStub {

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
     *  Arrival Terminal Exit Stub Instantiation.
     *
     *    @param hostName name of the computational system where the server is located
     *    @param port server listening port size
     */

    public ArrivalTerminalExitStub(String hostName, int port){
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     *  Provide parameters of the problem (service request).
     *
     */

//    public void probPar(GenReposInfoStub reposStub, ArrivalLoungeStub arrivLoungeStub,
//                        ArrivalTermTransfQuayStub arrivalQuayStub){
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
//        outMessage = new Message(Message.PARAMSATEXIT, reposStub, arrivLoungeStub, arrivalQuayStub);
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
     *   Operation of the passenger of waiting for the last passenger to arrive the Arrival Terminal Exit or the
     *   Departure Terminal Entrance or, if the last, to notify all the others.
     */

    public void goHome(int id){

        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Passenger p = (Passenger) Thread.currentThread();
                p.sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.GOHOME, id);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.ENDPASSENGER){
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /**
     *   Wakes up the passengers waiting in the Arrival Terminal Entrance.
     */

    public void notifyFromPrepareNextLeg(){

        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }

        // asks for the service to be done
        outMessage = new Message(Message.NOTFNEXTL);
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
     *   Operation of incrementing/decrementing the counter.
     *
     *    @return <li>true, if the value of the counter after the operation is the limit.</li>
     *            <li>false, otherwise.</li>
     */

    public boolean incDecCounter(boolean inc) {

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }

        // asks for the service to be done
        outMessage = new Message(Message.INCDECCOUNTER, inc);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if ((inMessage.getType() != Message.LIMITCOUNTER) && (inMessage.getType() != Message.CONTCOUNTER)){
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();

        return inMessage.getType() == Message.CONTCOUNTER;
    }

    /**
     *   Resets the counter of passengers that are at the exit od the Arrival Terminal or at the entrance of the
     *   Departure Terminal.
     */

    public void resetArrivalTerminalExit(){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.RESETATE);
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

    /* ************************************************* Getters ******************************************************/

    /**
     *   Getter for the value of the of passengers of the current flight that left the airport at the Arrival Terminal
     *   or at the Departure Terminal.
     *
     *    @return the value of the counter.
     */

    public int getDeadPassValue(){
        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.GETDEADPASSVAL);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.DEADPASSVAL){
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();

        return inMessage.getMsgDeadPassVal();
    }

    /* ************************************************* Setters ******************************************************/

    /**
     *   Sets the Departure Terminal Entrance Stub Reference.
     *
     *    @param departureTermStub Departure Terminal Entrance Stub.
     */

    public void setDepartureTerminalRef(DepartureTerminalEntranceStub departureTermStub){

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;
        Passenger p = (Passenger) Thread.currentThread();
        while(!con.open()){  // waiting for the connection to be established
            try {
                p.sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.SETDEPTERNREF, departureTermStub);
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
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
}
