package serverSide.servers;

import comInf.SimulPar;
import clientSide.sharedRegionsStubs.GenReposInfoStub;
import comInf.MemException;
import serverSide.ServerCom;
import serverSide.interfaces.TemporaryStorageAreaInterface;
import serverSide.proxies.TemporaryStorageAreaProxy;
import serverSide.sharedRegions.TemporaryStorageArea;

import java.net.SocketTimeoutException;

/**
 *
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class ServerTemporaryStorageArea  {

    /**
     *  Número do port de escuta do serviço a ser prestado (4000, por defeito)
     *
     *    @serialField portNumb
     */

    private static final int portNumb = SimulPar.tmpStorageAreaPort;
    public static boolean waitConnection;                              // sinalização de actividade

    /**
     *  Programa principal.
     */

    public static void main (String [] args) throws MemException {
        TemporaryStorageArea temporaryStorageArea;                                    // barbearia (representa o serviço a ser prestado)
        TemporaryStorageAreaInterface temporaryStorageAreaInter;                      // interface à barbearia
        ServerCom scon, sconi;                               // canais de comunicação
        TemporaryStorageAreaProxy cliProxy;
        GenReposInfoStub reposInfoStub;                     // thread agente prestador do serviço

        /* estabelecimento do servico */

        scon = new ServerCom(portNumb);                     // criação do canal de escuta e sua associação
        scon.start ();
        reposInfoStub = new GenReposInfoStub(SimulPar.genReposInfoHost, SimulPar.genReposInfoPort);// com o endereço público
        temporaryStorageArea = new TemporaryStorageArea(reposInfoStub);                           // activação do serviço
        temporaryStorageAreaInter = new TemporaryStorageAreaInterface(temporaryStorageArea);        // activação do interface com o serviço
        System.out.println("O serviço foi estabelecido!");
        System.out.println("O servidor esta em escuta.");

        /* processamento de pedidos */

        waitConnection = true;
        while (waitConnection)
            try {
                sconi = scon.accept ();                          // entrada em processo de escuta
                cliProxy = new TemporaryStorageAreaProxy(sconi, temporaryStorageAreaInter);  // lançamento do agente prestador do serviço
                cliProxy.start ();
            } catch (SocketTimeoutException e) {}
        scon.end ();                                         // terminação de operações
        System.out.println("O servidor foi desactivado.");
    }
}