package serverSide.proxies;

import comInf.Message;
import comInf.MessageException;
import serverSide.interfaces.ArrivalTermTransfQuayInterface;
import serverSide.interfaces.DepartureTermTransfQuayInterface;

/**
 *   Este tipo de dados define o thread agente prestador de serviço para uma solução do Problema dos Barbeiros
 *   Sonolentos que implementa o modelo cliente-servidor de tipo 2 (replicação do servidor) com lançamento estático dos
 *   threads barbeiro.
 *   A comunicação baseia-se em passagem de mensagens sobre sockets usando o protocolo TCP.
 */

public class BusDriverProxy extends Thread
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
     *   Arrival Terminal Transfer Quay Interface.
     *
     *    @serialField arrivalTerminalQuayInterface
     */

    ArrivalTermTransfQuayInterface arrivalTerminalQuayInterface;

    /**
     *   Departure Terminal Transfer Quay Interface.
     *
     *    @serialField departureTransferQuayInterface
     */

    DepartureTermTransfQuayInterface departureTransferQuayInterface;

    /**
     *  Instanciação do interface à barbearia.
     *
     *    @param sconi canal de comunicação
     *    @param arrivalTerminalQuayInterface Departure Terminal Transfer Quay Interface.
     *    @param departureTransferQuayInterface Arrival Terminal Transfer Quay Interface.
     */

    public BusDriverProxy (ServerCom sconi, /*BarberShopInterface bShopInter,*/
                        ArrivalTermTransfQuayInterface arrivalTerminalQuayInterface,
                        DepartureTermTransfQuayInterface departureTransferQuayInterface)
    {
        super ("Proxy_" + BusDriverProxy.getProxyId ());

        this.sconi = sconi;
        /*this.bShopInter = bShopInter;*/
        this.arrivalTerminalQuayInterface = arrivalTerminalQuayInterface;
        this.departureTransferQuayInterface = departureTransferQuayInterface;
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

