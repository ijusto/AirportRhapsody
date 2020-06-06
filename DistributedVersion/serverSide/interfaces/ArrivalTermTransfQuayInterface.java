package serverSide.interfaces;

import comInf.Message;
import comInf.MessageException;
import serverSide.sharedRegions.ArrivalTermTransfQuay;

public class ArrivalTermTransfQuayInterface {

    private ArrivalTermTransfQuay arrivalTermTransfQuay;

    public ArrivalTermTransfQuayInterface(ArrivalTermTransfQuay arrivalTermTransfQuay) {
        this.arrivalTermTransfQuay = arrivalTermTransfQuay;
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

            case Message.TAKEABUS:
                arrivalTermTransfQuay.takeABus(inMessage.getPassId());
                outMessage = new Message(Message.TAKERSTDONE);
                break;
        }

        return (outMessage);
    }
}