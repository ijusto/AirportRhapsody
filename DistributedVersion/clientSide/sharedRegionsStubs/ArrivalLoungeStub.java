package clientSide.sharedRegionsStubs;

import clientSide.clients.ClientCom;
import clientSide.entities.Porter;
import clientSide.PorterStates;
import clientSide.SimulPar;
import comInf.Bag;
import comInf.Message;
import old.GenReposInfo;
import serverSide.sharedRegions.ArrivalTermTransfQuay;
import serverSide.sharedRegions.BaggageColPoint;

import java.util.HashMap;
import java.util.Map;

public class ArrivalLoungeStub {

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

    public ArrivalLoungeStub (String hostName, int port) {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     *  Fornecer parâmetros do problema (solicitação do serviço).
     *
     */

    public void probPar (GenReposInfo repos, BaggageColPointStub bagColPointStub, ArrivalTermTransfQuayStub arrQuayStub,
                         Bag.DestStat[][] destStat, int[][] nBagsPHold)//(String fName, int nIter)
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


    /* **************************************************Passenger*************************************************** */

    /**
     *   Deciding what to do next - raised by the Passenger (service solicitation).
     *   <p> Head start delay, that represents the time before the passenger chooses between what to do when arriving to
     *   the airport.
     *
     *     @param  passengerId passenger id
     *     @return <li> true, if final destination
     *             <li> false, otherwise
     */

    public boolean whatShouldIDo(int passengerId){

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ()) {                                 // aguarda ligação
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException e) {}
        }

        outMessage = new Message (Message.WSID, passengerId);        // pede a realização do serviço
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();

        if ((inMessage.getType () != Message.FNDST) && (inMessage.getType () != Message.TRDST)) {
            System.out.println("Thread " + Thread.currentThread ().getName () + ": Tipo inválido!");
            System.out.println(inMessage.toString ());
            System.exit (1);
        }
        con.close ();

        if (inMessage.getType () == Message.FNDST)
            return true;                                                // operação bem sucedida - final destination
        else return false;                                          // operação falhou - otherwise

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

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ()) {                                    // aguarda ligação
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException e) {}
        }

        outMessage = new Message (Message.TAKEARST);     // o barbeiro vai dormir
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();

        if ((inMessage.getType () != Message.TAKERSTDONE/*CONTPORTER*/) && (inMessage.getType () != Message.ENDPORTER)) {
            System.out.println("Thread " + Thread.currentThread ().getName ()+ ": Tipo inválido!");
            System.out.println(inMessage.toString ());
            System.exit (1);
        }
        con.close ();

        if (inMessage.getType () == Message.ENDPORTER)
            return 'E';                                          // fim de operações
        else return 'R';
    }

    /**
     *   Operation of trying to collect a bag from the Plane's hold (raised by the Porter).
     *
     *     @return <li> a bag, if there is still bags on the Plane's hold.</li>
     *             <li> null, otherwise. </li>
     */

    public Bag tryToCollectABag(){

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ()) {                                    // aguarda ligação
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException e) {}
        }

        outMessage = new Message (Message.TRYTOCOL);     // o barbeiro vai dormir
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();

        if ((inMessage.getType () != Message.BAG) && (inMessage.getType () != Message.NULLBAG)) {
            System.out.println("Thread " + Thread.currentThread ().getName ()+ ": Tipo inválido!");
            System.out.println(inMessage.toString ());
            System.exit (1);
        }
        con.close ();

        if (inMessage.getType () == Message.NULLBAG)
            return null;
        else {
            return new Bag(inMessage.getMsgBagDestStat(), inMessage.getMsgBagIdOwner());
        }
    }

    /**
     *   Operation of waking up the passengers that are waiting for their bag at the baggage collection point or the
     *   passengers that want to prepare the next leg and are waiting for all the bags to be collected (raised by the
     *   Porter).
     */

    public void noMoreBagsToCollect(){

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ()) {                                               // aguarda ligação
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException e) {}
        }
        outMessage = new Message (Message.NOBAGS2COL);    // o barbeiro recebe o pagamento
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK) {
            System.out.println("Thread " + Thread.currentThread ().getName () + ": Tipo inválido!");
            System.out.println(inMessage.toString ());
            System.exit (1);
        }
        con.close ();
    }

    /**
     *   Resets that need to be done after the passengers leave the airport.
     *   Notifies the porter and bus driver of the end of the day.
     *    @param bagAndPassDest Destination states of the bags of the passengers.
     *    @param nBagsNA Numbers of bags per passenger that arrive the plane's hold.
     *    @throws MemException Exception.
     */

    public void resetArrivalLounge(){

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ()) {                                               // aguarda ligação
            try {
                Thread.currentThread().sleep ((long) (10));
            } catch (InterruptedException e) {}
        }
        outMessage = new Message(Message.RESETAL);    // o barbeiro recebe o pagamento
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.RESETALDONE) {
            System.out.println("Thread " + Thread.currentThread ().getName () + ": Tipo inválido!");
            System.out.println(inMessage.toString ());
            System.exit (1);
        }
        con.close ();
    }

    /**
     *   Operation of incrementing/decrementing the counter.
     *
     *    @return <li>true, if the value of the counter after the operation is the limit.</li>
     *            <li>false, otherwise.</li>
     */

    public boolean incDecNPassAtArrivLCounter(boolean inc) {
        synchronized (lockNnPassAtArrivLCounter) {
            if(inc) {
                nPassAtArrivL++;
            } else {
                nPassAtArrivL--;
            }
            return nPassAtArrivL == SimulPar.N_PASS_PER_FLIGHT;
        }
    }

    /**
     *   Sets the value of the counter to zero.
     */

    public void resetNPassAtArrivL(){
        synchronized (lockNnPassAtArrivLCounter) { // Locks on the private Object
            nPassAtArrivL = 0;
        }
    }

    /* ************************************************* Getters ******************************************************/

    /**
     *   Getter for the value of the of passengers that are currently ate the Arrival Lounge.
     *
     *    @return the value of the counter.
     */

    public int getNPassAtArrivLValue(){
        synchronized (lockNnPassAtArrivLCounter) {
            return nPassAtArrivL;
        }
    }

    /* ************************************************* Setters ******************************************************/

    /**
     *   Sets the Departure Terminal Entrance Reference.
     *
     *    @param departureTerm Departure Terminal Entrance.
     */

    public synchronized void setDepartureTerminalRef(DepartureTerminalEntrance departureTerm){
        this.depTerm = departureTerm;
    }

    /**
     *   Signals the end of the day and notifies the Porter if he is waiting in takeARest().
     */

    public synchronized void setEndDay(){
        this.endDay = true;
        notifyAll();
    }
}
