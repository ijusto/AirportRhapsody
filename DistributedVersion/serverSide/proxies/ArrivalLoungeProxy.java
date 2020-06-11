package serverSide.proxies;

import clientSide.entities.BusDriverStates;
import clientSide.entities.Passenger;
import clientSide.entities.PassengerStates;
import clientSide.entities.PorterStates;
import comInf.*;
import serverSide.interfaces.ArrivalLoungeInterface;
import serverSide.ServerCom;

/**
 *
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class ArrivalLoungeProxy extends Thread implements PassengerInterface, PorterInterface, BusDriverInterface {

    private BusDriverStates busStates;

    private int nPass;

    private Passenger.SiPass si;

    private PassengerStates st;

    private int NA;

    private int NR;

    private int id;

    private PorterStates pSt;

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
        super ("Proxy_" + ArrivalLoungeProxy.getProxyId ());

        this.sconi = sconi;
        this.arrivalLoungeInterface = arrivalLoungeInterface;
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
            outMessage = arrivalLoungeInterface.processAndReply(inMessage);         // processá-lo
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


    /*********************** INTERFACE IMPLEMENTATION ****************************/

    @Override
    public int getNPassOnTheBus() {
        return nPass;
    }

    @Override
    public void setStat(BusDriverStates stat) {
        this.busStates = stat;
    }

    @Override
    public void setNPassOnTheBus(int nPassOnTheBus) {
        this.nPass = nPassOnTheBus;
    }

    @Override
    public BusDriverStates getStat() {
        return busStates;
    }

    /********************************* PASSENGER ****************************************/

    @Override
    public Passenger.SiPass getSi() {
        return si;
    }

    @Override
    public PassengerStates getSt() {
        return st;
    }

    @Override
    public int getNA() {
        return NA;
    }

    @Override
    public int getNR() {
        return NR;
    }

    @Override
    public int getPassengerID() {
        return id;
    }

    @Override
    public void setSt(PassengerStates st) {
        this.st = st;
    }

    @Override
    public void setNA(int NA) {
        this.NA = NA;
    }

    @Override
    public void setSi(Passenger.SiPass si) {
        this.si = si;
    }

    @Override
    public void setNR(int NR) {
        this.NR = NR;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
    /*********************************PORTER****************************************/

    @Override
    public void setStatPorter(PorterStates stat) {
        this.pSt = stat;
    }

    @Override
    public PorterStates getStatPorter() {
        return pSt;
    }

}
