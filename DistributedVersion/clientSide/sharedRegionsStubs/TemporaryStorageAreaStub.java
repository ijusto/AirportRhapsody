package clientSide.sharedRegionsStubs;

import clientSide.PorterStates;
import clientSide.SimulPar;
import clientSide.entities.Porter;
import comInf.Bag;
import comInf.MemException;
import comInf.MemStack;

public class TemporaryStorageAreaStub {

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

    public TemporaryStorageAreaStub (String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     *   Operation of carrying a bag from the plane's hold to the temporary storage area (raised by the Porter).
     */

    public void carryItToAppropriateStore(Bag bag){
        Porter porter = (Porter) Thread.currentThread();
        assert porter.getStat() == PorterStates.AT_THE_PLANES_HOLD;
        assert bag != null;
        porter.setStat(PorterStates.AT_THE_STOREROOM);
        repos.updatePorterStat(PorterStates.AT_THE_STOREROOM);

        try {
            tmpStorageStack.write(bag);
            repos.saveBagInSR();
        } catch (MemException e) {
            e.printStackTrace();
        }

        repos.printLog();
    }

    /**
     *   Resets the stack of bags when all passengers from a flight leave the airport.
     */

    public synchronized void resetTemporaryStorageArea() {
        // stack of the store room instantiation
        try {
            tmpStorageStack = new MemStack<>(new Bag [SimulPar.N_PASS_PER_FLIGHT * SimulPar.N_BAGS_PER_PASS]);
        } catch (MemException e) {
            e.printStackTrace();
        }
    }
}
