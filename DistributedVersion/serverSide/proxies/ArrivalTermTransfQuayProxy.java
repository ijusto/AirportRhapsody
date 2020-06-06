package serverSide.proxies;

import comInf.Message;
import comInf.MessageException;
import serverSide.interfaces.ArrivalTermTransfQuayInterface;
import serverSide.servers.ServerArrivalTermTransfQuay;
import serverSide.servers.ServerCom;

public class ArrivalTermTransfQuayProxy extends Thread {

    /**
     *  Contador de threads lançados
     *
     *    @serialField nProxy
     */

    private static int nProxy = 0;

    /**
     *  Canal de comunicação
     *
     *    @serialField sconi
     */

    private ServerCom sconi;

    /**
     *
     *
     *    @serialField arrivalTermTransfQuayInterface
     */

    private ArrivalTermTransfQuayInterface arrivalTermTransfQuayInterface;

    /**
     *  Instanciação do interface
     *
     *    @param sconi canal de comunicação
     *    @param arrivalTermTransfQuayInterface
     */

    public ArrivalTermTransfQuayProxy(ServerCom sconi, ArrivalTermTransfQuayInterface arrivalTermTransfQuayInterface) {
        super ("Proxy_" + ArrivalTermTransfQuayProxy.getProxyId ());

        this.sconi = sconi;
        this.arrivalTermTransfQuayInterface = arrivalTermTransfQuayInterface;
    }

    /**
     *  Ciclo de vida do thread agente prestador de serviço.
     */

    @Override
    public void run () {
        Message inMessage = null,                                      // mensagem de entrada
                outMessage = null;                      // mensagem de saída

        inMessage = (Message) sconi.readObject ();                     // ler pedido do cliente
        try {
            outMessage = arrivalTermTransfQuayInterface.processAndReply(inMessage);         // processá-lo
        } catch (MessageException e) {
            System.out.println("Thread " + getName () + ": " + e.getMessage () + "!");
            System.out.println(e.getMessageVal ().toString ());
            System.exit (1);
        }
        sconi.writeObject(outMessage);                                // enviar resposta ao cliente
        sconi.close();                                                // fechar canal de comunicação
    }

    /**
     *  Geração do identificador da instanciação.
     *
     *    @return identificador da instanciação
     */

    private static int getProxyId () {
        Class<?> cl = null;                                  // representação do tipo de dados ClientProxy na máquina
        //   virtual de Java
        int proxyId;                                         // identificador da instanciação

        try {
            cl = Class.forName ("serverSide.proxies.ArrivalTermTransfQuayProxy");
        } catch (ClassNotFoundException e) {
            System.out.println("O tipo de dados ArrivalTermTransfQuayProxy não foi encontrado!");
            e.printStackTrace ();
            System.exit (1);
        }

        synchronized (cl) {
            proxyId = nProxy;
            nProxy += 1;
        }

        return proxyId;
    }

    /**
     *  Obtenção do canal de comunicação.
     *
     *    @return canal de comunicação
     */

    public ServerCom getScon ()
    {
        return sconi;
    }
}
