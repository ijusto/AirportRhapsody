package serverSide.interfaces;

import comInf.Message;
import comInf.MessageException;
import serverSide.DepartureTermTransfQuay;

public class DepartureTermTransfQuayInterface {

    private DepartureTermTransfQuay departureTermTransfQuay;

    public DepartureTermTransfQuayInterface(DepartureTermTransfQuay departureTermTransfQuay){
        this.departureTermTransfQuay = departureTermTransfQuay;
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
        Message outMessage = null;                           // mensagem de resposta

        /* validação da mensagem recebida */

        switch (inMessage.getType ()) {
            // TODO: Change cases
            case Message.SETNFIC:  if ((inMessage.getFName () == null) || (inMessage.getFName ().equals ("")))
                throw new MessageException ("Nome do ficheiro inexistente!", inMessage);
                break;
            default:

        }
