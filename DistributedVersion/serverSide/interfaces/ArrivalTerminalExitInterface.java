package serverSide.interfaces;

import clientSide.entities.PassengerStates;
import comInf.CommonProvider;
import comInf.Message;
import comInf.MessageException;
import comInf.SimulPar;
import serverSide.proxies.ArrivalTerminalExitProxy;
import serverSide.servers.ServerArrivalTerminalExit;
import serverSide.sharedRegions.ArrivalTerminalExit;

/**
 *
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class ArrivalTerminalExitInterface {

    private ArrivalTerminalExit arrivalTerminalExit;

    public ArrivalTerminalExitInterface(ArrivalTerminalExit arrivalTerminalExit){
        this.arrivalTerminalExit = arrivalTerminalExit;
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

    public Message processAndReply (Message inMessage) throws MessageException
    {
        Message outMessage = null;                           // mensagem de resposta

        /* validação da mensagem recebida */

        switch (inMessage.getType ()) {

            case Message.PARAMSATEXIT:
                /* TODO: Validation */
                break;

            case Message.GOHOME:
                if(inMessage.getPassId() < 0 || inMessage.getPassId() > SimulPar.N_PASS_PER_FLIGHT)
                    throw new MessageException("Id do passageiro inválido", inMessage);
                break;

            case Message.SETDEPTERNREF:
                if(inMessage.getMsgDepTermEntStub() == null)
                    throw new MessageException("Departure Terminal Entrance Stub null.", inMessage);
                break;

            case Message.NOTFNEXTL: case Message.RESETATE: case Message.INCDECCOUNTER: case Message.GETDEADPASSVAL:
            case Message.SHUT: break;

            default:
                throw new MessageException ("Tipo inválido!", inMessage);
        }

        /* seu processamento */
        CommonProvider cp = (CommonProvider) Thread.currentThread();
        switch (inMessage.getType ()) {

            // probPar
            /*
            case Message.PARAMSATEXIT:
                arrivalTerminalExit.probPar(inMessage.getMsgReposStub(), inMessage.getMsgArrLoungeStub(),
                            inMessage.getMsgArrQuayStub());
                outMessage = new Message(Message.ACK);
                break;
             */

            // goHome (Passenger)

            case Message.GOHOME:
                cp.setStatPass(inMessage.getPassId(), PassengerStates.values()[inMessage.getPassStat()]);
                arrivalTerminalExit.goHome(inMessage.getPassId());
                outMessage = new Message(Message.ENDPASSENGER);
                break;

            // notifyFromPrepareNextLeg
            case Message.NOTFNEXTL:
                arrivalTerminalExit.notifyFromPrepareNextLeg();
                outMessage = new Message(Message.ACK);
                break;

            // incDecCounter
            case Message.INCDECCOUNTER:
                if (arrivalTerminalExit.incDecCounter(inMessage.getIncOrDec()))
                    outMessage = new Message(Message.CONTCOUNTER);    // gerar resposta positiva
                else
                    outMessage = new Message(Message.LIMITCOUNTER); // gerar resposta negativa
                break;

            // setDepartureTerminalRef (main)
            case Message.SETDEPTERNREF:                                                      
                arrivalTerminalExit.setDepartureTerminalRef(inMessage.getMsgDepTermEntStub());
                outMessage = new Message(Message.ACK);
                break;

            case Message.RESETATE:
                arrivalTerminalExit.resetArrivalTerminalExit();
                outMessage = new Message(Message.ACK);
                break;

            case Message.GETDEADPASSVAL:
                int deadPassValue = arrivalTerminalExit.getDeadPassValue();
                outMessage = new Message(Message.DEADPASSVAL, deadPassValue);
                break;

            case Message.SHUT:                                                        // shutdown do servidor
                ServerArrivalTerminalExit.waitConnection = false;
                (((ArrivalTerminalExitProxy) (Thread.currentThread ())).getScon ()).setTimeout (10);
                outMessage = new Message(Message.ACK);            // gerar confirmação
                break;

            default:
                break;
        }

        return (outMessage);
    }
}