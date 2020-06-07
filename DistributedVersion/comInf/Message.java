package comInf;

import clientSide.sharedRegionsStubs.ArrivalTermTransfQuayStub;
import clientSide.sharedRegionsStubs.BaggageColPointStub;
import clientSide.sharedRegionsStubs.DepartureTerminalEntranceStub;
import clientSide.sharedRegionsStubs.GenReposInfoStub;
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

    /* ************* probPar ************************** */
    public static final int PARAMSARRLNG = 1;

    /* ************* whatShouldIDo ******************** */
    public static final int WSID = 2;

    public static final int FNDST = 3;

    public static final int TRDST = 4;

    /* ************* takeARest ************************ */
    public static final int TAKEARST = 5;

    public static final int TAKERSTDONE = 6;

    /* ************* tryToCollectABag ***************** */
    public static final int TRYTOCOL = 7;

    public static final int BAG = 8;

    public static final int NULLBAG = 9;

    /* ************* noMoreBagsToCollec *************** */
    public static final int NOBAGS2COL = 10;

    /* ************* resetArrivalLounge *************** */
    public static final int RESETAL = 11;

    public static final int RESETALDONE = 12;

    /* ************* setDepartureTerminalRef ********** */
    public static final int SETDEPTERNREF = 13;

    /* ******************************************** ArrivalTerminalExit ********************************************* */

    /* ************* probPar ************************** */
    public static final int PARAMSATEXIT = 14;

    /* ******** goHome ******************************** */
    public static final int GOHOME = 15;

    public static final int GODONE = 16;

    /* ***** notifyFromPrepareNextLeg ***************** */
    public static final int NOTFNEXTL = 17;

    /* ********* incDecCounter ************************ */
    public static final int INCDECCOUNTER = 18;

    public static final int CONTCOUNTER = 19;

    public static final int LIMITCOUNTER = 20;

    /* ******* resetArrivalTerminalExit *************** */
    public static final int RESETATE = 21;

    /* ******* getDeadPassValue *********************** */
    public static final int GETDEADPASSVAL = 22;

    public static final int DEADPASSVAL = 23;

    /* ****** setDepartureTerminalRef ***************** */
    // same as for ArrivalLounge

    /* ****************************************** ArrivalTermTransfQuay ********************************************* */

    /* ************* probPar ************************** */
    public static final int PARAMSATTQUAY = 24;

    /* ******* takeABus ******************************* */
    public static final int TAKEABUS = 25;

    public static final int TAKEABUSDONE = 26;

    /* ******* enterTheBus **************************** */
    public static final int ENTERBUS = 27;

    /* ******* hasDaysWorkEnded  ********************** */
    public static final int WORKENDED = 28;

    public static final int CONTDAYS = 29;

    /* ******* parkTheBus ***************************** */
    public static final int PARKBUS = 30;

    public static final int PBDONE = 31;

    /* ******* announcingBusBoarding ****************** */
    public static final int ANNOUCEBUSBORADING = 32;

    public static final int ABBDONE = 33;

    /* ******* resetArrivalTermTransfQuay ************* */
    public static final int RESETATQ = 34;

    /* ********* setEndDay **************************** */
    public static final int SETENDDAY = 35;

    /* *********************************************** BaggageColPoint ********************************************** */

    /* ************* probPar ************************** */
    public static final int PARAMSBAGCOLPNT = 36;

    /* ******* goCollectABag ************************** */
    public static final int GOCOLLECTBAG = 37;

    public static final int GCBDONE = 38;

    /* ******* carryItToAppropriateStore ************** */
    public static final int CARRYAPPSTORE = 39;

    /* ******* resetBaggageColPoint ******************* */

    /* ******* noMoreBags ***************************** */

    /* ******* setTreadmill *************************** */

    /* ******* setPHoldEmpty ************************** */


    /* ******************************************** BaggageReclaimOffice ******************************************** */

    /* ************* probPar ************************** */
    public static final int PARAMSBAGRECOFF = 46;

    /* ******* reportMissingBags ********************** */
    public static final int REPORTMISSBAG = 47;

    /* ****************************************** DepartureTerminalEntrance ***************************************** */

    /* ************* probPar ************************** */
    public static final int PARAMSDEPTENT = 48;

    /* ******* prepareNextLeg ************************* */
    public static final int PREPARENEXTLEG = 49;

    public static final int PNLDONE = 50;

    /* ******* resetDepartureTerminalExit ************* */

    /* ******* notifyFromGoHome *********************** */

    /* ******* noMoreBags ***************************** */

    /* ******* setArrivalTerminalRef ****************** */


    /* ******************************************** DepartureTermTransfQuay ***************************************** */

    /* ************* probPar ************************** */
    public static final int PARAMSDEPTTQUAY = 57;

    /* ******* leaveTheBus **************************** */
    public static final int LEAVEBUS = 58;

    public static final int LBDONE = 59;

    /* ******* parkTheBusAndLetPassOff **************** */
    public static final int PBLPO = 60;

    public static final int PBLPODONE = 61;

    /* ******* resetDepartureTermTransfQuay *********** */


    /* *********************************************** TemporaryStorageArea ***************************************** */

    /* ************* probPar ************************** */
    public static final int PARAMSTEMPSTORAREA = 63;

    /* ******* carryItToAppropriateStore ************** */
    public static final int CARRYTOAPPSTORE_TSA = 64;

    /* ******* resetTemporaryStorageArea ************** */


    /* ******************************************** GENERAL MESSAGES ************************************************ */

    public static final int ACK      =  66; // TODO: Change value

    public static final int ENDPASSENGER      = 67;

    public static final int ENDPORTER      = 68;

    public static final int ENDBUSDRIVER     = 69;


    /**
     *  Inicialização do ficheiro de logging (operação pedida pelo cliente)
     */
    public static final int SETNFIC  =  70;

    /**
     *  Ficheiro de logging foi inicializado (resposta enviada pelo servidor)
     */
    public static final int NFICDONE =  71;

    /**
     *  Shutdown do servidor (operação pedida pelo cliente)
     */

    public static final int SHUT   = 72;

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

    private int[][] msgNBagsPHold;

    private DepartureTerminalEntranceStub msgDepTermEntStub = null;

    private GenReposInfoStub msgReposStub = null;

    private BaggageColPointStub msgBagColPointStub = null;

    private ArrivalTermTransfQuayStub msgArrQuayStub = null;

    private boolean msgIncOrDec;

    private int msgDeadPassValue = -1;

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


    public Message (int type, int value) {
        msgType = type;
        if ((msgType == WSID) || (msgType == GOCOLLECTBAG) || (msgType == REPORTMISSBAG) || (msgType == GOHOME)
                || (msgType == TAKEABUS) || (msgType == ENTERBUS) || (msgType == LEAVEBUS)
                || (msgType == PREPARENEXTLEG)){
            passId = value;
        } else if (msgType == DEADPASSVAL){
            msgDeadPassValue = value;
        }
    }

    public Message (int type, GenReposInfoStub reposStub, BaggageColPointStub bagColPointStub,
                    ArrivalTermTransfQuayStub arrQuayStub, int[][] destStat, int[][] nBagsPHold) {
        msgType = type;
        if (msgType ==  PARAMSARRLNG){
            msgReposStub = reposStub;
            msgBagColPointStub = bagColPointStub;
            msgArrQuayStub = arrQuayStub;
            msgBagAndPassDest = destStat;
            msgNBagsPHold = nBagsPHold;
        }
    }

    public Message (int type, int[][] bagAndPassDest, int[][] nBagsNA) {
        msgType = type;
        if (msgType ==  RESETAL){
            msgBagAndPassDest = bagAndPassDest;
            msgNBagsNA = nBagsNA;
        }
    }

    public Message (int type, boolean incOrDec) {
        msgType = type;
        if (msgType ==  INCDECCOUNTER){
            msgIncOrDec = incOrDec;
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

    public int[][] getMsgNBagsPHold(){ return msgNBagsPHold; }

    public DepartureTerminalEntranceStub getMsgDepTermEntStub(){ return msgDepTermEntStub; }

    public boolean getIncOrDec(){ return msgIncOrDec; }

    public int getMsgDeadPassVal(){ return msgDeadPassValue; }

    public GenReposInfoStub getMsgReposStub(){ return msgReposStub; }

    public BaggageColPointStub getMsgBagColPointStub(){ return msgBagColPointStub; }

    public ArrivalTermTransfQuayStub getMsgArrQuayStub(){ return msgArrQuayStub; }

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