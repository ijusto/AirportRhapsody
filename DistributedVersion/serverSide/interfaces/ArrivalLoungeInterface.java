package serverSide.interfaces;

import comInf.Message;
import comInf.MessageException;
import serverSide.sharedRegions.ArrivalLounge;

public class ArrivalLoungeInterface {

    private ArrivalLounge arrivalLounge;

    public ArrivalLoungeInterface(ArrivalLounge arrivalLounge){
        this.arrivalLounge = arrivalLounge;
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

            // WhatShouldIDo (operação pedida pelo passenger)
            case Message.WSID:
                if (arrivalLounge.whatShouldIDo())
                    outMessage = new Message (Message.FNDST);    // gerar resposta positiva
                else
                    outMessage = new Message (Message.TRDST); // gerar resposta negativa
                break;
        }

        return (outMessage);
    }
}
