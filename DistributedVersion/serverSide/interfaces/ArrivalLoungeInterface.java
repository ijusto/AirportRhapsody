package serverSide.interfaces;

import comInf.Bag;
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

            // WhatShouldIDo (Passenger)
            case Message.WSID:
                if (arrivalLounge.whatShouldIDo())
                    outMessage = new Message (Message.FNDST);    // gerar resposta positiva
                else
                    outMessage = new Message (Message.TRDST); // gerar resposta negativa
                break;

            // takeARest (Porter)
            case Message.TAKEARST:
                if (arrivalLounge.takeARest() == 'R')
                    outMessage = new Message (Message.TAKERSTDONE);    // gerar resposta positiva
                else
                    outMessage = new Message (Message.ENDPORTER); // gerar resposta negativa
                break;

            // tryToCollectABag (Porter)
            case Message.TRYTOCOL:
                Bag msgBag = arrivalLounge.tryToCollectABag();
                if (msgBag != null)
                    outMessage = new Message (Message.BAG, msgBag.getIntDestStat(), msgBag.getIdOwner());    // gerar resposta positiva
                else
                    outMessage = new Message (Message.NULLBAG); // gerar resposta negativa
                break;
        }

        return (outMessage);
    }
}
