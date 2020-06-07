package serverSide.interfaces;

import comInf.Message;
import comInf.MessageException;
import serverSide.proxies.BaggageReclaimOfficeProxy;
import serverSide.proxies.DepartureTerminalEntranceProxy;
import serverSide.servers.ServerBaggageReclaimOffice;
import serverSide.servers.ServerDepartureTerminalEntrance;
import serverSide.sharedRegions.DepartureTerminalEntrance;

public class DepartureTerminalEntranceInterface {

    private DepartureTerminalEntrance departureTerminalEntrance;

    public DepartureTerminalEntranceInterface(DepartureTerminalEntrance departureTerminalEntrance) {
        this.departureTerminalEntrance = departureTerminalEntrance;
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

            case Message.PARAMSDEPTENT:/* TODO: Validation if necessary */
            case Message.PREPARENEXTLEG:/* TODO: Validation if necessary */
            case Message.RESETDTE:/* TODO: Validation if necessary */
            case Message.NOTFGOHOME:/* TODO: Validation if necessary */
            case Message.NOMOREBAGS:/* TODO: Validation if necessary */
            case Message.SETARRTERREF:/* TODO: Validation if necessary */

            // Shutdown do servidor (operação pedida pelo cliente)
            case Message.SHUT:
                break;
            default:
                throw new MessageException ("Tipo inválido!", inMessage);
        }

        /* seu processamento */

        switch (inMessage.getType ()) {
            // probPar
            case Message.PARAMSDEPTENT:
                departureTerminalEntrance.probPar(inMessage.getMsgReposStub(), inMessage.getMsgArrLoungeStub(),
                        inMessage.getMsgArrQuayStub());
                outMessage = new Message(Message.ACK);
                break;

            // prepareNextLeg
            case Message.PREPARENEXTLEG:
                departureTerminalEntrance.prepareNextLeg(inMessage.getPassId());
                outMessage = new Message(Message.PNLDONE);
                break;

            // resetDepartureTerminalExit
            case Message.RESETDTE:
                departureTerminalEntrance.resetDepartureTerminalExit();
                outMessage = new Message(Message.ACK);
                break;

            // notifyFromGoHome
            case Message.NOTFGOHOME:
                departureTerminalEntrance.notifyFromGoHome();
                outMessage = new Message(Message.ACK);
                break;

            // noMoreBags
            case Message.NOMOREBAGS:
                departureTerminalEntrance.noMoreBags();
                outMessage = new Message(Message.ACK);
                break;

            // setArrivalTerminalRef
            case Message.SETARRTERREF:
                departureTerminalEntrance.setArrivalTerminalRef(inMessage.getMsgArrTermExitStub());
                outMessage = new Message(Message.ACK);
                break;

            case Message.SHUT:                                                        // shutdown do servidor
                ServerDepartureTerminalEntrance.waitConnection = false;
                (((DepartureTerminalEntranceProxy) (Thread.currentThread ())).getScon ()).setTimeout (10);
                outMessage = new Message(Message.ACK);            // gerar confirmação
                break;
        }

        return (outMessage);
    }
}