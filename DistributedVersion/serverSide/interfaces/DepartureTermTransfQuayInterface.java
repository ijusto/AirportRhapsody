package serverSide.interfaces;

import clientSide.entities.BusDriverStates;
import clientSide.entities.PassengerStates;
import comInf.CommonProvider;
import comInf.Message;
import comInf.MessageException;
import serverSide.proxies.DepartureTermTransfQuayProxy;
import serverSide.servers.ServerDepartureTermTransfQuay;
import serverSide.sharedRegions.DepartureTermTransfQuay;

/**
 *
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

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

            case Message.PARAMSDEPTTQUAY:break;/* TODO: Validation */
            case Message.LEAVEBUS:break;/* TODO: Validation */
            case Message.PBLPO:break;
            case Message.RESETDTTQ:break;

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
            case Message.PARAMSDEPTTQUAY:
                departureTermTransfQuay.probPar(inMessage.getMsgReposStub());
                outMessage = new Message(Message.ACK);
                break;

             */

            // leaveTheBus
            case Message.LEAVEBUS:
                cp.setStatPass(inMessage.getPassId(), PassengerStates.values()[inMessage.getPassStat()]);
                //((CommonProvider) Thread.currentThread()).setId(inMessage.getPassId());
                departureTermTransfQuay.leaveTheBus(inMessage.getPassId());
                outMessage = new Message(Message.LBDONE);
                break;

            // parkTheBusAndLetPassOff
            case Message.PBLPO:
                cp.setBDStat(BusDriverStates.values()[inMessage.getBDStat()]);
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
            default:
                break;
        }

        return (outMessage);
    }
}