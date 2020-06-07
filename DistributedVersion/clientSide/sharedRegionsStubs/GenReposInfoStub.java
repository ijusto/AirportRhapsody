package clientSide.sharedRegionsStubs;

import clientSide.BusDriverStates;
import clientSide.PassengerStates;
import clientSide.PorterStates;
import clientSide.clients.ClientCom;
import comInf.Message;

public class GenReposInfoStub {

    /**
     *  Nome do sistema computacional onde está localizado o servidor
     *    @serialField serverHostName
     */

    private String serverHostName = null;

    /**
     *  Número do port de escuta do servidor
     *    @serialField serverPortNumb
     */

    private int serverPortNumb;

    /**
     *  Instanciação do stub à barbearia.
     *
     *    @param hostName nome do sistema computacional onde está localizado o servidor
     *    @param port número do port de escuta do servidor
     */

    public GenReposInfoStub (String hostName, int port) {
        serverHostName = hostName;
        serverPortNumb = port;
    }


    public void probPar(String filename){

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ()){                                                // aguarda ligação
            try {
                Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (Message.PARAMSREPOS, filename);
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType() != Message.ACK) {
            System.out.println("Arranque da simulação: Tipo inválido!");
            System.out.println(inMessage.toString ());
            System.exit (1);
        }
        con.close ();
    }

    /**
     *  Fazer o shutdown do servidor (solicitação do serviço).
     */

    public void shutdown ()
    {
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ()){                                                // aguarda ligação
            try {
                Thread.currentThread ().sleep ((long) (10));
            } catch (InterruptedException e) {}
        }
        outMessage = new Message (Message.SHUT);
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType () != Message.ACK) {
            System.out.println("Thread " + Thread.currentThread ().getName () + ": Tipo inválido!");
            System.out.println(inMessage.toString ());
            System.exit (1);
        }
        con.close ();
    }

    /**
     *   Logs the alterations.
     */

    public void printLog(){
    }

    /**
     *   Logs the final report of the simulation.
     */

    public void finalReport(){
    }

    /* **************************************************Plane******************************************************* */

    /**
     *   Update flight number after the previous flight is finished.
     *
     *   @param flight flight.
     */

    public void updateFlightNumber(int flight){
    }

    /**
     *   Initializes the number of pieces of luggage at the plane's hold.
     *
     *    @param bn Number of pieces of luggage presently at the plane's hold.
     */

    public void initializeCargoHold(int bn){
    }

    /**
     *   Update baggage stored in the cargo hold when porter retrieves the baggage.
     */

    public void removeBagFromCargoHold(){
    }

    /* **************************************************Porter****************************************************** */

    /**
     *   Update baggage stored in the Conveyor Belt when porter adds bags.
     */

    public void incBaggageCB(){
    }

    /**
     *   Update baggage stored in the Conveyor Belt when passenger gets the baggage.
     */

    public void pGetsABag(){
    }

    /**
     *   Update baggage stored in the Storage Room when porter puts the baggage
     */

    public void saveBagInSR(){
    }

    /**
     *   Update the Porter state.
     *
     *    @param porterState Porter's state.
     */

    public void updatePorterStat(PorterStates porterState){
    }

    /* ************************************************Bus Driver**************************************************** */

    /**
     *   Update the Bus Driver State.
     *
     *   @param busDriverState Bus driver's state.
     */

    public void updateBDriverStat(BusDriverStates busDriverState){
    }

    /**
     *   Update the queue of passengers.
     *
     *    @param id Passenger's id.
     */

    public void pJoinWaitingQueue(int id){
    }

    /**
     *   Update of the occupation state of the bus seat.
     *
     *    @param id Passenger's id.
     */

    public void pLeftWaitingQueue(int id){
    }

    /**
     *   Removes the passenger id from the bus seat.
     *
     *    @param id Passenger's id.
     */

    public void freeBusSeat(int id){
    }

    /* **************************************************Passenger*************************************************** */

    /**
     *   Increments the number of passengers from a certain passenger situation.
     *
     *    @param passSi Passenger's situation.
     */

    public  void newPass(Passenger.SiPass passSi){
    }

    /**
     *   Update the Passenger State.
     *
     *    @param id Passenger's id.
     *    @param passengerState Passenger's state.
     */

    public void updatePassSt(int id, PassengerStates passengerState){
    }

    /**
     *   Get the passenger situation (in transit or final).
     *
     *    @param id Passenger's id.
     *    @param si Passenger's situation.
     */

    public void getPassSi(int id, String si){
    }

    /**
     *   Update the passengers luggage at the start of the journey.
     *
     *   @param id Passenger's id.
     *   @param nr Number of pieces of luggage the passenger had at the start of the journey.
     */

    public void updatesPassNR(int id, int nr){
    }

    /**
     *   Update the number of luggage a passenger collected.
     *
     *   @param id Passenger's id.
     *   @param na Number of pieces of luggage the passenger has collected.
     */

    public void updatesPassNA(int id, int na){
    }

    /**
     *   Resets all the info of a passenger with a certain id.
     *
     *    @param id Passenger's id.
     */

    public void passengerExit(int id){
    }

    /* ***********************************************Final Report*************************************************** */

    /**
     *   Updates the number of reported missing bags.
     */

    public void missingBagReported(){
    }

    /**
     *   Updates the number of pieces of luggage all passengers combined had at the start of the journey.
     *
     *    @param nr Number of pieces of luggage all passengers combined had at the start of the journey.
     */

    public void numberNRTotal(int nr){
    }

}