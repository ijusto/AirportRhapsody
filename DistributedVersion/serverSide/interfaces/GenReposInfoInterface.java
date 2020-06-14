package serverSide.interfaces;

import clientSide.entities.BusDriverStates;
import clientSide.entities.Passenger;
import clientSide.entities.PassengerStates;
import clientSide.entities.PorterStates;
import comInf.Message;
import comInf.MessageException;
import comInf.SimulPar;
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

            case Message.UPDATEFN:
                if(inMessage.getMsgFlight() > SimulPar.N_FLIGHTS || inMessage.getMsgFlight() < 0)
                    throw new MessageException("Número do voo inválido", inMessage);
                break;

            case Message.INITCHOLD:
                if(inMessage.getMsgBN() > SimulPar.N_PASS_PER_FLIGHT * 2 || inMessage.getMsgBN() < 0)
                    throw new MessageException("Número do malas no porão inválido", inMessage);
                break;

            case Message.UDTEPORTSTAT:
                if(inMessage.getPorterStat() > PorterStates.values().length || inMessage.getPorterStat() < 0)
                    throw new MessageException("Estado do porter inválido", inMessage);
                break;

            case Message.UDTEBDSTAT:
                if(inMessage.getBDStat() > BusDriverStates.values().length || inMessage.getBDStat() < 0)
                    throw new MessageException("Estado do bus driver inválido", inMessage);
                break;

            case Message.PJOINWQ: case Message.PLEFTWQ: case Message.FREEBS: case Message.PASSEXIT:
                if(inMessage.getPassId() < 0 || inMessage.getPassId() > SimulPar.N_PASS_PER_FLIGHT)
                    throw new MessageException("Id do passageiro inválido", inMessage);
                break;

            case Message.NEWPASS:
                if(inMessage.getPassSi() < 0 || inMessage.getPassSi() > Passenger.SiPass.values().length)
                    throw new MessageException("Si do passageiro inválido", inMessage);
                break;

            case Message.UDTEPASSSTAT:
                if(inMessage.getPassId() < 0 || inMessage.getPassId() > SimulPar.N_PASS_PER_FLIGHT)
                    throw new MessageException("Id do passageiro inválido", inMessage);
                if(inMessage.getPassStat() > PassengerStates.values().length || inMessage.getPassStat() < 0)
                    throw new MessageException("Estado do passageiro inválido", inMessage);
                break;

            case Message.GETPASSSI:
                if(inMessage.getPassId() < 0 || inMessage.getPassId() > SimulPar.N_PASS_PER_FLIGHT)
                    throw new MessageException("Id do passageiro inválido", inMessage);
                if(inMessage.getPassSi() < 0 || inMessage.getPassSi() > Passenger.SiPass.values().length)
                    throw new MessageException("Si do passageiro inválido", inMessage);
                break;

            case Message.UDTEPASSNR:
                if(inMessage.getPassId() < 0 || inMessage.getPassId() > SimulPar.N_PASS_PER_FLIGHT)
                    throw new MessageException("Id do passageiro inválido", inMessage);
                if(inMessage.getPassNR() < 0 || inMessage.getPassNR() > 2)
                    throw new MessageException("NR do passageiro inválido", inMessage);
                break;

            case Message.UDTEPASSNA:
                if(inMessage.getPassId() < 0 || inMessage.getPassId() > SimulPar.N_PASS_PER_FLIGHT)
                    throw new MessageException("Id do passageiro inválido", inMessage);
                if(inMessage.getPassNA() < 0 || inMessage.getPassNA() > 2)
                    throw new MessageException("NA do passageiro inválido", inMessage);
                break;

            case Message.NUMNRTOTAL:
                if(inMessage.getPassNR() < 0 || inMessage.getPassNR() > 2)
                    throw new MessageException("NR do passageiro inválido", inMessage);
                break;

            case Message.PRINTLOG: case Message.FINALREPORT: case Message.REMBAGCHOLD: case Message.INCBAGCB:
                case Message.PGETSABAG: case Message.SAVEBAGSR: case Message.MISSBAGREP: case Message.SHUT:
                    break;

            default:
                throw new MessageException ("Tipo inválido!", inMessage);
        }

        /* seu processamento */

        switch (inMessage.getType ()) {

            // printLog
            case Message.PRINTLOG:
                repos.printLog();
                outMessage = new Message(Message.ACK);
                break;

            // finalReport
            case Message.FINALREPORT:
                fr_count++;
                if(fr_count >= 3){
                    repos.finalReport();
                    outMessage = new Message(Message.SHUT);
                } else {
                    outMessage = new Message(Message.ACK);
                }
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
            default:
                break;
        }

        return (outMessage);
    }
}