package serverSide.proxies;

import clientSide.entities.BusDriverStates;
import clientSide.entities.Passenger;
import clientSide.entities.PassengerStates;
import clientSide.entities.PorterStates;
import comInf.*;
import serverSide.interfaces.ArrivalLoungeInterface;
import serverSide.ServerCom;
import comInf.CommonProvider;
/**
 *
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class ArrivalLoungeProxy extends CommonProvider{

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
     *    @serialField arrivalLoungeInterface
     */

    private ArrivalLoungeInterface arrivalLoungeInterface;

    /**
     *  Instanciação do interface
     *
     *    @param sconi canal de comunicação
     *    @param arrivalLoungeInterface
     */

    public ArrivalLoungeProxy(ServerCom sconi, ArrivalLoungeInterface arrivalLoungeInterface) {
        super("Proxy_ArrivalLoungeProxy_" + ArrivalLoungeProxy.getProxyId());

        this.sconi = sconi;
        this.arrivalLoungeInterface = arrivalLoungeInterface;
    }

    /**
     *  Ciclo de vida do thread agente prestador de serviço.
     */

    @Override
    public void run() {
        Message inMessage = null,                                      // mensagem de entrada
                outMessage = null;                      // mensagem de saída

        inMessage = (Message) sconi.readObject();                     // ler pedido do cliente
        try {
            outMessage = arrivalLoungeInterface.processAndReply(inMessage);         // processá-lo
        } catch (MessageException e) {
            System.out.println("Thread " + getName() + ": " + e.getMessage() + "!");
            System.out.println(e.getMessageVal().toString());
            System.exit(1);
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

    public ServerCom getScon ()
    {
        return sconi;
    }

}
