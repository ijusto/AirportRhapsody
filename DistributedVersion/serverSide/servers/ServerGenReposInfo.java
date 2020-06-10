package serverSide.servers;

import comInf.SimulPar;
import serverSide.ServerCom;
import serverSide.interfaces.GenReposInfoInterface;
import serverSide.proxies.GenReposInfoProxy;
import serverSide.sharedRegions.GenReposInfo;

import java.net.SocketTimeoutException;

public class ServerGenReposInfo {

    /**
     *  Número do port de escuta do serviço a ser prestado (4000, por defeito)
     *
     *    @serialField portNumb
     */

    private static final int portNumb = SimulPar.genReposInfoPort;
    public static boolean waitConnection;                              // sinalização de actividade

    /**
     *  Programa principal.
     */

    public static void main (String [] args)
    {
        GenReposInfo repos;                                    // barbearia (representa o serviço a ser prestado)
        GenReposInfoInterface reposInter;                      // interface à barbearia
        ServerCom scon, sconi;                               // canais de comunicação
        GenReposInfoProxy cliProxy;                                // thread agente prestador do serviço

        /* estabelecimento do servico */

        scon = new ServerCom(portNumb);                     // criação do canal de escuta e sua associação
        scon.start ();                                       // com o endereço público
        repos = new GenReposInfo(SimulPar.filename);                           // activação do serviço
        reposInter = new GenReposInfoInterface(repos);        // activação do interface com o serviço
        System.out.println("O serviço foi estabelecido!");
        System.out.println("O servidor esta em escuta.");

        /* processamento de pedidos */

        waitConnection = true;
        while (waitConnection)
            try {
                sconi = scon.accept ();                          // entrada em processo de escuta
                cliProxy = new GenReposInfoProxy(sconi, reposInter);  // lançamento do agente prestador do serviço
                cliProxy.start ();
            } catch (SocketTimeoutException e) {}
        scon.end ();                                         // terminação de operações
        System.out.println("O servidor foi desactivado.");
    }
}