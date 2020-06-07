package serverSide.interfaces;

import comInf.Message;
import comInf.MessageException;
import serverSide.proxies.GenReposInfoProxy;
import serverSide.servers.ServerGenReposInfo;
import serverSide.sharedRegions.GenReposInfo;

public class GenReposInfoInterface {

    private GenReposInfo repos;

    public GenReposInfoInterface(GenReposInfo repos){
        this.repos = repos;
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

            case Message.PARAMSREPOS: /* TODO: Validation if necessary */
            case Message.PRINTLOG: /* TODO: Validation if necessary */
            case Message.FINALREPORT: /* TODO: Validation if necessary */
            case Message.UPDATEFN: /* TODO: Validation if necessary */
            case Message.INITCHOLD: /* TODO: Validation if necessary */
            case Message.REMBAGCHOLD: /* TODO: Validation if necessary */
            case Message.INCBAGCB: /* TODO: Validation if necessary */
            case Message.PGETSABAG: /* TODO: Validation if necessary */
            case Message.SAVEBAGSR: /* TODO: Validation if necessary */
            case Message.PJOINWQ: /* TODO: Validation if necessary */
            case Message.PLEFTWQ: /* TODO: Validation if necessary */
            case Message.FREEBS: /* TODO: Validation if necessary */
            case Message.GETPASSSI: /* TODO: Validation if necessary */
            case Message.UDTEPASSNR: /* TODO: Validation if necessary */
            case Message.UDTEPASSNA: /* TODO: Validation if necessary */
            case Message.PASSEXIT: /* TODO: Validation if necessary */
            case Message.MISSBAGREP: /* TODO: Validation if necessary */
            case Message.NUMNRTOTAL: /* TODO: Validation if necessary */

            // Shutdown do servidor (operação pedida pelo cliente)
            case Message.SHUT:
                break;
            default:
                throw new MessageException ("Tipo inválido!", inMessage);
        }

        /* seu processamento */

        switch (inMessage.getType ()) {
            // probPar
            case Message.PARAMSREPOS:
                repos.probPar(inMessage.getMsgReposFile());
                outMessage = new Message(Message.ACK);
                break;

            // printLog
            case Message.PRINTLOG:
                repos.printLog();
                outMessage = new Message(Message.ACK);
                break;

            // finalReport
            case Message.FINALREPORT:
                repos.finalReport();
                outMessage = new Message(Message.ACK);
                break;

            // updateFlightNumber
            case Message.UPDATEFN:
                repos.updateFlightNumber(inMessage.getMsgFlight());
                outMessage = new Message(Message.ACK);
                break;

            // initializeCargoHold
            case Message.INITCHOLD:
                repos.initializeCargoHold(inMessage.getMsgBN());
                outMessage = new Message(Message.ACK);
                break;

            // removeBagFromCargoHold
            case Message.REMBAGCHOLD:
                repos.removeBagFromCargoHold();
                outMessage = new Message(Message.ACK);
                break;

            // incBaggageCB
            case Message.INCBAGCB:
                repos.incBaggageCB();
                outMessage = new Message(Message.ACK);
                break;

            // pGetsABag
            case Message.PGETSABAG:
                repos.pGetsABag();
                outMessage = new Message(Message.ACK);
                break;

            // saveBagInSR
            case Message.SAVEBAGSR:
                repos.saveBagInSR();
                outMessage = new Message(Message.ACK);
                break;

            // pJoinWaitingQueue
            case Message.PJOINWQ:
                repos.pJoinWaitingQueue(inMessage.getPassId());
                outMessage = new Message(Message.ACK);
                break;

            // pLeftWaitingQueue
            case Message.PLEFTWQ:
                repos.pLeftWaitingQueue(inMessage.getPassId());
                outMessage = new Message(Message.ACK);
                break;

            // freeBusSeat
            case Message.FREEBS:
                repos.freeBusSeat(inMessage.getPassId());
                outMessage = new Message(Message.ACK);
                break;

            // getPassSi
            case Message.GETPASSSI:
                repos.getPassSi(inMessage.getPassId(), inMessage.getPassSi());
                outMessage = new Message(Message.ACK);
                break;

            // updatesPassNR
            case Message.UDTEPASSNR:
                repos.updatesPassNR(inMessage.getPassId(), inMessage.getPassNR());
                outMessage = new Message(Message.ACK);
                break;

            // updatesPassNA
            case Message.UDTEPASSNA:
                repos.updatesPassNA(inMessage.getPassId(), inMessage.getPassNA());
                outMessage = new Message(Message.ACK);
                break;

            // updatesPassNA
            case Message.PASSEXIT:
                repos.passengerExit(inMessage.getPassId());
                outMessage = new Message(Message.ACK);
                break;

            // missingBagReported
            case Message.MISSBAGREP:
                repos.missingBagReported();
                outMessage = new Message(Message.ACK);
                break;

            // numberNRTotal
            case Message.NUMNRTOTAL:
                repos.numberNRTotal(inMessage.getMsgNR());
                outMessage = new Message(Message.ACK);
                break;

            case Message.SHUT:                                                        // shutdown do servidor
                ServerGenReposInfo.waitConnection = false;
                (((GenReposInfoProxy) (Thread.currentThread ())).getScon ()).setTimeout (10);
                outMessage = new Message(Message.ACK);            // gerar confirmação
                break;
        }

        return (outMessage);
    }
}