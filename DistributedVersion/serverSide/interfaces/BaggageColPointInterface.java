package serverSide.interfaces;

import clientSide.entities.PassengerStates;
import clientSide.entities.PorterStates;
import comInf.*;
import serverSide.proxies.BaggageColPointProxy;
import serverSide.servers.ServerBaggageColPoint;
import serverSide.sharedRegions.BaggageColPoint;

/**
 *
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

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

        switch (inMessage.getType()) {

            case Message.PARAMSBAGCOLPNT: /* TODO: Validation */ break;

            case Message.GOCOLLECTBAG:
                if(inMessage.getPassId() < 0 || inMessage.getPassId() > SimulPar.N_PASS_PER_FLIGHT)
                    throw new MessageException("Id do passageiro inválido", inMessage);
                if(inMessage.getPassStat() > PassengerStates.values().length || inMessage.getPassStat() < 0)
                    throw new MessageException("Estado do passageiro inválido", inMessage);
                break;

            case Message.CARRYAPPSTORE:
                if(inMessage.getMsgBagDestStat() > Bag.DestStat.values().length || inMessage.getMsgBagDestStat() < 0)
                    throw new MessageException("Destino da mala do passageiro inválido", inMessage);
                if(inMessage.getMsgBagIdOwner() > SimulPar.N_PASS_PER_FLIGHT || inMessage.getMsgBagIdOwner() < 0)
                    throw new MessageException("Id do dono da mala inválido", inMessage);
                break;

            case Message.SETTREADMILL:
                if(inMessage.getMsgNBagsPerPass() == null){
                    throw new MessageException("Array de malas para inicializar a treadmill null", inMessage);
                }
                break;

            case Message.RESETBCP: case Message.NOMOREBAGS: case Message.SETPHEMPTY: case Message.SHUT:
                break;

            default:
                //System.out.println(inMessage.getType());
                throw new MessageException ("Tipo inválido!", inMessage);
        }

        /* seu processamento */
        CommonProvider cp = (CommonProvider) Thread.currentThread();
        switch (inMessage.getType ()) {

            // goCollectABag (passenger)
            case Message.GOCOLLECTBAG:
                cp.setStatPass(inMessage.getPassId(), PassengerStates.values()[inMessage.getPassStat()]);
                boolean bagCollected = baggageColPoint.goCollectABag(inMessage.getPassId());
                outMessage = new Message(Message.GCBDONE, bagCollected);
                break;

            // carryItToAppropriateStore (porter)
            case Message.CARRYAPPSTORE:
                cp.setStatPorter(PorterStates.values()[inMessage.getPorterStat()]);
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

            case Message.SETTREADMILL:
                baggageColPoint.setTreadmill(inMessage.getMsgNBagsPerPass());
                outMessage = new Message(Message.ACK);
                break;

            case Message.SHUT:                                                        // shutdown do servidor
                ServerBaggageColPoint.waitConnection = false;
                (((BaggageColPointProxy) (Thread.currentThread ())).getScon ()).setTimeout (10);
                outMessage = new Message(Message.ACK);            // gerar confirmação
                break;

            default:
                break;
        }

        return (outMessage);
    }
}