package serverSide.interfaces;
import clientSide.entities.Passenger;
import clientSide.entities.PassengerStates;
import clientSide.entities.PorterStates;
import comInf.*;
import serverSide.proxies.ArrivalLoungeProxy;
import serverSide.servers.ServerArrivalLounge;
import serverSide.sharedRegions.ArrivalLounge;

/**
 *
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class ArrivalLoungeInterface {

    /*
     *
     */

    private ArrivalLounge arrivalLounge;


    /*
     *
     */

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

    /*
     *
     */

    public Message processAndReply (Message inMessage) throws MessageException {

        // mensagem de resposta
        Message outMessage = null;

        /* validação da mensagem recebida */

        switch (inMessage.getType ()) {

            case Message.PARAMSARRLNG:
                if(inMessage.getMsgBagAndPassDest() == null || inMessage.getMsgNBagsNA() == null)
                    throw new MessageException("Arrival Lounge has invalid parameters.", inMessage);
                break;

            case Message.WSID:
                if(inMessage.getPassId() < 0 || inMessage.getPassId() > SimulPar.N_PASS_PER_FLIGHT)
                    throw new MessageException("Id do passageiro inválido", inMessage);
                if(inMessage.getPassNR() < 0 || inMessage.getPassNR() > 2)
                    throw new MessageException("NR do passageiro inválido", inMessage);
                if(inMessage.getPassSi() < 0 || inMessage.getPassSi() >= Passenger.SiPass.values().length) {
                    System.out.println("Si passageiro: " + inMessage.getPassSi());
                    throw new MessageException("Si do passageiro inválido", inMessage);
                }
                if(inMessage.getPassStat() > PassengerStates.values().length || inMessage.getPassStat() < 0)
                    throw new MessageException("Estado do passageiro inválido", inMessage);
                break;

            case Message.TAKEARST: case Message.TRYTOCOL: case Message.NOBAGS2COL:
                if(inMessage.getPorterStat() > PorterStates.values().length || inMessage.getPorterStat() < 0)
                    throw new MessageException("Estado do porter inválido", inMessage);
                break;

            case Message.RESETAL:
                /** TODO: Validation **/
                break;

            case Message.SETDEPTERNREF:
                if(inMessage.getMsgDepTermEntStub() == null)
                    throw new MessageException("Departure Terminal Entrance Stub null.", inMessage);
                break;

            case Message.SHUT:
                break;

            default:
                throw new MessageException ("Tipo inválido!", inMessage);
        }

        /* seu processamento */
        ArrivalLoungeProxy cp = (ArrivalLoungeProxy) Thread.currentThread();
        System.out.println("CommonProvider: " + cp.toString());
        System.out.println("Current thread name: " + Thread.currentThread().getName());
        switch(inMessage.getType()) {

            // probPar
            case Message.PARAMSARRLNG:
                try {
                    arrivalLounge.probPar(inMessage.getMsgBagAndPassDest(), inMessage.getMsgNBagsNA());
                } catch (MemException e) {
                    e.printStackTrace();
                }
                outMessage = new Message(Message.ACK);
                break;

            // WhatShouldIDo (Passenger)
            case Message.WSID:
                ((CommonProvider) Thread.currentThread()).setStatPass(inMessage.getPassId(), PassengerStates.values()[inMessage.getPassStat()]);
                ((CommonProvider) Thread.currentThread()).setNR(inMessage.getPassId(), inMessage.getPassNR());
                ((CommonProvider) Thread.currentThread()).setSi(inMessage.getPassId(), Passenger.SiPass.values()[inMessage.getPassSi()]);
                if (arrivalLounge.whatShouldIDo(inMessage.getPassId()))
                    outMessage = new Message(Message.FNDST);    // gerar resposta positiva
                else
                    outMessage = new Message(Message.TRDST); // gerar resposta negativa
                break;

            // takeARest (Porter)
            case Message.TAKEARST:
                cp.setStatPorter(PorterStates.values()[inMessage.getPorterStat()]);
                if (arrivalLounge.takeARest() == 'R')
                    outMessage = new Message(Message.TAKERSTDONE);    // gerar resposta positiva
                else
                    outMessage = new Message(Message.ENDPORTER); // gerar resposta negativa
                break;

            // tryToCollectABag (Porter)
            case Message.TRYTOCOL:
                Bag msgBag = arrivalLounge.tryToCollectABag();
                cp.setStatPorter(PorterStates.values()[inMessage.getPorterStat()]);
                if (msgBag != null)
                    outMessage = new Message(Message.BAG, msgBag.getIntDestStat(), msgBag.getIdOwner());    // gerar resposta positiva
                else
                    outMessage = new Message(Message.NULLBAG); // gerar resposta negativa
                break;

            // noMoreBagsToCollect (Porter)
            case Message.NOBAGS2COL:
                cp.setStatPorter(PorterStates.values()[inMessage.getPorterStat()]);
                arrivalLounge.noMoreBagsToCollect();
                outMessage = new Message(Message.ACK);            // gerar confirmação
                break;

            // resetArrivalLounge (main)
            case Message.RESETAL:
                try {
                    arrivalLounge.resetArrivalLounge(inMessage.getMsgBagAndPassDest(), inMessage.getMsgNBagsNA());
                } catch (MemException e) {
                    e.printStackTrace();
                }
                outMessage = new Message(Message.RESETALDONE);            // gerar confirmação
                break;

            // setDepartureTerminalRef (main)
            case Message.SETDEPTERNREF:
                arrivalLounge.setDepartureTerminalRef(inMessage.getMsgDepTermEntStub());
                outMessage = new Message(Message.ACK);
                break;

            case Message.SHUT:                                                        // shutdown do servidor
                ServerArrivalLounge.waitConnection = false;
                (((ArrivalLoungeProxy) (Thread.currentThread())).getScon()).setTimeout(10);
                outMessage = new Message(Message.ACK);            // gerar confirmação
                break;

            default:
                break;
        }

        return (outMessage);
    }
}
