package clientSide.sharedRegionsStubs;

import clientSide.PassengerStates;
import clientSide.SimulPar;
import clientSide.clients.ClientCom;
import clientSide.entities.Passenger;
import comInf.Bag;
import comInf.Message;
import old.GenReposInfo;
import serverSide.sharedRegions.ArrivalLounge;
import serverSide.sharedRegions.ArrivalTermTransfQuay;
import serverSide.sharedRegions.DepartureTerminalEntrance;

public class ArrivalTerminalExitStub {

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

    public ArrivalTerminalExitStub (String hostName, int port) {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     *  Fornecer parâmetros do problema (solicitação do serviço).
     *
     */

    public void probPar (GenReposInfo repos, ArrivalLoungeStub arrivLoungeStub, ArrivalTermTransfQuayStub arrivalQuayStub)//(String fName, int nIter)
    {

        ClientCom con = new ClientCom (serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open ()){                                                // aguarda ligação
            try {
                Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        /*
        outMessage = new Message (Message.SETNFIC, fName, nIter);
        con.writeObject (outMessage);
        inMessage = (Message) con.readObject ();
        if (inMessage.getType() != Message.NFICDONE) {
            System.out.println("Arranque da simulação: Tipo inválido!");
            System.out.println(inMessage.toString ());
            System.exit (1);
        }
         */
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
     *   Operation of the passenger of waiting for the last passenger to arrive the Arrival Terminal Exit or the
     *   Departure Terminal Entrance or, if the last, to notify all the others.
     */

    public void goHome(){
        Passenger passenger = (Passenger) Thread.currentThread();
        assert(passenger.getSt() == PassengerStates.AT_THE_DISEMBARKING_ZONE ||
                passenger.getSt() == PassengerStates.AT_THE_LUGGAGE_COLLECTION_POINT ||
                passenger.getSt() == PassengerStates.AT_THE_BAGGAGE_RECLAIM_OFFICE);
        passenger.setSt(PassengerStates.EXITING_THE_ARRIVAL_TERMINAL);

        repos.updatePassSt(passenger.getPassengerID(), PassengerStates.EXITING_THE_ARRIVAL_TERMINAL);
        repos.printLog();

        // increment the number of passengers that wants to leave the airport
        boolean isLastPass = this.incDecCounter(true);

        if(isLastPass) {
            // wakes up all the passengers
            wakeAllPassengers();
            //arrivLounge.notifyAllPassExited();

        } else {

            // the passengers that are not the last ones to want to leave the airport, need to wait for the last one to
            // notify them so they can leave
            while (this.getDeadPassValue() < SimulPar.N_PASS_PER_FLIGHT) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        repos.passengerExit(passenger.getPassengerID());
        repos.printLog();
    }

    /**
     *   Wakes up the passengers waiting in the Arrival Terminal Entrance.
     */

    public synchronized void notifyFromPrepareNextLeg(){
        notifyAll();
    }

    /**
     *   Wakes up all the passengers at the Arrival Terminal Exit and at the Departure Terminal Entrance.
     */

    public synchronized void wakeAllPassengers(){
        notifyAll();
        wakeDepPass();
    }

    /**
     *   Wakes up the passengers waiting in the Departure Terminal Entrance.
     */

    public synchronized void wakeDepPass(){
        departureTerm.notifyFromGoHome();
    }

    /**
     *   Operation of incrementing/decrementing the counter.
     *
     *    @return <li>true, if the value of the counter after the operation is the limit.</li>
     *            <li>false, otherwise.</li>
     */

    public boolean incDecCounter(boolean inc) {
        synchronized (lockDeadPassCounter) {
            if(inc) {
                deadPassCounter++;
            } else {
                deadPassCounter--;
            }
            return deadPassCounter == SimulPar.N_PASS_PER_FLIGHT;
        }
    }

    /**
     *   Sets the value of the counter to zero.
     */

    public void resetDeadPassCounter(){
        synchronized (lockDeadPassCounter) { // Locks on the private Object
            deadPassCounter = 0;
        }
    }

    /**
     *   Resets the counter of passengers that are at the exit od the Arrival Terminal or at the entrance of the
     *   Departure Terminal.
     */

    public synchronized void resetArrivalTerminalExit(){
        this.resetDeadPassCounter();
    }

    /* ************************************************* Getters ******************************************************/

    /**
     *   Getter for the value of the of passengers of the current flight that left the airport at the Arrival Terminal
     *   or at the Departure Terminal.
     *
     *    @return the value of the counter.
     */

    public int getDeadPassValue(){
        synchronized (lockDeadPassCounter) {
            return deadPassCounter;
        }
    }

    /* ************************************************* Setters ******************************************************/

    /**
     *   Sets the Departure Terminal Entrance Reference.
     *
     *    @param departureTerm Departure Terminal Entrance.
     */

    public synchronized void setDepartureTerminalRef(DepartureTerminalEntrance departureTerm){
        this.departureTerm = departureTerm;
    }
}
