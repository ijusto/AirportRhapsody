package serverSide.servers;

import clientSide.SimulPar;
import serverSide.ServerCom;
import serverSide.interfaces.ArrivalLoungeInterface;
import serverSide.proxies.ArrivalLoungeProxy;
import serverSide.sharedRegions.ArrivalLounge;

import java.net.*;

public class ServerArrivalLounge {

    /**
     *  Número do port de escuta do serviço a ser prestado (4000, por defeito)
     *
     *    @serialField portNumb
     */

    private static final int portNumb = SimulPar.arrivalLoungePort;
    public static boolean waitConnection;                              // sinalização de actividade

    /**
     *  Programa principal.
     */

    public static void main (String [] args)
    {
        ArrivalLounge arrivalLounge;                                    // barbearia (representa o serviço a ser prestado)
        ArrivalLoungeInterface arrivalLoungeInter;                      // interface à barbearia
        ServerCom scon, sconi;                               // canais de comunicação
        ArrivalLoungeProxy cliProxy;                                // thread agente prestador do serviço

        /* estabelecimento do servico */

        scon = new ServerCom(portNumb);                     // criação do canal de escuta e sua associação
        scon.start ();                                       // com o endereço público
        arrivalLounge = new ArrivalLounge();                           // activação do serviço
        arrivalLoungeInter = new ArrivalLoungeInterface(arrivalLounge);        // activação do interface com o serviço
        System.out.println("O serviço foi estabelecido!");
        System.out.println("O servidor esta em escuta.");

        /* processamento de pedidos */

        waitConnection = true;
        while (waitConnection)
            try {
                sconi = scon.accept ();                          // entrada em processo de escuta
                cliProxy = new ArrivalLoungeProxy(sconi, arrivalLoungeInter);  // lançamento do agente prestador do serviço
                cliProxy.start ();
            } catch (SocketTimeoutException e) {}
        scon.end ();                                         // terminação de operações
        System.out.println("O servidor foi desactivado.");
    }
}