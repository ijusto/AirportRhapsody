package serverSide.servers;

import serverSide.interfaces.ArrivalTermTransfQuayInterface;
import serverSide.proxies.ArrivalTermTransfQuayProxy;
import serverSide.sharedRegions.ArrivalTermTransfQuay;

import java.net.SocketTimeoutException;

public class ServerArrivalTermTransfQuay {
    /**
     *  Número do port de escuta do serviço a ser prestado (4000, por defeito)
     *
     *    @serialField portNumb
     */

    private static final int portNumb = 22001;
    public static boolean waitConnection;                              // sinalização de actividade

    /**
     *  Programa principal.
     */

    public static void main (String [] args)
    {
        ArrivalTermTransfQuay arrivalTermTransfQuay;                                    // barbearia (representa o serviço a ser prestado)
        ArrivalTermTransfQuayInterface arrivalTermTransfQuayInter;                      // interface à barbearia
        ServerCom scon, sconi;                               // canais de comunicação
        ArrivalTermTransfQuayProxy cliProxy;                                // thread agente prestador do serviço

        /* estabelecimento do servico */

        scon = new ServerCom(portNumb);                     // criação do canal de escuta e sua associação
        scon.start ();                                       // com o endereço público
        arrivalTermTransfQuay = new ArrivalTermTransfQuay();                           // activação do serviço
        arrivalTermTransfQuayInter = new ArrivalTermTransfQuayInterface(arrivalTermTransfQuay);        // activação do interface com o serviço
        System.out.println("O serviço foi estabelecido!");
        System.out.println("O servidor esta em escuta.");

        /* processamento de pedidos */

        waitConnection = true;
        while (waitConnection)
            try
            { sconi = scon.accept ();                          // entrada em processo de escuta
                cliProxy = new ArrivalTermTransfQuayProxy(sconi, arrivalTermTransfQuayInter);  // lançamento do agente prestador do serviço
                cliProxy.start ();
            }
            catch (SocketTimeoutException e)
            {
            }
        scon.end ();                                         // terminação de operações
        System.out.println("O servidor foi desactivado.");
    }
}
