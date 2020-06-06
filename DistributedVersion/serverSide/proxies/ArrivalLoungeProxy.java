package serverSide.proxies;

import comInf.Message;
import comInf.MessageException;
import serverSide.ServerCom;
import serverSide.interfaces.ArrivalLoungeInterface;
import serverSide.servers.ServerArrivalLoungeCom;

public class ArrivalLoungeProxy extends Thread {

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

    private ServerArrivalLoungeCom sconi;

    /**
     *
     *
     *    @serialField arrivalLoungeInterface
     */

    private ArrivalLoungeInterface arrivalLoungeInterface;

    /**
     *  Instanciação do interface
     *
     *    @param sconi canal de comunicação
     *    @param arrivalLoungeInterface
     */

    public ArrivalLoungeProxy(ServerArrivalLoungeCom sconi, ArrivalLoungeInterface arrivalLoungeInterface)
    {
        super ("Proxy_" + ArrivalLoungeProxy.getProxyId ());

        this.sconi = sconi;
        this.arrivalLoungeInterface = arrivalLoungeInterface;
    }

    /**
     *  Ciclo de vida do thread agente prestador de serviço.
     */

    @Override
    public void run ()
    {
        Message inMessage = null,                                      // mensagem de entrada
                outMessage = null;                      // mensagem de saída

        inMessage = (Message) sconi.readObject ();                     // ler pedido do cliente
        try {
            outMessage = arrivalLoungeInterface.processAndReply (inMessage);         // processá-lo
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

    private static int getProxyId ()
    {
        Class<?> cl = null;                                  // representação do tipo de dados ClientProxy na máquina
        //   virtual de Java
        int proxyId;                                         // identificador da instanciação

        try {
            cl = Class.forName ("serverSide.proxies.ArrivalLoungeProxy");
        } catch (ClassNotFoundException e) {
            System.out.println("O tipo de dados ArrivalLoungeProxy não foi encontrado!");
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

    public ServerArrivalLoungeCom getScon ()
    {
        return sconi;
    }
}
