package clientSide.sharedRegionsStubs;

import clientSide.ClientCom;
import comInf.SimulPar;
import comInf.Bag;
import comInf.Message;
import clientSide.entities.*;

/**
 *
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class ArrivalLoungeStub {

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
     *  Arrival Lounge Stub Instantiation.
     *
     *    @param hostName name of the computational system where the server is located
     *    @param port server listening port size
     */

    public ArrivalLoungeStub (String hostName, int port) {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     *  Provide parameters of the problem (service request).
     *
     */
//
//    public void probPar(GenReposInfoStub reposStub, BaggageColPointStub bagColPointStub,
//                        ArrivalTermTransfQuayStub arrQuayStub, int[][] destStat, int[][] nBagsPHold) {
//
//        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
//        Message inMessage, outMessage;
//
//        while(!con.open()){  // waiting for the connection to be established
//            try {
//                Thread.currentThread().sleep((long) 10);
//            } catch (InterruptedException ignored) {}
//        }
//
//        // asks for the service to be done
//        outMessage = new Message(Message.PARAMSARRLNG, reposStub, bagColPointStub, arrQuayStub, destStat, nBagsPHold);
//        con.writeObject(outMessage);
//
//        inMessage = (Message) con.readObject();
//        if (inMessage.getType() != Message.ACK) {
//            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
//            System.out.println(inMessage.toString());
//            System.exit(1);
//        }
//        con.close();
//    }

    /* **************************************************Passenger*************************************************** */

    /**
     *   Deciding what to do next - raised by the Passenger (service solicitation).
     *   <p> Head start delay, that represents the time before the passenger chooses between what to do when arriving to
     *   the airport.
     *
     *     @return <li> true, if final destination
     *             <li> false, otherwise
     */

    public boolean whatShouldIDo(int id){

        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;
        Passenger p = (Passenger) Thread.currentThread();

        while(!con.open()){  // waiting for the connection to be established
            try {
               p.sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.WSID, id, p.getSt().ordinal());
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        if ((inMessage.getType() != Message.FNDST) && (inMessage.getType() != Message.TRDST)){
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();

        return inMessage.getType() == Message.FNDST;
    }

    /* **************************************************Porter****************************************************** */

    /**
     *   Operation of taking a rest (raised by the Porter).
     *   The porter is waken up by the operation whatShouldIDo of the last of the passengers to reach the arrival
     *   lounge or when the day is over.
     *
     *     @return <li> 'E', if end of state </li>
     *             <li> 'R', otherwise </li>
     */

    public char takeARest(){

        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;
        Porter pt = (Porter) Thread.currentThread();

        while(!con.open()){  // waiting for the connection to be established
            try {
                pt.sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.TAKEARST, pt.getStatPorter().ordinal());
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        if ((inMessage.getType() != Message.TAKERSTDONE/*CONTPORTER*/) && (inMessage.getType() != Message.ENDPORTER)) {
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();

        return (inMessage.getType() == Message.ENDPORTER) ? 'E' : 'R';
    }

    /**
     *   Operation of trying to collect a bag from the Plane's hold (raised by the Porter).
     *
     *     @return <li> a bag, if there is still bags on the Plane's hold.</li>
     *             <li> null, otherwise. </li>
     */

    public Bag tryToCollectABag(){

        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;
        Porter pt = (Porter) Thread.currentThread();
        while(!con.open()){  // waiting for the connection to be established
            try {
                pt.sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.TRYTOCOL, pt.getStatPorter().ordinal());
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if( (inMessage.getType() == Message.BAG &&
                ( (inMessage.getMsgBagDestStat() != 0 || inMessage.getMsgBagDestStat() != 1)
                    || inMessage.getMsgBagIdOwner() > SimulPar.N_PASS_PER_FLIGHT) )
            || ( (inMessage.getType() != Message.BAG) && (inMessage.getType() != Message.NULLBAG))){
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();

        return (inMessage.getType() == Message.NULLBAG) ? null : new Bag(inMessage.getMsgBagDestStat(),
                                                                         inMessage.getMsgBagIdOwner());

    }

    /**
     *   Operation of waking up the passengers that are waiting for their bag at the baggage collection point or the
     *   passengers that want to prepare the next leg and are waiting for all the bags to be collected (raised by the
     *   Porter).
     */

    public void noMoreBagsToCollect(){

        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;
        Porter pt = (Porter) Thread.currentThread();
        while(!con.open()){  // waiting for the connection to be established
            try {
                pt.sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.NOBAGS2COL, pt.getStatPorter().ordinal());
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /**
     *   Resets that need to be done after the passengers leave the airport.
     *   Notifies the porter and bus driver of the end of the day.
     *    @param bagAndPassDest Destination states of the bags of the passengers.
     *    @param nBagsNA Numbers of bags per passenger that arrive the plane's hold.
     */

    public void resetArrivalLounge(int[][] bagAndPassDest, int[][] nBagsNA){

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;
        Passenger p = (Passenger) Thread.currentThread();

        while(!con.open()){  // waiting for the connection to be established
            try {
                p.sleep((long) 10);
            } catch (InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.RESETAL, bagAndPassDest, nBagsNA);
        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.RESETALDONE) {
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /* ************************************************* Setters ******************************************************/

    /**
     *   Sets the Departure Terminal Entrance Reference Stub.
     *
     *    @param departureTermStub Departure Terminal Entrance Stub.
     */

    public void setDepartureTerminalRef(DepartureTerminalEntranceStub departureTermStub){

        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        Passenger p = (Passenger) Thread.currentThread();

        while(!con.open()){  // waiting for the connection to be established
            try {
                p.sleep((long) 10);
            } catch(InterruptedException ignored){}
        }

        // asks for the service to be done
        outMessage = new Message(Message.SETDEPTERNREF, departureTermStub);
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
     *   Shut down the server (service request).
     */

    public void shutdown(){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;
        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }

        // asks for the service to be done
        outMessage = new Message(Message.SHUT);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK){
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
}
