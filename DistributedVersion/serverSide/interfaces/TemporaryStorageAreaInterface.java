package serverSide.interfaces;

import comInf.Message;
import comInf.MessageException;
import serverSide.proxies.DepartureTermTransfQuayProxy;
import serverSide.proxies.TemporaryStorageAreaProxy;
import serverSide.servers.ServerDepartureTermTransfQuay;
import serverSide.servers.ServerTemporaryStorageArea;
import serverSide.sharedRegions.TemporaryStorageArea;

public class TemporaryStorageAreaInterface {

    private TemporaryStorageArea temporaryStorageArea;

    public TemporaryStorageAreaInterface(TemporaryStorageArea temporaryStorageArea) {
        this.temporaryStorageArea = temporaryStorageArea;
    }

    /**
     * Processamento das mensagens através da execução da tarefa correspondente.
     * Geração de uma mensagem de resposta.
     *
     * @param inMessage mensagem com o pedido
     * @return mensagem de resposta
     * @throws MessageException se a mensagem com o pedido for considerada inválida
     */

    public Message processAndReply (Message inMessage) throws MessageException
    {
        Message outMessage = null;                           // mensagem de resposta

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
            case Message.SHUT:                                                        // shutdown do servidor
                ServerTemporaryStorageArea.waitConnection = false;
                (((TemporaryStorageAreaProxy) (Thread.currentThread ())).getScon ()).setTimeout (10);
                outMessage = new Message (Message.ACK);            // gerar confirmação
                break;
        }

        return (outMessage);
    }
}