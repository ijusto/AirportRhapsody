package serverSide.interfaces;

import comInf.Message;
import comInf.MessageException;
import serverSide.sharedRegions.ArrivalLounge;

public class ArrivalLoungeInterface {

    private ArrivalLounge arrivalLounge;

    public ArrivalLoungeInterface(ArrivalLounge arrivalLounge){
        this.arrivalLounge = arrivalLounge;
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
        // mensagem de resposta
        Message outMessage = null;

        /* validação da mensagem recebida */

        switch (inMessage.getType ()) {
            // Inicialização do ficheiro de logging (operação pedida pelo cliente)
            case Message.SETNFIC:
                if ((inMessage.getFName () == null) || (inMessage.getFName ().equals ("")))
                    throw new MessageException ("Nome do ficheiro inexistente!", inMessage);
                break;
            // Corte de cabelo (operação pedida pelo cliente)
            case Message.REQCUTH:
                if ((inMessage.getCustId () < 0) || (inMessage.getCustId () >= bShop.getNCust ()))
                    throw new MessageException ("Id do cliente inválido!", inMessage);
                break;
            // Alertar o thread barbeiro do fim de operações (operação pedida pelo cliente)
            case Message.ENDOP:
            // Mandar o barbeiro dormir (operação pedida pelo cliente)
            case Message.GOTOSLP:
            // Chamar um cliente pelo barbeiro (operação pedida pelo cliente)
            case Message.CALLCUST:
                if ((inMessage.getBarbId () < 0) || (inMessage.getBarbId () >= bShop.getNBarb ()))
                    throw new MessageException ("Id do barbeiro inválido!", inMessage);
                break;
            // Receber pagamento pelo barbeiro (operação pedida pelo cliente)
            case Message.GETPAY:
                if ((inMessage.getBarbId () < 0) || (inMessage.getBarbId () >= bShop.getNBarb ()))
                    throw new MessageException ("Id do barbeiro inválido!", inMessage);
                if ((inMessage.getCustId () < 0) || (inMessage.getCustId () >= bShop.getNCust ()))
                    throw new MessageException ("Id do cliente inválido!", inMessage);
                break;
            // Shutdown do servidor (operação pedida pelo cliente)
            case Message.SHUT:
                break;
            default:
                throw new MessageException ("Tipo inválido!", inMessage);
        }

        /* seu processamento */

        switch (inMessage.getType ()) {
            // Inicialização do ficheiro de logging (operação pedida pelo cliente)
            case Message.SETNFIC:
                bShop.setFileName (inMessage.getFName (), inMessage.getNIter ());
                outMessage = new Message (Message.NFICDONE);       // gerar resposta
                break;
            // Corte de cabelo (operação pedida pelo cliente)
            case Message.REQCUTH:
                if (bShop.goCutHair (inMessage.getCustId ()))      // o cliente quer cortar o cabelo
                    outMessage = new Message (Message.CUTHDONE);    // gerar resposta positiva
                else
                    outMessage = new Message (Message.BSHOPF); // gerar resposta negativa
                break;
            // Mandar o barbeiro dormir (operação pedida pelo cliente)
            case Message.GOTOSLP:
                if (bShop.goToSleep (inMessage.getBarbId ()))      // o barbeiro vai dormir
                    outMessage = new Message (Message.END);         // gerar resposta positiva
                else
                    outMessage = new Message (Message.CONT);   // gerar resposta negativa
                break;
            // Chamar um cliente pelo barbeiro (operação pedida pelo cliente)
            case Message.CALLCUST:
                int custID = bShop.callCustomer (inMessage.getBarbId ());  // chamar cliente
                outMessage = new Message (Message.CUSTID, custID); // enviar id do cliente
                break;
            // Receber pagamento pelo barbeiro (operação pedida pelo cliente)
            case Message.GETPAY:
                bShop.getPayment (inMessage.getBarbId (), inMessage.getCustId ());
                outMessage = new Message (Message.ACK);            // gerar confirmação
                break;
            // Alertar o thread barbeiro do fim de operações (operação pedida pelo cliente)
            case Message.ENDOP:
                bShop.endOperation (inMessage.getBarbId ());
                outMessage = new Message (Message.ACK);            // gerar confirmação
                break;
            // Shutdown do servidor (operação pedida pelo cliente)
            case Message.SHUT:
                ServerSleepingBarbers.waitConnection = false;
                (((ClientProxy) (Thread.currentThread ())).getScon ()).setTimeout (10);
                outMessage = new Message (Message.ACK);            // gerar confirmação
                break;
        }

        return (outMessage);
    }
}
