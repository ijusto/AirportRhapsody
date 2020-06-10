package serverSide.servers;

import comInf.SimulPar;
import clientSide.sharedRegionsStubs.GenReposInfoStub;
import serverSide.ServerCom;
import serverSide.interfaces.BaggageReclaimOfficeInterface;
import serverSide.proxies.BaggageReclaimOfficeProxy;
import serverSide.sharedRegions.BaggageReclaimOffice;

import java.net.SocketTimeoutException;

/**
 *
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class ServerBaggageReclaimOffice {

    /**
     *  Número do port de escuta do serviço a ser prestado (4000, por defeito)
     *
     *    @serialField portNumb
     */

    private static final int portNumb = SimulPar.bgrOfficePort;
    public static boolean waitConnection;                              // sinalização de actividade

    /**
     *  Programa principal.
     */

    public static void main (String [] args)
    {
        BaggageReclaimOffice baggageReclaimOffice;                                    // barbearia (representa o serviço a ser prestado)
        BaggageReclaimOfficeInterface baggageReclaimOfficeInter;                      // interface à barbearia
        ServerCom scon, sconi;                               // canais de comunicação
        BaggageReclaimOfficeProxy cliProxy;
        GenReposInfoStub reposInfoStub;                         // thread agente prestador do serviço

        /* estabelecimento do servico */

        scon = new ServerCom(portNumb);                     // criação do canal de escuta e sua associação
        scon.start ();                                       // com o endereço público
        reposInfoStub = new GenReposInfoStub(SimulPar.genReposInfoHost, SimulPar.genReposInfoPort);
        baggageReclaimOffice = new BaggageReclaimOffice(reposInfoStub);                           // activação do serviço
        baggageReclaimOfficeInter = new BaggageReclaimOfficeInterface(baggageReclaimOffice);        // activação do interface com o serviço
        System.out.println("O serviço foi estabelecido!");
        System.out.println("O servidor esta em escuta.");

        /* processamento de pedidos */

        waitConnection = true;
        while (waitConnection)
            try {
                sconi = scon.accept ();                          // entrada em processo de escuta
                cliProxy = new BaggageReclaimOfficeProxy(sconi, baggageReclaimOfficeInter);  // lançamento do agente prestador do serviço
                cliProxy.start ();
            } catch (SocketTimeoutException e) {}
        scon.end ();                                         // terminação de operações
        System.out.println("O servidor foi desactivado.");
    }
}
