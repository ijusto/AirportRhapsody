package clientSide.sharedRegionsStubs;
import clientSide.ClientCom;
import comInf.Message;

import java.io.Serializable;

/**
 *
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class GenReposInfoStub implements Serializable {

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
     *  General Repository of Information Stub Instantiation.
     *
     *    @param hostName name of the computational system where the server is located
     *    @param port server listening port size
     */

    public GenReposInfoStub(String hostName, int port){
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     *   Logs the alterations.
     */

    public void printLog(){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.PRINTLOG);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        //System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.ACK){
            //System.out.println("Tipo inválido: " + inMessage.getType() + "na função printLog");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /**
     *   Logs the final report of the simulation.
     */

    public boolean finalReport(int type){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.FINALREPORT, type);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        //System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.ACK && inMessage.getType() != Message.SHUT){
            //System.out.println("Tipo inválido: " + inMessage.getType() + "na função finalReport");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();

        return inMessage.getType() == Message.SHUT;
    }

    /* **************************************************Plane******************************************************* */

    /**
     *   Update flight number after the previous flight is finished.
     *
     *   @param flight flight.
     */

    public void updateFlightNumber(int flight){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.UPDATEFN, flight);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        //System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.ACK){
            //System.out.println("Tipo inválido: " + inMessage.getType() + "na função updateFlightNumber");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /**
     *   Initializes the number of pieces of luggage at the plane's hold.
     *
     *    @param bn Number of pieces of luggage presently at the plane's hold.
     */

    public void initializeCargoHold(int bn){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.INITCHOLD, bn);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        //System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.ACK){
            //System.out.println("Tipo inválido: " + inMessage.getType() + "na função initializeCargoHold");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /**
     *   Update baggage stored in the cargo hold when porter retrieves the baggage.
     */

    public void removeBagFromCargoHold(){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.REMBAGCHOLD);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        //System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.ACK){
            //System.out.println("Tipo inválido: " + inMessage.getType() + "na função removeBagFromCargoHold");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /* **************************************************Porter****************************************************** */

    /**
     *   Update baggage stored in the Conveyor Belt when porter adds bags.
     */

    public void incBaggageCB(){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.INCBAGCB);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        //System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.ACK){
            //System.out.println("Tipo inválido: " + inMessage.getType() + "na função incBaggageCB");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /**
     *   Update baggage stored in the Conveyor Belt when passenger gets the baggage.
     */

    public void pGetsABag(){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message (Message.PGETSABAG);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        //System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.ACK){
            //System.out.println("Tipo inválido: " + inMessage.getType() + "na função pGetsABag");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /**
     *   Update baggage stored in the Storage Room when porter puts the baggage
     */

    public void saveBagInSR(){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.SAVEBAGSR);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        //System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.ACK){
            //System.out.println("Tipo inválido: " + inMessage.getType() + "na função saveBagInSR");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /**
     *   Update the Porter state.
     *
     *    @param porterState Porter's state.
     */

    public void updatePorterStat(int porterState){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.UDTEPORTSTAT, porterState);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        //System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.ACK){
            //System.out.println("Tipo inválido: " + inMessage.getType() + "na função updatePorterStat");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /* ************************************************Bus Driver**************************************************** */

    /**
     *   Update the Bus Driver State.
     *
     *   @param busDriverState Bus driver's state.
     */

    public void updateBDriverStat(int busDriverState){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.UDTEBDSTAT, busDriverState);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        //System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.ACK){
            //System.out.println("Tipo inválido: " + inMessage.getType() + "na função updateBDriverStat");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /**
     *   Update the queue of passengers.
     *
     *    @param id Passenger's id.
     */

    public void pJoinWaitingQueue(int id){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.PJOINWQ, id);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        //System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.ACK){
            //System.out.println("Tipo inválido: " + inMessage.getType() + "na função pJoinWaitingQueue");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /**
     *   Update of the occupation state of the bus seat.
     *
     *    @param id Passenger's id.
     */

    public void pLeftWaitingQueue(int id){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.PLEFTWQ, id);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        //System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.ACK){
            //System.out.println("Tipo inválido: " + inMessage.getType() + "na função pLeftWaitingQueue");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /**
     *   Removes the passenger id from the bus seat.
     *
     *    @param id Passenger's id.
     */

    public void freeBusSeat(int id){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.FREEBS, id);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        //System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.ACK) {
            //System.out.println("Tipo inválido: " + inMessage.getType() + "na função freeBusSeat");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /* **************************************************Passenger*************************************************** */

    /**
     *   Increments the number of passengers from a certain passenger situation.
     *
     *    @param passSi Passenger's situation.
     */

    public  void newPass(int passSi){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.NEWPASS, passSi);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        //System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.ACK){
            //System.out.println("Tipo inválido: " + inMessage.getType() + "na função newPass");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /**
     *   Update the Passenger State.
     *
     *    @param id Passenger's id.
     *    @param passengerState Passenger's state.
     */

    public void updatePassSt(int id, int passengerState){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.UDTEPASSSTAT, id, passengerState);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        //System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.ACK){
            //System.out.println("Tipo inválido: " + inMessage.getType() + "na função updatePassSt");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /**
     *   Get the passenger situation (in transit or final).
     *
     *    @param id Passenger's id.
     *    @param si Passenger's situation.
     */

    public void getPassSi(int id, int si){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.GETPASSSI, id, si);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        //System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.ACK){
            //System.out.println("Tipo inválido: " + inMessage.getType() + "na função getPassSi");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /**
     *   Update the passengers luggage at the start of the journey.
     *
     *   @param id Passenger's id.
     *   @param nr Number of pieces of luggage the passenger had at the start of the journey.
     */

    public void updatesPassNR(int id, int nr){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.UDTEPASSNR, id, nr);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        //System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.ACK){
            //System.out.println("Tipo inválido: " + inMessage.getType() + "na função updatesPassNR");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /**
     *   Update the number of luggage a passenger collected.
     *
     *   @param id Passenger's id.
     *   @param na Number of pieces of luggage the passenger has collected.
     */

    public void updatesPassNA(int id, int na){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.UDTEPASSNA, id, na);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        //System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.ACK){
            //System.out.println("Tipo inválido: " + inMessage.getType() + "na função updatesPassNA");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /**
     *   Resets all the info of a passenger with a certain id.
     *
     *    @param id Passenger's id.
     */

    public void passengerExit(int id){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.PASSEXIT, id);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        //System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.ACK){
            //System.out.println("Tipo inválido: " + inMessage.getType() + "na função passengerExit");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /* ***********************************************Final Report*************************************************** */

    /**
     *   Updates the number of reported missing bags.
     */

    public void missingBagReported(){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.MISSBAGREP);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        //System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.ACK){
            //System.out.println("Tipo inválido: " + inMessage.getType() + "na função missingBagReported");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }


    /**
     *   Updates the number of pieces of luggage all passengers combined had at the start of the journey.
     *
     *    @param nr Number of pieces of luggage all passengers combined had at the start of the journey.
     */

    public void numberNRTotal(int nr){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.NUMNRTOTAL, nr);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        //System.out.println("-------- Received Message of type " + Message.getMsgTypeString(inMessage.getType()) + " --------");
        if (inMessage.getType() != Message.ACK){
            //System.out.println("Tipo inválido: " + inMessage.getType() + "na função numberNRTotal");
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
            //System.out.println("Tipo inválido: " + inMessage.getType() + "na função shutdown");
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
}