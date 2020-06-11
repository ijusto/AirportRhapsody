package serverSide.interfaces;

import comInf.Message;
import comInf.MessageException;
import serverSide.proxies.GenReposInfoProxy;
import serverSide.servers.ServerGenReposInfo;
import serverSide.sharedRegions.GenReposInfo;

/**
 *
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
 */

public class GenReposInfoInterface {

    private GenReposInfo repos;
    private int fr_count;

    public GenReposInfoInterface(GenReposInfo repos){
        this.repos = repos;
        this.fr_count = 0;
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

            case Message.PARAMSREPOS:break; /* TODO: Validation */
            case Message.PRINTLOG:break;
            case Message.FINALREPORT:break;
            case Message.UPDATEFN: break;/* TODO: Validation */
            case Message.INITCHOLD:break; /* TODO: Validation */
            case Message.REMBAGCHOLD:break;
            case Message.INCBAGCB:break;
            case Message.PGETSABAG:break;
            case Message.SAVEBAGSR:break;
            case Message.PJOINWQ: break;/* TODO: Validation */
            case Message.PLEFTWQ:break; /* TODO: Validation */
            case Message.FREEBS: break;/* TODO: Validation */
            case Message.GETPASSSI:break; /* TODO: Validation */
            case Message.UDTEPASSNR: break;/* TODO: Validation */
            case Message.UDTEPASSNA: break;/* TODO: Validation */
            case Message.PASSEXIT: break;/* TODO: Validation */
            case Message.MISSBAGREP:break;
            case Message.NUMNRTOTAL: break;/* TODO: Validation */
            case Message.NEWPASS: break;/* TODO: Validation */
            case Message.UDTEPASSSTAT: break;/* TODO: Validation */
            case Message.UDTEPORTSTAT: break;/* TODO: Validation */
            case Message.UDTEBDSTAT: break;/* TODO: Validation */

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
            case Message.PARAMSREPOS:
                repos.probPar(inMessage.getMsgReposFile());
                outMessage = new Message(Message.ACK);
                break;
             */

            // printLog
            case Message.PRINTLOG:
                repos.printLog();
                outMessage = new Message(Message.ACK);
                break;

            // finalReport
            case Message.FINALREPORT:
                fr_count++;
                if(fr_count == 3){
                    repos.finalReport();
                }
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

            case Message.NEWPASS:
                repos.newPass(inMessage.getPassSi());
                outMessage = new Message(Message.ACK);
                break;

            case Message.UDTEPASSSTAT:
                repos.updatePassSt(inMessage.getPassId(), inMessage.getPassStat());
                outMessage = new Message(Message.ACK);
                break;

            case Message.UDTEPORTSTAT:
                repos.updatePorterStat(inMessage.getPorterStat());
                outMessage = new Message(Message.ACK);
                break;

            case Message.UDTEBDSTAT:
                repos.updateBDriverStat(inMessage.getBDStat());
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