package serverSide.interfaces;

import comInf.Bag;
import comInf.Message;
import comInf.MessageException;
import serverSide.proxies.ArrivalLoungeProxy;
import serverSide.proxies.GenReposInfoProxy;
import serverSide.servers.ServerArrivalLounge;
import serverSide.servers.ServerGenReposInfo;
import serverSide.sharedRegions.ArrivalLounge;
import serverSide.sharedRegions.GenReposInfo;

public class GenReposInfoInterface {

    private GenReposInfo repos;

    public GenReposInfoInterface(GenReposInfo repos){
        this.repos = repos;
    }

    /**
     *  Processamento das mensagens através da execução da tarefa correspondente.
     *  Geração de uma mensagem de resposta.
     *
     *    @param inMessage mensagem com o pedido
     *
     *    @return mensagem de resposta
     *
     *    @throws MessageException se a mensagem com o pedido for considerada inválida
     */

    public Message processAndReply (Message inMessage) throws MessageException
    {
        // mensagem de resposta
        Message outMessage = null;

        /* validação da mensagem recebida */

        switch (inMessage.getType ()) {

            // Shutdown do servidor (operação pedida pelo cliente)
            case Message.SHUT:
                break;
            default:
                throw new MessageException ("Tipo inválido!", inMessage);
        }

        /* seu processamento */

        switch (inMessage.getType ()) {
            // probPar
            case Message.PARAMSREPOS:
                repos.probPar(inMessage.getMsgReposFile());
                outMessage = new Message(Message.ACK);
                break;

            case Message.SHUT:                                                        // shutdown do servidor
                ServerGenReposInfo.waitConnection = false;
                (((GenReposInfoProxy) (Thread.currentThread ())).getScon ()).setTimeout (10);
                outMessage = new Message(Message.ACK);            // gerar confirmação
                break;
        }

        return (outMessage);
    }
}