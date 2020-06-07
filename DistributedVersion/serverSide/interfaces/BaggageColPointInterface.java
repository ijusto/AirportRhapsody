package serverSide.interfaces;

import comInf.Bag;
import comInf.MemException;
import comInf.Message;
import comInf.MessageException;
import serverSide.proxies.ArrivalTermTransfQuayProxy;
import serverSide.proxies.BaggageColPointProxy;
import serverSide.servers.ServerArrivalTermTransfQuay;
import serverSide.servers.ServerBaggageColPoint;
import serverSide.sharedRegions.BaggageColPoint;

public class BaggageColPointInterface {

    private BaggageColPoint baggageColPoint;

    public BaggageColPointInterface(BaggageColPoint baggageColPoint) {
        this.baggageColPoint = baggageColPoint;
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

            case Message.PARAMSBAGCOLPNT:/* TODO: Validation */
            case Message.GOCOLLECTBAG:/* TODO: Validation */
            case Message.CARRYAPPSTORE:/* TODO: Validation */
            case Message.RESETBCP:
            case Message.NOMOREBAGS:
            case Message.SETPHEMPTY:/* TODO: Validation */

            // Shutdown do servidor (operação pedida pelo cliente)
            case Message.SHUT:
                break;
            default:
                throw new MessageException ("Tipo inválido!", inMessage);
        }

        /* seu processamento */

        switch (inMessage.getType ()) {
            // probPar
            case Message.PARAMSBAGCOLPNT:
                baggageColPoint.probPar(inMessage.getMsgReposStub());
                outMessage = new Message(Message.ACK);
                break;

            // goCollectABag (passenger)
            case Message.GOCOLLECTBAG:
                boolean bagCollected = baggageColPoint.goCollectABag(inMessage.getPassId());
                outMessage = new Message(Message.GCBDONE, bagCollected);
                break;

            // carryItToAppropriateStore (porter)
            case Message.CARRYAPPSTORE:
                Bag bag = new Bag(inMessage.getMsgBagDestStat(), inMessage.getMsgBagIdOwner());
                baggageColPoint.carryItToAppropriateStore(bag);
                outMessage = new Message(Message.ACK);
                break;

            // resetBaggageColPoint
            case Message.RESETBCP:
                baggageColPoint.resetBaggageColPoint();
                outMessage = new Message(Message.ACK);
                break;

            // noMoreBags
            case Message.NOMOREBAGS:
                baggageColPoint.noMoreBags();
                outMessage = new Message(Message.ACK);
                break;

            // setPHoldEmpty
            case Message.SETPHEMPTY:
                baggageColPoint.setPHoldEmpty(inMessage.msgPlaneHoldEmpty());
                outMessage = new Message(Message.ACK);
                break;

            case Message.SHUT:                                                        // shutdown do servidor
                ServerBaggageColPoint.waitConnection = false;
                (((BaggageColPointProxy) (Thread.currentThread ())).getScon ()).setTimeout (10);
                outMessage = new Message(Message.ACK);            // gerar confirmação
                break;
        }

        return (outMessage);
    }
}