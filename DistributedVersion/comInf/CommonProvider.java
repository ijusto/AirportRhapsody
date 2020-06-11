package comInf;


import serverSide.ServerCom;
import clientSide.entities.*;
import serverSide.proxies.SharedRegionProxy;


public class CommonProvider extends Thread implements PassengerInterface, PorterInterface, BusDriverInterface{

    private SharedRegionProxy sharedRegion;

    private ServerCom serverCom;

    private BusDriverStates busStates;

    private int nPass;

    private Passenger.SiPass si;

    private PassengerStates st;

    private int NA;

    private int NR;

    private int id;

    private PorterStates pSt;

    public CommonProvider(SharedRegionProxy sharedRegion, ServerCom serverCom) {
        this.sharedRegion = sharedRegion;
        this.serverCom = serverCom;
    }

    /**
     * Waits for message and sends it to shared region to be processed
     */
    @Override
    public void run() {
        Message msg = (Message) serverCom.readObject();
        try {
            msg = sharedRegion.processAndReply(msg);
        } catch (MessageException e) {
            e.printStackTrace();
        }
        serverCom.writeObject(msg);
        serverCom.close();
    }


    /*********************************BUS DRIVER****************************************/
    @Override
    public int getNPassOnTheBus() {
        return nPass;
    }

    @Override
    public void setStat(BusDriverStates stat) {
        this.busStates = stat;
    }

    @Override
    public void setNPassOnTheBus(int nPassOnTheBus) {
        this.nPass = nPassOnTheBus;
    }

    @Override
    public BusDriverStates getStat() {
        return busStates;
    }

    /********************************* PASSENGER ****************************************/

    @Override
    public Passenger.SiPass getSi() {
        return si;
    }

    @Override
    public PassengerStates getSt() {
        return st;
    }

    @Override
    public int getNA() {
        return NA;
    }

    @Override
    public int getNR() {
        return NR;
    }

    @Override
    public int getPassengerID() {
        return id;
    }

    @Override
    public void setSt(PassengerStates st) {
        this.st = st;
    }

    @Override
    public void setNA(int NA) {
        this.NA = NA;
    }

    /*********************************PORTER****************************************/



    @Override
    public void setStatPorter(PorterStates stat) {
        this.pSt = stat;
    }

    @Override
    public PorterStates getStatPorter() {
        return pSt;
    }
}
