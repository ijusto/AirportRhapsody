package clientSide.sharedRegionsStubs;

import clientSide.clients.ClientCom;
import comInf.Message;

public class DepartureTermTransfQuayStub {

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

    public DepartureTermTransfQuayStub (String hostName, int port) {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     *  Fornecer parâmetros do problema (solicitação do serviço).
     *
     */

    public void probPar (GenReposInfoStub reposStub){

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ()){                                                // aguarda ligação
            try {
                Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (Message.PARAMSDEPTTQUAY, reposStub);
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType() != Message.ACK) {
            System.out.println("Arranque da simulação: Tipo inválido!");
            System.out.println(inMessage.toString ());
            System.exit (1);
        }
        con.close ();
    }


    //TODO: Change messages
    public void leaveTheBus(int passengerId){
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

    /* *************************************************Bus Driver*************************************************** */

    /**
     *   BusDriver informs the passengers they can leave the bus (raised by the BusDriver).
     *   Then he waits for a notification of the last passenger to leave the bus.
     */
    //TODO: Change messages
    public void parkTheBusAndLetPassOff() {

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


    /* ************************************************* Getters ******************************************************/

    /**
     *   Signaling the bus driver will to let the passengers enter the bus.
     *
     *    @return <li>true, if the bus driver wants the passengers to leave the bus</li>
     *            <li>false, otherwise</li>
     */

    public boolean canPassLeaveTheBus() {
        return this.busDoorsOpen;
    }

    /**
     *   Getter for the value of the of ths counter of passengers on the bus.
     *
     *    @return the value of the counter.
     */

    public int getNPassOnTheBusValue(){
        synchronized (lockNPassOnTheBus) {
            return nPassOnTheBus;
        }
    }

    /* ************************************************* Setters ******************************************************/

    /**
     *   Sets the bus driver will to let the passengers enter the bus to true.
     */

    public void pleaseLeaveTheBus() {
        this.busDoorsOpen = true;
    }

    /**
     *   Sets the number of passengers on the bus.
     *
     *   @param nPassOnTheBus Counter of passengers on the bus.
     */

    public void setNPassOnTheBusValue(int nPassOnTheBus){
        synchronized (lockNPassOnTheBus) {
            this.nPassOnTheBus = nPassOnTheBus;
        }
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
