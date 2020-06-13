package comInf;
import clientSide.entities.*;
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

    public static final int ENDPASSENGER = 84;

    public static final int ENDPORTER = 85;

    public static final int ENDBUSDRIVER = 86;

    /**
     *  Shutdown do servidor (operação pedida pelo cliente)
     */

    public static final int SHUT   = 87;

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

    private int stPass;

    private int stPorter;

    private int stBD;

    /**
     *  Instanciação de uma mensagem (forma 1).
     *
     *    @param type tipo da mensagem
     */

    public Message(int type)
    {
        msgType = type;
        System.out.println("-------- Created Message of type " + getMsgTypeString(type) + " --------");
    }

    public Message(int type, int value) {
        msgType = type;
        System.out.println("-------- Created Message of type " + getMsgTypeString(type) + " --------");
        if ((msgType == WSID) || (msgType == GOCOLLECTBAG) || (msgType == REPORTMISSBAG) || (msgType == GOHOME)
                || (msgType == TAKEABUS) || (msgType == ENTERBUS) || (msgType == LEAVEBUS)
                || (msgType == PREPARENEXTLEG)){
            passId = value;
            System.out.println("passId " + value);
        } else if (msgType == DEADPASSVAL){
            msgDeadPassValue = value;
            System.out.println("msgDeadPassValue " + value);
        } else if(msgType == UPDATEFN){
            msgFlight = value;
            System.out.println("msgFlight " + value);
        } else if(msgType == INITCHOLD){
            msgBN = value;
            System.out.println("msgBN " + value);
        } else if(msgType == NUMNRTOTAL){
            msgNR = value;
            System.out.println("msgNR " + value);
        } else if(msgType == UDTEBDSTAT){
            bdStat = value;
            System.out.println("bdStat " + value);
        } else if(msgType == NEWPASS){
            passSi = value;
            System.out.println("passSi " + value);
        } else if(msgType == UDTEPORTSTAT){
            porterStat = value;
            System.out.println("porterStat " + value);
        } else if((msgType == TAKEARST) || (msgType == TRYTOCOL) || (msgType == NOBAGS2COL) || (msgType == NOMOREBAGS)){
            this.stPorter = value;
            System.out.println("stPorter " + value);
        } else if((msgType == WORKENDED) || (msgType == PARKBUS) || (msgType == ANNOUCEBUSBORADING) || (msgType == PBLPO)){
            this.stBD = value;
            System.out.println("stBD " + value);
        }
    }

    public Message (int type, ArrivalTerminalExitStub arrivalTerminalExitStub) {
        msgType = type;
        System.out.println("-------- Created Message of type " + getMsgTypeString(type) + " --------");
        if (msgType ==  SETARRTERREF){
            msgArrTermExitStub = arrivalTerminalExitStub;
            System.out.println("msgArrTermExitStub " + arrivalTerminalExitStub);
        }
    }

    public Message (int type, GenReposInfoStub reposStub) {
        msgType = type;
        System.out.println("-------- Created Message of type " + getMsgTypeString(type) + " --------");
        if (msgType ==  PARAMSATTQUAY || msgType == PARAMSBAGCOLPNT || msgType == PARAMSBAGRECOFF
                || msgType == PARAMSDEPTTQUAY || msgType == PARAMSTEMPSTORAREA){
            msgReposStub = reposStub;
            System.out.println("msgReposStub " + reposStub);
        }
    }

    public Message (int type, GenReposInfoStub reposStub, ArrivalLoungeStub arrivalLoungeStub,
                    ArrivalTermTransfQuayStub arrivalTermTransfQuayStub) {
        msgType = type;
        System.out.println("-------- Created Message of type " + getMsgTypeString(type) + " --------");
        if (msgType ==  PARAMSATEXIT){
            msgReposStub = reposStub;
            msgArrLoungeStub = arrivalLoungeStub;
            msgArrQuayStub = arrivalTermTransfQuayStub;
            System.out.println("msgReposStub " + reposStub);
            System.out.println("msgArrLoungeStub " + arrivalLoungeStub);
            System.out.println("msgArrQuayStub " + arrivalTermTransfQuayStub);
        }
    }

    public Message (int type, String filename) {
        msgType = type;
        System.out.println("-------- Created Message of type " + getMsgTypeString(type) + " --------");
        if (msgType == PARAMSREPOS) {
            msgReposFile = filename;
            System.out.println("msgReposFile " + filename);
        }
    }

    public Message (int type, int[][] bagAndPassDest, int[][] nBagsNA) {
        msgType = type;
        System.out.println("-------- Created Message of type " + getMsgTypeString(type) + " --------");
        if ((msgType ==  RESETAL) || (msgType == PARAMSARRLNG)){
            msgBagAndPassDest = bagAndPassDest;
            System.out.println("msgBagAndPassDest " + msgBagAndPassDest);
            msgNBagsNA = nBagsNA;
            System.out.println("msgNBagsNA " + msgNBagsNA);
        }
    }

    public Message (int type, int[] nBagsPerPass) {
        msgType = type;
        System.out.println("-------- Created Message of type " + getMsgTypeString(type) + " --------");
        if(msgType ==  SETTREADMILL){
            msgNBagsPerPass = nBagsPerPass;
            System.out.println("msgNBagsPerPass " + msgNBagsPerPass);
        }
    }
    public Message (int type, boolean bool) {
        msgType = type;
        System.out.println("-------- Created Message of type " + getMsgTypeString(type) + " --------");
        if (msgType ==  INCDECCOUNTER){
            msgIncOrDec = bool;
            System.out.println("msgIncOrDec " + msgIncOrDec);
        } else if(msgType == GCBDONE){
            msgBagCollected = bool;
            System.out.println("msgBagCollected " + msgBagCollected);
        } else if(msgType == SETPHEMPTY){
            msgPlaneHoldEmpty = bool;
            System.out.println("msgPlaneHoldEmpty " + msgPlaneHoldEmpty);
        }
    }

    public Message (int type, DepartureTerminalEntranceStub departureTerminalEntranceStub) {
        msgType = type;
        System.out.println("-------- Created Message of type " + getMsgTypeString(type) + " --------");
        if (msgType ==  SETDEPTERNREF){
            msgDepTermEntStub = departureTerminalEntranceStub;
            System.out.println("msgDepTermEntStub " + msgDepTermEntStub);
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
        System.out.println("-------- Created Message of type " + getMsgTypeString(type) + " --------");
        if ((msgType == WSID) || (msgType == GOCOLLECTBAG) || (msgType == REPORTMISSBAG) || (msgType == GOHOME)
                || (msgType == TAKEABUS) || (msgType == ENTERBUS) || (msgType == LEAVEBUS)
                || (msgType == PREPARENEXTLEG)){
            passId = firstInt;
            System.out.println("passId " + passId);
            stPass = secondInt;
            System.out.println("stPass " + stPass);
        } else if ((msgType == BAG) || (msgType == CARRYAPPSTORE) || (msgType == CARRYTOAPPSTORE_TSA)){
            msgBagDestStat = firstInt;
            System.out.println("msgBagDestStat " + msgBagDestStat);
            msgBagIdOwner = secondInt;
            System.out.println("msgBagIdOwner " + msgBagIdOwner);
        } else if(msgType == UDTEPASSNR){
            passId = firstInt;
            System.out.println("passId " + passId);
            passNR = secondInt;
            System.out.println("passNR " + passNR);
        } else if(msgType == UDTEPASSNA){
            passId = firstInt;
            System.out.println("passId " + passId);
            passNA = secondInt;
            System.out.println("passNA " + passNA);
        } else if(msgType == UDTEPASSSTAT){
            passId = firstInt;
            System.out.println("passId " + passId);
            passStat = secondInt;
            System.out.println("passStat " + passStat);
        } else if (msgType ==  GETPASSSI){
            passId = firstInt;
            System.out.println("passId " + passId);
            passSi = secondInt;
            System.out.println("passSi " + passSi);
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
        System.out.println("-------- Created Message of type " + getMsgTypeString(type) + " --------");
        fName= name;
        System.out.println("fName " + fName);
        this.nIter = nIter;
        System.out.println("nIter " + nIter);
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

    public int getPassStatInt(){ return passStat; }

    public int getPassStat(){return stPass;}

    public int getPorterStat(){return stPorter;}

    public int getBDStat(){return stBD;}

    public int getPorterStatInt(){ return porterStat; }

    public int getBDStatInt(){ return bdStat; }

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

    public static String getMsgTypeString(int type){
        String str;
        switch(type){
            case 1: str = "PARAMSARRLNG"; break; case 2: str = "WSID"; break; case 3: str = "FNDST"; break;
            case 4: str = "TRDST"; break; case 5: str = "TAKEARST"; break; case 6: str = "TAKERSTDONE"; break;
            case 7: str = "TRYTOCOL"; break; case 8: str = "BAG"; break; case 9: str = "NULLBAG"; break;
            case 10: str = "NOBAGS2COL"; break; case 11: str = "RESETAL"; break; case 12: str = "RESETALDONE"; break;
            case 13: str = "SETDEPTERNREF"; break; case 14: str = "PARAMSATEXIT"; break; case 15: str = "GOHOME"; break;
            case 16: str = "GODONE"; break; case 17: str = "NOTFNEXTL"; break; case 18: str = "INCDECCOUNTER"; break;
            case 19: str = "CONTCOUNTER"; break; case 20: str = "IMITCOUNTER"; break; case 21: str = "RESETATE"; break;
            case 22: str = "GETDEADPASSVAL"; break; case 23: str = "DEADPASSVAL"; break;
            case 24: str = "PARAMSATTQUAY"; break; case 25: str = "TAKEABUS"; break;
            case 26: str = "TAKEABUSDONE"; break; case 27: str = "ENTERBUS"; break; case 28: str = "WORKENDED"; break;
            case 29: str = "CONTDAYS"; break; case 30: str = "PARKBUS"; break; case 31: str = "PBDONE"; break;
            case 32: str = "ANNOUCEBUSBORADING"; break; case 33: str = "ABBDONE"; break;
            case 34: str = "RESETATQ"; break; case 35: str = "SETENDDAY"; break; case 36: str = "PARAMSBAGCOLPNT"; break;
            case 37: str = "GOCOLLECTBAG"; break; case 38: str = "GCBDONE"; break; case 39: str = "CARRYAPPSTORE"; break;
            case 40: str = "RESETBCP"; break; case 41: str = "NOMOREBAGS"; break; case 42: str = "SETTREADMILL"; break;
            case 43: str = "SETPHEMPTY"; break; case 44: str = "PARAMSBAGRECOFF"; break;
            case 45: str = "REPORTMISSBAG"; break; case 46: str = "PARAMSDEPTENT"; break;
            case 47: str = "PREPARENEXTLEG"; break; case 48: str = "PNLDONE"; break; case 49: str = "RESETDTE"; break;
            case 50: str = "NOTFGOHOME"; break; case 51: str = "SETARRTERREF"; break;
            case 52: str = "PARAMSDEPTTQUAY"; break; case 53: str = "LEAVEBUS"; break; case 54: str = "LBDONE"; break;
            case 55: str = "PBLPO"; break; case 56: str = "PBLPODONE"; break; case 57: str = "RESETDTTQ"; break;
            case 58: str = "PARAMSTEMPSTORAREA"; break; case 59: str = "CARRYTOAPPSTORE_TSA"; break;
            case 60: str = "RESETTSA"; break; case 61: str = "PARAMSREPOS"; break; case 62: str = "PRINTLOG"; break;
            case 63: str = "FINALREPORT"; break; case 64: str ="UPDATEFN"; break; case 65: str = "INITCHOLD"; break;
            case 66: str = "REMBAGCHOLD"; break; case 67: str = "INCBAGCB"; break; case 68: str = "PGETSABAG"; break;
            case 69: str = "SAVEBAGSR"; break; case 70: str = "UDTEPORTSTAT"; break; case 71: str = "UDTEBDSTAT"; break;
            case 72: str = "PJOINWQ"; break; case 73: str = "PLEFTWQ"; break; case 74: str = "FREEBS"; break;
            case 75: str = "NEWPASS"; break; case 76: str = "UDTEPASSSTAT"; break; case 77: str = "GETPASSSI"; break;
            case 78: str = "UDTEPASSNR"; break; case 79: str = "UDTEPASSNA"; break; case 80: str = "PASSEXIT"; break;
            case 81: str = "MISSBAGREP"; break; case 82: str = "NUMNRTOTAL"; break; case 83: str = "ACK"; break;
            case 84: str = "ENDPASSENGER"; break; case 85: str = "ENDPORTER"; break;
            case 86: str = "ENDBUSDRIVER"; break; case 87: str = "SHUT";
            default: str = "";
        }
        return str;
    }
}