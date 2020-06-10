package serverSide.servers;

import comInf.SimulPar;
import clientSide.sharedRegionsStubs.GenReposInfoStub;
import serverSide.ServerCom;
import serverSide.interfaces.DepartureTermTransfQuayInterface;
import serverSide.proxies.DepartureTermTransfQuayProxy;
import serverSide.sharedRegions.DepartureTermTransfQuay;

import java.net.SocketTimeoutException;

public class ServerDepartureTermTransfQuay {

    /**
     *  Número do port de escuta do serviço a ser prestado (4000, por defeito)
     *
     *    @serialField portNumb
     */

    private static final int portNumb = SimulPar.depTTQuayPort;
    public static boolean waitConnection;                              // sinalização de actividade

    /**
     *  Programa principal.
     */

    public static void main (String [] args)
    {
        DepartureTermTransfQuay departureTermTransfQuay;                                    // barbearia (representa o serviço a ser prestado)
        DepartureTermTransfQuayInterface departureTermTransfQuayInter;                      // interface à barbearia
        ServerCom scon, sconi;                               // canais de comunicação
        DepartureTermTransfQuayProxy cliProxy;                                // thread agente prestador do serviço
        GenReposInfoStub reposInfoStub;
        /* estabelecimento do servico */

        scon = new ServerCom(portNumb);                     // criação do canal de escuta e sua associação
        scon.start ();
        reposInfoStub = new GenReposInfoStub(SimulPar.genReposInfoHost, SimulPar.genReposInfoPort);// com o endereço público
        departureTermTransfQuay = new DepartureTermTransfQuay(reposInfoStub);                           // activação do serviço
        departureTermTransfQuayInter = new DepartureTermTransfQuayInterface(departureTermTransfQuay);        // activação do interface com o serviço
        System.out.println("O serviço foi estabelecido!");
        System.out.println("O servidor esta em escuta.");

        /* processamento de pedidos */

        waitConnection = true;
        while (waitConnection)
            try {
                sconi = scon.accept ();                          // entrada em processo de escuta
                cliProxy = new DepartureTermTransfQuayProxy(sconi, departureTermTransfQuayInter);  // lançamento do agente prestador do serviço
                cliProxy.start ();
            } catch (SocketTimeoutException e) {}
        scon.end ();                                         // terminação de operações
        System.out.println("O servidor foi desactivado.");
    }
}