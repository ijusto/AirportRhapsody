package serverSide.interfaces;

import comInf.Bag;
import comInf.MemException;
import comInf.Message;
import comInf.MessageException;
import serverSide.proxies.ArrivalLoungeProxy;
import serverSide.servers.ServerArrivalLounge;
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

            // probPar
            case Message.PARAMSARRLNG:
                try {
                    arrivalLounge.probPar(inMessage.getMsgReposStub(), inMessage.getMsgBagColPointStub(),
                            inMessage.getMsgArrQuayStub(), inMessage.getMsgBagAndPassDest(), inMessage.getMsgNBagsPHold());
                } catch (MemException e) {
                    e.printStackTrace();
                }
                outMessage = new Message (Message.ACK);
                break;

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

            // noMoreBagsToCollect (Porter)
            case Message.NOBAGS2COL:                                                      // receber pagamento
                arrivalLounge.noMoreBagsToCollect();
                outMessage = new Message (Message.ACK);            // gerar confirmação
                break;

            // resetArrivalLounge (main)
            case Message.RESETAL:                                                      // receber pagamento
                arrivalLounge.resetArrivalLounge(inMessage.getMsgBagAndPassDest(), inMessage.getMsgNBagsNA());
                outMessage = new Message (Message.RESETALDONE);            // gerar confirmação
                break;

            // setDepartureTerminalRef (main)
            case Message.SETDEPTERNREF:
                arrivalLounge.setDepartureTerminalRef(inMessage.getMsgDepTermEntStub());
                outMessage = new Message (Message.ACK);
                break;

            case Message.SHUT:                                                        // shutdown do servidor
                ServerArrivalLounge.waitConnection = false;
                (((ArrivalLoungeProxy) (Thread.currentThread ())).getScon ()).setTimeout (10);
                outMessage = new Message (Message.ACK);            // gerar confirmação
                break;
        }

        return (outMessage);
    }
}
