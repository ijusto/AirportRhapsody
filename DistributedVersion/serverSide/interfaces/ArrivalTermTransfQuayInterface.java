package serverSide.interfaces;

import clientSide.entities.BusDriver;
import clientSide.entities.BusDriverStates;
import clientSide.entities.PassengerStates;
import clientSide.entities.PorterStates;
import comInf.*;
import serverSide.proxies.ArrivalTermTransfQuayProxy;
import serverSide.servers.ServerArrivalTermTransfQuay;
import serverSide.sharedRegions.ArrivalTermTransfQuay;

/**
 *
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class ArrivalTermTransfQuayInterface {

    private ArrivalTermTransfQuay arrivalTermTransfQuay;

    public ArrivalTermTransfQuayInterface(ArrivalTermTransfQuay arrivalTermTransfQuay) {
        this.arrivalTermTransfQuay = arrivalTermTransfQuay;
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

            case Message.PARAMSATTQUAY: /* TODO: Validation */ break;

            case Message.TAKEABUS: case Message.ENTERBUS:
                if(inMessage.getPassId() < 0 || inMessage.getPassId() > SimulPar.N_PASS_PER_FLIGHT)
                    throw new MessageException("Id do passageiro inválido", inMessage);
                if(inMessage.getPassStat() > PassengerStates.values().length || inMessage.getPassStat() < 0)
                    throw new MessageException("Estado do passageiro inválido", inMessage);
                break;

            case Message.WORKENDED: case Message.PARKBUS: case Message.ANNOUCEBUSBORADING:
                if(inMessage.getBDStat() > BusDriverStates.values().length || inMessage.getBDStat() < 0)
                    throw new MessageException("Estado do bus driver inválido", inMessage);
                break;

            case Message.RESETATQ: case Message.SETENDDAY: case Message.SHUT:
                break;

            default:
                throw new MessageException ("Tipo inválido!", inMessage);
        }

        /* seu processamento */
        CommonProvider cp = (CommonProvider) Thread.currentThread();
        switch (inMessage.getType ()) {

            // probPar
//            case Message.PARAMSATTQUAY:
//                try {
//                    arrivalTermTransfQuay.probPar(inMessage.getMsgReposStub());
//                } catch (MemException e) {
//                    e.printStackTrace();
//                }
//                outMessage = new Message(Message.ACK);
//                break;

            // takeABus (Passenger)
            case Message.TAKEABUS:
                cp.setStatPass(inMessage.getPassId(), PassengerStates.values()[inMessage.getPassStat()]);
                arrivalTermTransfQuay.takeABus(inMessage.getPassId());
                outMessage = new Message(Message.TAKEABUSDONE);
                break;

            // enterTheBus (Passenger)
            case Message.ENTERBUS:
                cp.setStatPass(inMessage.getPassId(), PassengerStates.values()[inMessage.getPassStat()]);
                arrivalTermTransfQuay.enterTheBus(inMessage.getPassId());
                outMessage = new Message(Message.ACK);
                break;

            // hasDaysWorkEnded (BusDriver)
            case Message.WORKENDED:
                cp.setBDStat(BusDriverStates.values()[inMessage.getBDStat()]);
                if (arrivalTermTransfQuay.hasDaysWorkEnded() == 'R')
                    outMessage = new Message(Message.CONTDAYS);    // gerar resposta positiva
                else
                    outMessage = new Message(Message.ENDBUSDRIVER); // gerar resposta negativa
                break;

            // parkTheBus (BusDriver)
            case Message.PARKBUS:
                cp.setBDStat(BusDriverStates.values()[inMessage.getBDStat()]);
                arrivalTermTransfQuay.parkTheBus();
                outMessage = new Message(Message.PBDONE);
                break;

            // announcingBusBoarding (BusDriver)
            case Message.ANNOUCEBUSBORADING:
                cp.setBDStat(BusDriverStates.values()[inMessage.getBDStat()]);
                arrivalTermTransfQuay.announcingBusBoarding();
                outMessage = new Message(Message.ABBDONE);
                break;

            // resetArrivalTermTransfQuay
            case Message.RESETATQ:
                try {
                    arrivalTermTransfQuay.resetArrivalTermTransfQuay();
                } catch (MemException e) {
                    e.printStackTrace();
                }
                outMessage = new Message(Message.ACK);
                break;

            // setEndDay
            case Message.SETENDDAY:
                arrivalTermTransfQuay.setEndDay();
                outMessage = new Message(Message.ACK);
                break;

            case Message.SHUT:                                                        // shutdown do servidor
                ServerArrivalTermTransfQuay.waitConnection = false;
                (((ArrivalTermTransfQuayProxy) (Thread.currentThread())).getScon()).setTimeout(10);
                outMessage = new Message(Message.ACK);            // gerar confirmação
                break;
            default:
                break;
        }

        return (outMessage);
    }
}