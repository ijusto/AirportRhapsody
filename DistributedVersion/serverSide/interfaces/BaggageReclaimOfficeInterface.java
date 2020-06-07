package serverSide.interfaces;

import comInf.Message;
import comInf.MessageException;
import serverSide.proxies.BaggageColPointProxy;
import serverSide.proxies.BaggageReclaimOfficeProxy;
import serverSide.servers.ServerBaggageColPoint;
import serverSide.servers.ServerBaggageReclaimOffice;
import serverSide.sharedRegions.BaggageReclaimOffice;

public class BaggageReclaimOfficeInterface {

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
            // Shutdown do servidor (operação pedida pelo cliente)
            case Message.SHUT:
                break;
            default:
                throw new MessageException ("Tipo inválido!", inMessage);
        }

        /* seu processamento */

        switch (inMessage.getType ()) {
            // probPar
            case Message.PARAMSBAGRECOFF:
                baggageReclaimOffice.probPar(inMessage.getMsgReposStub());
                outMessage = new Message (Message.ACK);
                break;

            // reportMissingBags
            case Message.REPORTMISSBAG:
                baggageReclaimOffice.reportMissingBags(inMessage.getPassId());
                outMessage = new Message (Message.ACK);
                break;

            case Message.SHUT:                                                        // shutdown do servidor
                ServerBaggageReclaimOffice.waitConnection = false;
                (((BaggageReclaimOfficeProxy) (Thread.currentThread ())).getScon ()).setTimeout (10);
                outMessage = new Message (Message.ACK);            // gerar confirmação
                break;
        }

        return (outMessage);
    }
}