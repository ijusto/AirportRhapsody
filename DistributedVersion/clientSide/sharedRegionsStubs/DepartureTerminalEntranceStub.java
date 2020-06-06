package clientSide.sharedRegionsStubs;

import clientSide.clients.ClientCom;
import comInf.Message;
import old.GenReposInfo;
import serverSide.sharedRegions.ArrivalLounge;
import serverSide.sharedRegions.ArrivalTermTransfQuay;

public class DepartureTerminalEntranceStub {

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

    public DepartureTerminalEntranceStub (String hostName, int port) {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     *  Fornecer parâmetros do problema (solicitação do serviço).
     *
     */

    public void probPar (GenReposInfo repos, ArrivalLoungeStub arrivLoungeStub, ArrivalTermTransfQuayStub arrivalQuayStub)//(String fName, int nIter)
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


    //TODO: Change messages
    public boolean prepareNextLeg(){

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ()) {
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException e) {}
        }

        outMessage = new Message (Message.REQCUTH, passengerId);  //pede report missing bags
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();

        if (inMessage.getType() != Message.NFICDONE)
        { System.out.println ("Arranque da simulação: Tipo inválido!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close ();

        if (inMessage.getType () == Message.CUTHDONE)
            return true;                                                // operação bem sucedida
        else return false;
    }

    /**
     *   Resets the signal of the porter of the plane's hold empty state.
     */
    //TODO: Change messages
    public boolean resetDepartureTerminalExit(){

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ()) {
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException e) {}
        }

        outMessage = new Message (Message.REQCUTH, passengerId);  //pede report missing bags
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();

        if (inMessage.getType() != Message.NFICDONE)
        { System.out.println ("Arranque da simulação: Tipo inválido!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close ();

        if (inMessage.getType () == Message.CUTHDONE)
            return true;                                                // operação bem sucedida
        else return false;

    }

    /**
     *   Wakes up all the passengers at the Arrival Terminal Exit and at the Departure Terminal Entrance.
     */

    //TODO: Change messages
    public boolean wakeAllPassengers(){
        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ()) {
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException e) {}
        }

        outMessage = new Message (Message.REQCUTH, passengerId);  //pede report missing bags
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();

        if (inMessage.getType() != Message.NFICDONE)
        { System.out.println ("Arranque da simulação: Tipo inválido!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close ();

        if (inMessage.getType () == Message.CUTHDONE)
            return true;                                                // operação bem sucedida
        else return false;
    }

    /**
     *   Wakes up all the passengers at the Departure Terminal Entrance.
     */
    //TODO: Change messages
    public boolean notifyFromGoHome(){

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ()) {
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException e) {}
        }

        outMessage = new Message (Message.REQCUTH, passengerId);  //pede report missing bags
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();

        if (inMessage.getType() != Message.NFICDONE)
        { System.out.println ("Arranque da simulação: Tipo inválido!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close ();

        if (inMessage.getType () == Message.CUTHDONE)
            return true;                                                // operação bem sucedida
        else return false;
    }

    /**
     *   Called by Porter in noMoreBagsToCollect to wake up all the passengers that are waiting for the plane's hold to
     *   be out of bags.
     */
    //TODO: Change messages
    public boolean noMoreBags() {
        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ()) {
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException e) {}
        }

        outMessage = new Message (Message.REQCUTH, passengerId);  //pede report missing bags
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();

        if (inMessage.getType() != Message.NFICDONE)
        { System.out.println ("Arranque da simulação: Tipo inválido!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close ();

        if (inMessage.getType () == Message.CUTHDONE)
            return true;                                                // operação bem sucedida
        else return false;
    }

    /* ************************************************* Setters ******************************************************/

    /**
     *   Sets the Arrival Terminal Exit Reference.
     *
     *    @param arrivalTerm Arrival Terminal Exit.
     */
    //TODO: Change messages
    public boolean setArrivalTerminalRef(ArrivalTerminalExit arrivalTerm){
        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ()) {
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException e) {}
        }

        outMessage = new Message (Message.REQCUTH, passengerId);  //pede report missing bags
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();

        if (inMessage.getType() != Message.NFICDONE)
        { System.out.println ("Arranque da simulação: Tipo inválido!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close ();

        if (inMessage.getType () == Message.CUTHDONE)
            return true;                                                // operação bem sucedida
        else return false;
    }
}
