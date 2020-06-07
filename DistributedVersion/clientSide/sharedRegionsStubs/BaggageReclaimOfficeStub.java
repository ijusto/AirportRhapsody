package clientSide.sharedRegionsStubs;

import clientSide.ClientCom;
import comInf.Message;

public class BaggageReclaimOfficeStub {
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

    public BaggageReclaimOfficeStub (String hostName, int port) {
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

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message (Message.PARAMSBAGRECOFF, reposStub);
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType() != Message.ACK) {
            System.out.println("Arranque da simulação: Tipo inválido!");
            System.out.println(inMessage.toString ());
            System.exit (1);
        }
        con.close ();
    }

    public void reportMissingBags(int passengerId){
        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }

        outMessage = new Message (Message.REPORTMISSBAG, passengerId);  //pede report missing bags
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

        while(!con.open()){  // waiting for the connection to be established
            try {
                Thread.currentThread().sleep((long) 10);
            } catch (InterruptedException ignored) {}
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
