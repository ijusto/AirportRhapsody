package comInf;

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

    /**
     *  Inicialização do ficheiro de logging (operação pedida pelo cliente)
     */
    public static final int SETNFIC  =  1;

    /**
     *  Ficheiro de logging foi inicializado (resposta enviada pelo servidor)
     */
    public static final int NFICDONE =  2;



    /** ARRIVAL LOUGE */
    public static final int WSID = 3;

    public static final int FNDST = 4;

    public static final int TRDST = 5;

    public static final int TAKEARST = 6;

    public static final int TAKERSTDONE = 7;

    public static final int TRYTOCOL = 8;

    public static final int BAG = 9;

    public static final int NULLBAG = 10;

    public static final int NOBAGS2COL = 11;


    /** ARRIVAL TERMINAL EXIT */
    public static final int GOHOME = 12;

    public static final int GODONE = 13;

    public static final int INCDECCOUNTER = 14;


    /**ARRIVAL TERMINAL TRANSFER QUAY */
    public static final int TAKEABUS = 15;

    public static final int TAKEABUSDONE = 16;

    public static final int ENTERBUS = 17;

    public static final int WORKENDED = 18;

    public static final int PARKBUS = 19;

    public static final int PBDONE = 20;

    public static final int ANNOUCEBUSBORADING = 21;

    public static final int ABBDONE = 22;
    //VERIFICA APENAS AS OCORRENCIAS DAS FUNÇOES AUXILIARES AQUI, PARA MIM NAO APARECEU NADA FORA DA SHARED REGION


    /** BAGGAGE COLLECTION POINT */

    public static final int GOCOLLECTBAG = 23;

    public static final int GCBDONE = 24;

    public static final int CARRYAPPSTORE = 25;
    //NOVAMENTE VERIFICAR AUXILIARES


    /** BAGGAGE RECLAIM OFFICE */

    public static final int REPORTMISSBAG = 26;

    /**DEPARTURE TERMINAL ENTRACE */

    public static final int PREPARENEXTLEG = 27;

    public static final int PNLDONE = 28;
    //PLEASE CHECK AUXILIARY xD

    /**DEPARTURE TERMINAL TRANSFER QUAY */

    public static final int LEAVEBUS = 29;

    public static final int LBDONE = 30;

    public static final int PBLPO = 31;

    public static final int PBLPODONE = 32;
    //IM SORRY INTELLIJ IS DUMD

    /** TEMPORARY STORAGE AREA */

    public static final int CARRYTOAPPSTORE_TSA = 33;


    /**GENERAL MESSAGES */

    public static final int ACK      =  34; // TODO: Change value

    public static final int ENDPASSENGER      = 35;

    public static final int ENDPORTER      = 36;

    public static final int ENDBUSDRIVER     = 37;


    /**
     *  Enviar a identificação do cliente (resposta enviada pelo servidor)
     */

    public static final int PASSID   = 38;

    /**
     *  Shutdown do servidor (operação pedida pelo cliente)
     */

    public static final int SHUT   = 39;

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


    public Message (int type, int id)
    {
        msgType = type;
        if ((msgType == REQCUTH) || (msgType == CUSTID))
            custId= id;
        else barbId = id;
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

    public int getMsgBagIdOwner(){
        return msgBagIdOwner;
    }

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