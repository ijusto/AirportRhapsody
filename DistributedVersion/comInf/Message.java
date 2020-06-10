package comInf;
import clientSide.sharedRegionsStubs.*;

import java.io.*;
import java.util.Arrays;

/**
 *   Este tipo de dados define as mensagens que são trocadas entre os clientes e o servidor numa solução do Problema
 *   dos Barbeiros Sonolentos que implementa o modelo cliente-servidor de tipo 2 (replicação do servidor) com lançamento
 *   estático dos threads barbeiro.
 *   A comunicação propriamente dita baseia-se na troca de objectos de tipo Message num canal TCP.
 *
 *   @author Inês Justo
 *   @author Miguel Lopes
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
    public static final int RESETBCP = 40;

    /* ******* noMoreBags ***************************** */
    public static final int NOMOREBAGS = 41;

    /* ******* setTreadmill *************************** */
    public static final int SETTREADMILL = 42;

    /* ******* setPHoldEmpty ************************** */
    public static final int SETPHEMPTY = 43;

    /* ******************************************** BaggageReclaimOffice ******************************************** */

    /* ************* probPar ************************** */
    public static final int PARAMSBAGRECOFF = 44;

    /* ******* reportMissingBags ********************** */
    public static final int REPORTMISSBAG = 45;

    /* ****************************************** DepartureTerminalEntrance ***************************************** */

    /* ************* probPar ************************** */
    public static final int PARAMSDEPTENT = 46;

    /* ******* prepareNextLeg ************************* */
    public static final int PREPARENEXTLEG = 47;

    public static final int PNLDONE = 48;

    /* ******* resetDepartureTerminalExit ************* */
    public static final int RESETDTE = 49;

    /* ******* notifyFromGoHome *********************** */
    public static final int NOTFGOHOME = 50;

    /* ******* noMoreBags ***************************** */
    // same as for BaggageColPoint

    /* ******* setArrivalTerminalRef ****************** */
    public static final int SETARRTERREF = 51;

    /* ******************************************** DepartureTermTransfQuay ***************************************** */

    /* ************* probPar ************************** */
    public static final int PARAMSDEPTTQUAY = 52;

    /* ******* leaveTheBus **************************** */
    public static final int LEAVEBUS = 53;

    public static final int LBDONE = 54;

    /* ******* parkTheBusAndLetPassOff **************** */
    public static final int PBLPO = 55;

    public static final int PBLPODONE = 56;

    /* ******* resetDepartureTermTransfQuay *********** */
    public static final int RESETDTTQ = 57;

    /* *********************************************** TemporaryStorageArea ***************************************** */

    /* ************* probPar ************************** */
    public static final int PARAMSTEMPSTORAREA = 58;

    /* ******* carryItToAppropriateStore ************** */
    public static final int CARRYTOAPPSTORE_TSA = 59;

    /* ******* resetTemporaryStorageArea ************** */
    public static final int RESETTSA = 60;

    /* *********************************************** GenReposInfo ************************************************* */

    /* ************* probPar ************************** */
    public static final int PARAMSREPOS = 61;

    /* ************* printLog ************************* */
    public static final int PRINTLOG = 62;

    /* ************* finalReport ********************** */
    public static final int FINALREPORT = 63;

    /* ************* updateFlightNumber *************** */
    public static final int UPDATEFN = 64;

    /* ************* initializeCargoHold ************** */
    public static final int INITCHOLD = 65;

    /* ************* removeBagFromCargoHold *********** */
    public static final int REMBAGCHOLD = 66;

    /* ************* incBaggageCB ********************* */
    public static final int INCBAGCB = 67;

    /* ************* pGetsABag ************************** */
    public static final int PGETSABAG = 68;

    /* ************* saveBagInSR ************************** */
    public static final int SAVEBAGSR = 69;

     /* ************* updatePorterStat ************************** */
     public static final int UDTEPORTSTAT = 70;

     /* ************* updateBDriverStat ************************** */
     public static final int UDTEBDSTAT = 71;

     /* ************* pJoinWaitingQueue ************************** */
     public static final int PJOINWQ = 72;

     /* ************* pLeftWaitingQueue ************************** */
     public static final int PLEFTWQ = 73;

     /* ************* freeBusSeat ************************** */
     public static final int FREEBS = 74;

     /* ************* newPass ************************** */
     public static final int NEWPASS = 75;

     /* ************* updatePassSt ************************** */
     public static final int UDTEPASSSTAT = 76;

     /* ************* getPassSi ************************** */
     public static final int GETPASSSI = 77;

     /* ************* updatesPassNR ************************** */
     public static final int UDTEPASSNR = 78;

     /* ************* updatesPassNA ************************** */
     public static final int UDTEPASSNA = 79;

     /* ************* passengerExit ************************** */
     public static final int PASSEXIT = 80;

     /* ************* missingBagReported ************************** */
     public static final int MISSBAGREP = 81;

     /* ************* numberNRTotal ************************** */
     public static final int NUMNRTOTAL = 82;


    /* ******************************************** GENERAL MESSAGES ************************************************ */

    public static final int ACK =  83;

    public static final int ENDPASSENGER = 94;

    public static final int ENDPORTER = 95;

    public static final int ENDBUSDRIVER = 96;

    /**
     *  Shutdown do servidor (operação pedida pelo cliente)
     */

    public static final int SHUT   = 99;

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

    private ArrivalLoungeStub msgArrLoungeStub = null;

    private ArrivalTerminalExitStub msgArrTermExitStub = null;

    private String msgReposFile = null;

    private boolean msgIncOrDec;

    private boolean msgBagCollected;

    private boolean msgPlaneHoldEmpty;

    private int msgDeadPassValue = -1;

    private int msgFlight = -1;

    private int msgBN = -1;

    private int msgNR = -1;

    private int passSi = -1;

    private int passNR = -1;

    private int passNA = -1;

    private int[] msgNBagsPerPass;

    private int passId = -1;

    private int passStat = -1;

    private int porterStat = -1;

    private int bdStat = -1;

    /**
     *  Instanciação de uma mensagem (forma 1).
     *
     *    @param type tipo da mensagem
     */

    public Message(int type)
    {
        msgType = type;
    }

    /**
     *  Instanciação de uma mensagem (forma 2).
     *
     *    @param type tipo da mensagem
     *    @param value
     */


    public Message(int type, int value) {
        msgType = type;
        if (msgType == DEADPASSVAL){
            msgDeadPassValue = value;
        } else if(msgType == UPDATEFN){
            msgFlight = value;
        } else if(msgType == INITCHOLD){
            msgBN = value;
        } else if(msgType == NUMNRTOTAL){
            msgNR = value;
        } else if(msgType == UDTEBDSTAT){
            bdStat = value;
        } else if(msgType == NEWPASS){
            passSi = value;
        } else if(msgType == UDTEPORTSTAT){
            porterStat = value;
        }
    }

    public Message (int type, ArrivalTerminalExitStub arrivalTerminalExitStub) {
        msgType = type;
        if (msgType ==  SETARRTERREF){
            msgArrTermExitStub = arrivalTerminalExitStub;
        }
    }

    public Message (int type, GenReposInfoStub reposStub) {
        msgType = type;
        if (msgType ==  PARAMSATTQUAY || msgType == PARAMSBAGCOLPNT || msgType == PARAMSBAGRECOFF
                || msgType == PARAMSDEPTTQUAY || msgType == PARAMSTEMPSTORAREA){
            msgReposStub = reposStub;
        }
    }

    public Message (int type, GenReposInfoStub reposStub, ArrivalLoungeStub arrivalLoungeStub,
                    ArrivalTermTransfQuayStub arrivalTermTransfQuayStub) {
        msgType = type;
        if (msgType ==  PARAMSATEXIT){
            msgReposStub = reposStub;
            msgArrLoungeStub = arrivalLoungeStub;
            msgArrQuayStub = arrivalTermTransfQuayStub;
        }
    }

    public Message (int type, String filename) {
        msgType = type;
        if (msgType == PARAMSREPOS) {
            msgReposFile = filename;
        }
    }

    public Message (int type, int[][] bagAndPassDest, int[][] nBagsNA) {
        msgType = type;
        if ((msgType ==  RESETAL) || (msgType == PARAMSARRLNG)){
            msgBagAndPassDest = bagAndPassDest;
            msgNBagsNA = nBagsNA;
        }
    }

    public Message (int type, int[] nBagsPerPass) {
        msgType = type;
        if(msgType ==  SETTREADMILL){
            msgNBagsPerPass = nBagsPerPass;
        }
    }
    public Message (int type, boolean bool) {
        msgType = type;
        if (msgType ==  INCDECCOUNTER){
            msgIncOrDec = bool;
        } else if(msgType == GCBDONE){
            msgBagCollected = bool;
        } else if(msgType == SETPHEMPTY){
            msgPlaneHoldEmpty = bool;
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
        if ((msgType == BAG) || (msgType == CARRYAPPSTORE) || (msgType == CARRYTOAPPSTORE_TSA)){
            msgBagDestStat = firstInt;
            msgBagIdOwner = secondInt;
        } else if(msgType == UDTEPASSNR){
            passId = firstInt;
            passNR = secondInt;
        } else if(msgType == UDTEPASSNA){
            passId = firstInt;
            passNA = secondInt;
        } else if(msgType == UDTEPASSSTAT){
            passId = firstInt;
            passStat = secondInt;
        } else if (msgType ==  GETPASSSI){
            passId = firstInt;
            passSi = secondInt;
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

    public ArrivalLoungeStub getMsgArrLoungeStub(){ return msgArrLoungeStub; }

    public ArrivalTerminalExitStub getMsgArrTermExitStub(){ return msgArrTermExitStub; }

    public String getMsgReposFile(){ return msgReposFile; }

    public boolean msgBagCollected(){ return msgBagCollected; }

    public boolean msgPlaneHoldEmpty(){ return msgPlaneHoldEmpty; }

    public int getMsgFlight(){ return msgFlight; }

    public int getMsgBN(){ return msgBN; }

    public int getMsgNR(){ return msgNR; }

    public int getPassSi(){ return passSi; }

    public int getPassNR(){ return passNR; }

    public int getPassNA(){ return passNA; }

    public int[] getMsgNBagsPerPass(){ return msgNBagsPerPass; }

    /**
     *  Obtenção do valor do campo tipo da mensagem.
     *
     *    @return tipo da mensagem
     */

    public int getType(){ return msgType; }

    public int getPassId(){ return passId; }

    public int getPassStat(){ return passStat; }

    public int getPorterStat(){ return porterStat; }

    public int getBDStat(){ return bdStat; }

    /**
     *  Obtenção do valor do campo identificador do cliente.
     *
     *    @return identificação do cliente
     */

    public int getCustId ()
    {
        return (custId);
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

}