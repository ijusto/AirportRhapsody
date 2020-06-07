package serverSide.interfaces;

import comInf.Message;
import comInf.MessageException;
import serverSide.proxies.ArrivalTermTransfQuayProxy;
import serverSide.proxies.ArrivalTerminalExitProxy;
import serverSide.servers.ServerArrivalTermTransfQuay;
import serverSide.servers.ServerArrivalTerminalExit;
import serverSide.sharedRegions.ArrivalTermTransfQuay;

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
            // Shutdown do servidor (operação pedida pelo cliente)
            case Message.SHUT:
                break;
            default:
                throw new MessageException ("Tipo inválido!", inMessage);
        }

        /* seu processamento */

        switch (inMessage.getType ()) {

            // takeABus (Passenger)
            case Message.TAKEABUS:
                arrivalTermTransfQuay.takeABus(inMessage.getPassId());
                outMessage = new Message(Message.TAKERSTDONE);
                break;

            // enterTheBus (Passenger)
            case Message.ENTERBUS:
                arrivalTermTransfQuay.enterTheBus(inMessage.getPassId());
                outMessage = new Message(Message.ACK);
                break;

            // hasDaysWorkEnded (BusDriver)
            case Message.WORKENDED:
                if (arrivalTermTransfQuay.hasDaysWorkEnded() == 'R')
                    outMessage = new Message (Message.CONTDAYS);    // gerar resposta positiva
                else
                    outMessage = new Message (Message.ENDBUSDRIVER); // gerar resposta negativa
                break;

            // parkTheBus (BusDriver)
            case Message.PARKBUS:
                arrivalTermTransfQuay.parkTheBus();
                outMessage = new Message(Message.PBDONE);
                break;

            // announcingBusBoarding (BusDriver)
            case Message.ANNOUCEBUSBORADING:
                arrivalTermTransfQuay.announcingBusBoarding();
                outMessage = new Message(Message.ABBDONE);
                break;

            case Message.SHUT:                                                        // shutdown do servidor
                ServerArrivalTermTransfQuay.waitConnection = false;
                (((ArrivalTermTransfQuayProxy) (Thread.currentThread ())).getScon ()).setTimeout (10);
                outMessage = new Message (Message.ACK);            // gerar confirmação
                break;
        }

        return (outMessage);
    }
}