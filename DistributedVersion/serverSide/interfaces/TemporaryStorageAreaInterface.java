package serverSide.interfaces;

import comInf.Bag;
import comInf.MemException;
import comInf.Message;
import comInf.MessageException;
import serverSide.proxies.TemporaryStorageAreaProxy;
import serverSide.servers.ServerTemporaryStorageArea;
import serverSide.sharedRegions.TemporaryStorageArea;

/**
 *
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

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
            case Message.PARAMSTEMPSTORAREA: /* TODO: Validation */
            case Message.CARRYTOAPPSTORE_TSA: /* TODO: Validation */
            case Message.RESETTSA:
            // Shutdown do servidor (operação pedida pelo cliente)
            case Message.SHUT:
                break;
            default:
                throw new MessageException ("Tipo inválido!", inMessage);
        }

        /* seu processamento */

        switch (inMessage.getType ()) {
            // probPar
            case Message.PARAMSTEMPSTORAREA:
                try {
                    temporaryStorageArea.probPar(inMessage.getMsgReposStub());
                } catch (MemException e) {
                    e.printStackTrace();
                }
                outMessage = new Message(Message.ACK);
                break;

            // carryItToAppropriateStore (porter)
            case Message.CARRYTOAPPSTORE_TSA:
                Bag bag = new Bag(inMessage.getMsgBagDestStat(), inMessage.getMsgBagIdOwner());
                temporaryStorageArea.carryItToAppropriateStore(bag);
                outMessage = new Message(Message.ACK);
                break;

            // resetTemporaryStorageArea
            case Message.RESETTSA:
                temporaryStorageArea.resetTemporaryStorageArea();
                outMessage = new Message(Message.ACK);
                break;

            case Message.SHUT:                                                        // shutdown do servidor
                ServerTemporaryStorageArea.waitConnection = false;
                (((TemporaryStorageAreaProxy) (Thread.currentThread ())).getScon ()).setTimeout (10);
                outMessage = new Message(Message.ACK);            // gerar confirmação
                break;
        }

        return (outMessage);
    }
}