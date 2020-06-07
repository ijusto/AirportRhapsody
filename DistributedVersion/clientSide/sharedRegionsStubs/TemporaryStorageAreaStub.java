package clientSide.sharedRegionsStubs;

import clientSide.PorterStates;
import clientSide.SimulPar;
import clientSide.clients.ClientCom;
import clientSide.entities.Porter;
import comInf.Bag;
import comInf.MemException;
import comInf.MemStack;
import comInf.Message;
import serverSide.sharedRegions.GenReposInfo;

public class TemporaryStorageAreaStub {

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

    public TemporaryStorageAreaStub (String hostName, int port) {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     *  Fornecer parâmetros do problema (solicitação do serviço).
     *
     */

    public void probPar (GenReposInfo repos)//(String fName, int nIter)
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
     *   Operation of carrying a bag from the plane's hold to the temporary storage area (raised by the Porter).
     */

    public void carryItToAppropriateStore(Bag bag){
        Porter porter = (Porter) Thread.currentThread();
        assert porter.getStat() == PorterStates.AT_THE_PLANES_HOLD;
        assert bag != null;
        porter.setStat(PorterStates.AT_THE_STOREROOM);
        repos.updatePorterStat(PorterStates.AT_THE_STOREROOM);

        try {
            tmpStorageStack.write(bag);
            repos.saveBagInSR();
        } catch (MemException e) {
            e.printStackTrace();
        }

        repos.printLog();
    }

    /**
     *   Resets the stack of bags when all passengers from a flight leave the airport.
     */

    public void resetTemporaryStorageArea() {
        // stack of the store room instantiation
        try {
            tmpStorageStack = new MemStack<>(new Bag [SimulPar.N_PASS_PER_FLIGHT * SimulPar.N_BAGS_PER_PASS]);
        } catch (MemException e) {
            e.printStackTrace();
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
