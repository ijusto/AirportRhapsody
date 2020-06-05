package serverSide.proxys;

import clientSide.stubs.ArrivalLoungeStub;
import clientSide.stubs.BaggageColPointStub;
import clientSide.stubs.TemporaryStorageAreaStub;
import comInf.Message;
import comInf.MessageException;
import serverSide.ServerCom;
import serverSide.interfaces.ArrivalLoungeInterface;
import serverSide.interfaces.BaggageColPointInterface;
import serverSide.interfaces.TemporaryStorageAreaInterface;

/**
 *   Este tipo de dados define o thread agente prestador de serviço para uma solução do Problema dos Barbeiros
 *   Sonolentos que implementa o modelo cliente-servidor de tipo 2 (replicação do servidor) com lançamento estático dos
 *   threads barbeiro.
 *   A comunicação baseia-se em passagem de mensagens sobre sockets usando o protocolo TCP.
 */

public class PorterProxy extends Thread
{
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
     *  ...
     *
     *    @serialField arrivalLoungeInterface
     */

    ArrivalLoungeInterface arrivalLoungeInterface;

    /**
     *  ...
     *
     *    @serialField tempStoreInterface
     */

    TemporaryStorageAreaInterface tempStoreInterface;

    /**
     *  ...
     *
     *    @serialField bColPntInterface
     */

    BaggageColPointInterface bColPntInterface;

    /**
     *  Instanciação do interface à barbearia.
     *
     *    @param sconi canal de comunicação
     *    @param arrivalLoungeInterface  ...
     *    @param tempStoreInterface ...
     *    @param bColPntInterface ....
     */

    public PorterProxy (ServerCom sconi, /*BarberShopInterface bShopInter*/
                        ArrivalLoungeInterface arrivalLoungeInterface, TemporaryStorageAreaInterface tempStoreInterface,
                        BaggageColPointInterface bColPntInterface)
    {
        super ("Proxy_" + PorterProxy.getProxyId ());

        this.sconi = sconi;
        /*this.bShopInter = bShopInter;*/
        this.arrivalLoungeInterface = arrivalLoungeInterface;
        this.tempStoreInterface = tempStoreInterface;
        this.bColPntInterface = bColPntInterface;
    }

    /**
     *  Ciclo de vida do thread agente prestador de serviço.
     */

    @Override
    public void run ()
    {
        Message inMessage = null,                                       // mensagem de entrada
                outMessage = null;                                      // mensagem de saída

        inMessage = (Message) sconi.readObject ();                      // ler pedido do cliente
        try {
            outMessage = bShopInter.processAndReply (inMessage);        // processá-lo
        } catch (MessageException e) {
            System.out.println("Thread " + getName () + ": " + e.getMessage () + "!");
            System.out.println(e.getMessageVal ().toString ());
            System.exit (1);
        }
        sconi.writeObject (outMessage);                                // enviar resposta ao cliente
        sconi.close ();                                                // fechar canal de comunicação
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
            cl = Class.forName ("serverSide.ClientProxy");
        } catch (ClassNotFoundException e) {
            System.out.println("O tipo de dados ClientProxy não foi encontrado!");
            e.printStackTrace ();
            System.exit (1);
        }

        synchronized (cl)
        {
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
