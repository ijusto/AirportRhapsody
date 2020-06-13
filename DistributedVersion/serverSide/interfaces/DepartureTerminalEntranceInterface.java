package serverSide.interfaces;

import clientSide.entities.PassengerStates;
import comInf.CommonProvider;
import comInf.Message;
import comInf.MessageException;
import serverSide.proxies.DepartureTerminalEntranceProxy;
import serverSide.servers.ServerDepartureTerminalEntrance;
import serverSide.sharedRegions.DepartureTerminalEntrance;

/**
 *
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

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

            case Message.PARAMSDEPTENT:break;/* TODO: Validation */
            case Message.PREPARENEXTLEG:break;/* TODO: Validation */
            case Message.RESETDTE:break;
            case Message.NOTFGOHOME:break;
            case Message.NOMOREBAGS:break;
            case Message.SETARRTERREF:break;/* TODO: Validation */

            // Shutdown do servidor (operação pedida pelo cliente)
            case Message.SHUT:
                break;
            default:
                throw new MessageException ("Tipo inválido!", inMessage);
        }

        /* seu processamento */
        CommonProvider cp = (CommonProvider) Thread.currentThread();
        switch (inMessage.getType ()) {
            // probPar
            /*
            case Message.PARAMSDEPTENT:
                departureTerminalEntrance.probPar(inMessage.getMsgReposStub(), inMessage.getMsgArrLoungeStub(),
                        inMessage.getMsgArrQuayStub());
                outMessage = new Message(Message.ACK);
                break;

             */

            // prepareNextLeg
            case Message.PREPARENEXTLEG:
               // ((CommonProvider) Thread.currentThread()).setId(inMessage.getPassId());
                cp.setStatPass(inMessage.getPassId(), PassengerStates.values()[inMessage.getPassStat()]);
                departureTerminalEntrance.prepareNextLeg(inMessage.getPassId());
                outMessage = new Message(Message.ENDPASSENGER);
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