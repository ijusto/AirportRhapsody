package serverSide.servers;

import comInf.SimulPar;
import clientSide.sharedRegionsStubs.GenReposInfoStub;
import serverSide.ServerCom;
import serverSide.interfaces.ArrivalTerminalExitInterface;
import serverSide.proxies.ArrivalTerminalExitProxy;
import serverSide.sharedRegions.ArrivalTerminalExit;

import java.net.*;

/**
 *
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class ServerArrivalTerminalExit {

    /**
     *  Número do port de escuta do serviço a ser prestado (4000, por defeito)
     *
     *    @serialField portNumb
     */

    private static final int portNumb = SimulPar.arrivalTermExitPort;
    public static boolean waitConnection;                              // sinalização de actividade

    /**
     *  Programa principal.
     */



    public static void main (String [] args)
    {
        ArrivalTerminalExit arrivalTerminalExit;                                    // barbearia (representa o serviço a ser prestado)
        ArrivalTerminalExitInterface arrivalTerminalExitInterface;                      // interface à barbearia
        ServerCom scon, sconi;                               // canais de comunicação
        ArrivalTerminalExitProxy cliProxy;                                // thread agente prestador do serviço

        GenReposInfoStub repoInfoStub = new GenReposInfoStub(SimulPar.genReposInfoHost, SimulPar.genReposInfoPort);

        /* estabelecimento do servico */

        scon = new ServerCom(portNumb);                     // criação do canal de escuta e sua associação
        scon.start ();                                       // com o endereço público
        arrivalTerminalExit = new ArrivalTerminalExit(repoInfoStub);                           // activação do serviço
        arrivalTerminalExitInterface = new ArrivalTerminalExitInterface(arrivalTerminalExit);        // activação do interface com o serviço
        System.out.println("O serviço foi estabelecido!");
        System.out.println("O servidor esta em escuta.");

        /* processamento de pedidos */

        waitConnection = true;
        while (waitConnection) {
            try {
                sconi = scon.accept();                          // entrada em processo de escuta
                cliProxy = new ArrivalTerminalExitProxy(sconi, arrivalTerminalExitInterface);  // lançamento do agente prestador do serviço
                cliProxy.start();
            } catch (SocketTimeoutException e) {
            }
        }
        scon.end ();                                         // terminação de operações
        System.out.println("O servidor foi desactivado.");
    }
}
