package clientSide.sharedRegionsStubs;

import clientSide.clients.ClientCom;
import comInf.Message;

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

    public void probPar (GenReposInfoStub reposStub, ArrivalLoungeStub arrivLoungeStub,
                         ArrivalTermTransfQuayStub arrivalQuayStub) {

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ()){                                                // aguarda ligação
            try {
                Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (Message.PARAMSDEPTENT, reposStub, arrivLoungeStub, arrivalQuayStub);
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType() != Message.ACK) {
            System.out.println("Arranque da simulação: Tipo inválido!");
            System.out.println(inMessage.toString ());
            System.exit (1);
        }
        con.close ();
    }

    public void prepareNextLeg(int passengerId){

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ()) {
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException e) {}
        }

        outMessage = new Message (Message.PREPARENEXTLEG, passengerId);  //pede report missing bags
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();

        if (inMessage.getType() != Message.PNLDONE)
        { System.out.println ("Arranque da simulação: Tipo inválido!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
    }

    /**
     *   Resets the signal of the porter of the plane's hold empty state.
     */
    public void resetDepartureTerminalExit(){

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ()) {
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException e) {}
        }

        outMessage = new Message (Message.RESETDTE);  //pede report missing bags
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();

        if (inMessage.getType() != Message.ACK)
        { System.out.println ("Arranque da simulação: Tipo inválido!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close ();

    }

    /**
     *   Wakes up all the passengers at the Departure Terminal Entrance.
     */
    public void notifyFromGoHome(){

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ()) {
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException e) {}
        }

        outMessage = new Message (Message.NOTFGOHOME);  //pede report missing bags
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();

        if (inMessage.getType() != Message.ACK)
        { System.out.println ("Arranque da simulação: Tipo inválido!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
    }

    /**
     *   Called by Porter in noMoreBagsToCollect to wake up all the passengers that are waiting for the plane's hold to
     *   be out of bags.
     */

    public void noMoreBags() {

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ()) {
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException e) {}
        }

        outMessage = new Message (Message.NOMOREBAGS);  //pede report missing bags
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();

        if (inMessage.getType() != Message.ACK)
        { System.out.println ("Arranque da simulação: Tipo inválido!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
        con.close ();
    }

    /* ************************************************* Setters ******************************************************/

    /**
     *   Sets the Arrival Terminal Exit Stub Reference.
     *
     *    @param arrivalTermStub Arrival Terminal Exit Stub.
     */

    public void setArrivalTerminalRef(ArrivalTerminalExitStub arrivalTermStub){
        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ()) {
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException e) {}
        }

        outMessage = new Message (Message.SETARRTERREF, arrivalTermStub);  //pede report missing bags
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();

        if (inMessage.getType() != Message.ACK)
        { System.out.println ("Arranque da simulação: Tipo inválido!");
            System.out.println (inMessage.toString ());
            System.exit (1);
        }
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


}
