package serverSide.interfaces;

import comInf.Message;
import comInf.MessageException;
import serverSide.sharedRegions.BaggageColPoint;

public class BaggageColPointInterface {

    private BaggageColPoint baggageColPoint;

    public BaggageColPointInterface(BaggageColPoint baggageColPoint) {
        this.baggageColPoint = baggageColPoint;
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
            // TODO: Change cases
            case Message.SETNFIC:  if ((inMessage.getFName () == null) || (inMessage.getFName ().equals ("")))
                throw new MessageException ("Nome do ficheiro inexistente!", inMessage);
                break;
            default:
                throw new MessageException ("Tipo inválido!", inMessage);
        }

        /* seu processamento */

        switch (inMessage.getType ()) {
            // TODO: Change cases
            case Message.SETNFIC:                                                     // inicializar ficheiro de logging
                bShop.setFileName (inMessage.getFName (), inMessage.getNIter ());
                outMessage = new Message (Message.NFICDONE);       // gerar resposta
                break;
        }

        return (outMessage);
    }
}