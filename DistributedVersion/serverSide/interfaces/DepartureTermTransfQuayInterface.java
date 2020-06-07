package serverSide.interfaces;

import comInf.Message;
import comInf.MessageException;
import serverSide.proxies.DepartureTermTransfQuayProxy;
import serverSide.proxies.DepartureTerminalEntranceProxy;
import serverSide.servers.ServerDepartureTermTransfQuay;
import serverSide.servers.ServerDepartureTerminalEntrance;
import serverSide.sharedRegions.DepartureTermTransfQuay;

public class DepartureTermTransfQuayInterface {

    private DepartureTermTransfQuay departureTermTransfQuay;

    public DepartureTermTransfQuayInterface(DepartureTermTransfQuay departureTermTransfQuay) {
        this.departureTermTransfQuay = departureTermTransfQuay;
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

            case Message.PARAMSDEPTTQUAY:/* TODO: Validation */
            case Message.LEAVEBUS:/* TODO: Validation */
            case Message.PBLPO:
            case Message.RESETDTTQ:

            // Shutdown do servidor (operação pedida pelo cliente)
            case Message.SHUT:
                break;
            default:
                throw new MessageException ("Tipo inválido!", inMessage);
        }

        /* seu processamento */

        switch (inMessage.getType ()) {
            // probPar
            case Message.PARAMSDEPTTQUAY:
                departureTermTransfQuay.probPar(inMessage.getMsgReposStub());
                outMessage = new Message(Message.ACK);
                break;

            // leaveTheBus
            case Message.LEAVEBUS:
                departureTermTransfQuay.leaveTheBus(inMessage.getPassId());
                outMessage = new Message(Message.LBDONE);
                break;

            // parkTheBusAndLetPassOff
            case Message.PBLPO:
                departureTermTransfQuay.parkTheBusAndLetPassOff();
                outMessage = new Message(Message.PBLPODONE);
                break;

            // resetDepartureTermTransfQuay
            case Message.RESETDTTQ:
                departureTermTransfQuay.resetDepartureTermTransfQuay();
                outMessage = new Message(Message.ACK);
                break;

            case Message.SHUT:                                                        // shutdown do servidor
                ServerDepartureTermTransfQuay.waitConnection = false;
                (((DepartureTermTransfQuayProxy) (Thread.currentThread ())).getScon ()).setTimeout (10);
                outMessage = new Message(Message.ACK);            // gerar confirmação
                break;
        }

        return (outMessage);
    }
}