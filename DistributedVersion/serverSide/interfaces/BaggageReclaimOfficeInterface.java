package serverSide.interfaces;

import clientSide.entities.PassengerStates;
import comInf.CommonProvider;
import comInf.Message;
import comInf.MessageException;
import comInf.SimulPar;
import serverSide.proxies.BaggageReclaimOfficeProxy;
import serverSide.servers.ServerBaggageReclaimOffice;
import serverSide.sharedRegions.BaggageReclaimOffice;

/**
 *
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class BaggageReclaimOfficeInterface{

    private BaggageReclaimOffice baggageReclaimOffice;

    public BaggageReclaimOfficeInterface(BaggageReclaimOffice baggageReclaimOffice) {
        this.baggageReclaimOffice = baggageReclaimOffice;
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

            case Message.PARAMSBAGRECOFF:break;/* TODO: Validation */

            case Message.REPORTMISSBAG:
                if(inMessage.getPassId() < 0 || inMessage.getPassId() > SimulPar.N_PASS_PER_FLIGHT)
                    throw new MessageException("Id do passageiro inválido", inMessage);
                if(inMessage.getPassStat() > PassengerStates.values().length || inMessage.getPassStat() < 0)
                    throw new MessageException("Estado do passageiro inválido", inMessage);
                break;

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
            case Message.PARAMSBAGRECOFF:
                baggageReclaimOffice.probPar(inMessage.getMsgReposStub());
                outMessage = new Message(Message.ACK);
                break;

             */

            // reportMissingBags
            case Message.REPORTMISSBAG:
                cp.setStatPass(inMessage.getPassId(), PassengerStates.values()[inMessage.getPassStat()]);
                baggageReclaimOffice.reportMissingBags(inMessage.getPassId());
                outMessage = new Message(Message.ACK);
                break;

            case Message.SHUT:                                                        // shutdown do servidor
                ServerBaggageReclaimOffice.waitConnection = false;
                (((BaggageReclaimOfficeProxy) (Thread.currentThread ())).getScon ()).setTimeout (10);
                outMessage = new Message(Message.ACK);            // gerar confirmação
                break;
            default:
                break;
        }

        return (outMessage);
    }
}