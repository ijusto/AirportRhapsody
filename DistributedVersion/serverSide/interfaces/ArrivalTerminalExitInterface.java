package serverSide.interfaces;

import comInf.CommonProvider;
import comInf.Message;
import comInf.MessageException;
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

            case Message.PARAMSATEXIT: break;/* TODO: Validation */
            case Message.GOHOME:break;/* TODO: Validation */
            case Message.NOTFNEXTL:break;
            case Message.INCDECCOUNTER:break;/* TODO: Validation */
            case Message.SETDEPTERNREF:break;/* TODO: Validation */
            case Message.RESETATE:break;
            case Message.GETDEADPASSVAL:break;/* TODO: Validation */

            // Shutdown do servidor (operação pedida pelo cliente)
            case Message.SHUT:
                break;
            default:
                throw new MessageException ("Tipo inválido!", inMessage);
        }

        /* seu processamento */

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
                //((CommonProvider) Thread.currentThread()).setId(inMessage.getPassId());
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
        }

        return (outMessage);
    }
}