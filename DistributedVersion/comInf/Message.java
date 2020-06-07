package comInf;

import clientSide.sharedRegionsStubs.DepartureTerminalEntranceStub;
import serverSide.sharedRegions.DepartureTerminalEntrance;

import java.io.*;

/**
 *   Este tipo de dados define as mensagens que são trocadas entre os clientes e o servidor numa solução do Problema
 *   dos Barbeiros Sonolentos que implementa o modelo cliente-servidor de tipo 2 (replicação do servidor) com lançamento
 *   estático dos threads barbeiro.
 *   A comunicação propriamente dita baseia-se na troca de objectos de tipo Message num canal TCP.
 */

public class Message implements Serializable
{
    /**
     *  Chave de serialização
     */

    private static final long serialVersionUID = 1001L;

    /* Tipos das mensagens */

    /* *********************************************** ArrivalLounge ************************************************ */

    /* ************* whatShouldIDo ************* */
    public static final int WSID = 1;

    public static final int FNDST = 2;

    public static final int TRDST = 3;

    /* ************* takeARest ************* */
    public static final int TAKEARST = 4;

    public static final int TAKERSTDONE = 5;

    /* ************* tryToCollectABag ************* */
    public static final int TRYTOCOL = 6;

    public static final int BAG = 7;

    public static final int NULLBAG = 8;

    /* ************* noMoreBagsToCollec ************* */
    public static final int NOBAGS2COL = 9;

    /* ************* resetArrivalLounge ************* */
    public static final int RESETAL = 10;

    public static final int RESETALDONE = 11;

    /* ************* setDepartureTerminalRef ************* */
    public static final int SETDEPTERNREF = 12;

    /* ************* setEndDay ************* */


    /* ******************************************** ArrivalTerminalExit ********************************************* */

    /*goHome*/
    public static final int GOHOME = 12;

    public static final int GODONE = 13;

    /*notifyFromPrepareNextLeg*/
    public static final int NOTFNEXTL = 14;

    /*incDecCounter*/
    public static final int INCDECCOUNTER = 15;

    /*resetArrivalTerminalExit*/

    /*getDeadPassValue*/

    /*setDepartureTerminalRef*/


    /* ****************************************** ArrivalTermTransfQuay ********************************************* */

    /*takeABus*/
    public static final int TAKEABUS = 16;

    public static final int TAKEABUSDONE = 17;

    /*enterTheBus*/
    public static final int ENTERBUS = 18;

    /*hasDaysWorkEnded*/
    public static final int WORKENDED = 19;

    public static final int CONTDAYS = 20;

    /*parkTheBus*/
    public static final int PARKBUS = 20;

    public static final int PBDONE = 21;

    /*announcingBusBoarding*/
    public static final int ANNOUCEBUSBORADING = 22;

    public static final int ABBDONE = 23;

    /*resetArrivalTermTransfQuay*/

    /*setEndDay*/


    /* *********************************************** BaggageColPoint ********************************************** */

    /*goCollectABag*/
    public static final int GOCOLLECTBAG = 24;

    public static final int GCBDONE = 25;

    /*carryItToAppropriateStore*/
    public static final int CARRYAPPSTORE = 26;

    /*resetBaggageColPoint*/

    /*noMoreBags*/

    /*setTreadmill*/

    /*setPHoldEmpty*/


    /* ******************************************** BaggageReclaimOffice ******************************************** */

    /*reportMissingBags*/
    public static final int REPORTMISSBAG = 27;

    /* ****************************************** DepartureTerminalEntrance ***************************************** */

    /*prepareNextLeg*/
    public static final int PREPARENEXTLEG = 28;

    public static final int PNLDONE = 29;

    /*resetDepartureTerminalExit*/

    /*notifyFromGoHome*/

    /*noMoreBags*/

    /*setArrivalTerminalRef*/


    /* ******************************************** DepartureTermTransfQuay ***************************************** */

    /*leaveTheBus*/
    public static final int LEAVEBUS = 30;

    public static final int LBDONE = 31;

    /*parkTheBusAndLetPassOff*/
    public static final int PBLPO = 32;

    public static final int PBLPODONE = 33;

    /*resetDepartureTermTransfQuay*/


    /* *********************************************** TemporaryStorageArea ***************************************** */

    /*carryItToAppropriateStore*/
    public static final int CARRYTOAPPSTORE_TSA = 34;

    /*resetTemporaryStorageArea*/


    /* ******************************************** GENERAL MESSAGES ************************************************ */

    public static final int ACK      =  35; // TODO: Change value

    public static final int ENDPASSENGER      = 36;

    public static final int ENDPORTER      = 37;

    public static final int ENDBUSDRIVER     = 38;


    /**
     *  Inicialização do ficheiro de logging (operação pedida pelo cliente)
     */
    public static final int SETNFIC  =  1;

    /**
     *  Ficheiro de logging foi inicializado (resposta enviada pelo servidor)
     */
    public static final int NFICDONE =  2;

    /**
     *  Enviar a identificação do cliente (resposta enviada pelo servidor)
     */

    public static final int PASSID   = 39;

    /**
     *  Shutdown do servidor (operação pedida pelo cliente)
     */

    public static final int SHUT   = 40;

    /* Campos das mensagens */

    /**
     *  Tipo da mensagem
     */

    private int msgType = -1;

    /**
     *  Identificação do cliente
     */

    private int custId = -1;

    /**
     *
     */

    private int passId = -1;

    /**
     *  Identificação do barbeiro
     */

    private int barbId = -1;

    /**
     *  Nome do ficheiro de logging
     */

    private String fName = null;

    /**
     *  Número de iterações do ciclo de vida dos clientes
     */

    private int nIter = -1;

    private int msgBagDestStat = -1;

    private int msgBagIdOwner = -1;

    private int[][] msgBagAndPassDest;

    private int[][] msgNBagsNA;

    private DepartureTerminalEntranceStub msgDepTermEntStub;

    /**
     *  Instanciação de uma mensagem (forma 1).
     *
     *    @param type tipo da mensagem
     */

    public Message (int type)
    {
        msgType = type;
    }

    /**
     *  Instanciação de uma mensagem (forma 2).
     *
     *    @param type tipo da mensagem
     *    @param id identificação do cliente/barbeiro
     */


    public Message (int type, int id) {
        msgType = type;
        if ((msgType == WSID) || (msgType == GOCOLLECTBAG) || (msgType == REPORTMISSBAG) || (msgType == GOHOME)
                || (msgType == TAKEABUS) || (msgType == ENTERBUS) || (msgType == LEAVEBUS)
                || (msgType == PREPARENEXTLEG)){
            passId = id;
        }
    }

    public Message (int type, int[][] bagAndPassDest, int[][] nBagsNA) {
        msgType = type;
        if (msgType ==  RESETAL){
            msgBagAndPassDest = bagAndPassDest;
            msgNBagsNA = nBagsNA;
        }
    }

    public Message (int type, DepartureTerminalEntranceStub departureTerminalEntranceStub) {
        msgType = type;
        if (msgType ==  SETDEPTERNREF){
            msgDepTermEntStub = departureTerminalEntranceStub;
        }
    }

    /**
     *  Instanciação de uma mensagem (forma 3).
     *
     *    @param type tipo da mensagem
     *    @param firstInt
     *    @param secondInt
     */

    public Message (int type, int firstInt, int secondInt)
    {
        msgType = type;
        if (msgType == BAG){
            msgBagDestStat = firstInt;
            msgBagIdOwner = secondInt;
        }
    }

    /**
     *  Instanciação de uma mensagem (forma 4).
     *
     *    @param type tipo da mensagem
     *    @param name nome do ficheiro de logging
     *    @param nIter número de iterações do ciclo de vida dos clientes
     */

    public Message (int type, String name, int nIter)
    {
        msgType = type;
        fName= name;
        this.nIter = nIter;
    }

    public int getMsgBagDestStat(){
        return msgBagDestStat;
    }

    public int getMsgBagIdOwner(){ return msgBagIdOwner; }

    public int[][] getMsgBagAndPassDest(){
        return msgBagAndPassDest;
    }

    public int[][] getMsgNBagsNA(){ return msgNBagsNA; }
    public DepartureTerminalEntranceStub getMsgDepTermEntStub(){ return msgDepTermEntStub; }


    /**
     *  Obtenção do valor do campo tipo da mensagem.
     *
     *    @return tipo da mensagem
     */

    public int getType ()
    {
        return (msgType);
    }

    /**
     *  Obtenção do valor do campo identificador do cliente.
     *
     *    @return identificação do cliente
     */

    public int getCustId ()
    {
        return (custId);
    }


    public int getPassId() {
        return passId;
    }

    /**
     *  Obtenção do valor do campo identificador do barbeiro.
     *
     *    @return identificação do barbeiro
     */

    public int getBarbId ()
    {
        return (barbId);
    }

    /**
     *  Obtenção do valor do campo nome do ficheiro de logging.
     *
     *    @return nome do ficheiro
     */

    public String getFName ()
    {
        return (fName);
    }

    /**
     *  Obtenção do valor do campo número de iterações do ciclo de vida dos clientes.
     *
     *    @return número de iterações do ciclo de vida dos clientes
     */

    public int getNIter ()
    {
        return (nIter);
    }

    /**
     *  Impressão dos campos internos.
     *  Usada para fins de debugging.
     *
     *    @return string contendo, em linhas separadas, a concatenação da identificação de cada campo e valor respectivo
     */

    @Override
    public String toString ()
    {
        return ("Tipo = " + msgType +
                "\nId Cliente = " + custId +
                "\nId Barbeiro = " + barbId +
                "\nNome Fic. Logging = " + fName +
                "\nN. de Iteracoes = " + nIter);
    }
}