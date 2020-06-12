package comInf;


import serverSide.ServerCom;
import clientSide.entities.*;


public class CommonProvider extends Thread implements PassengerInterface, PorterInterface, BusDriverInterface{

    private BusDriverStates busStates;

    private int nPass;

    private Passenger.SiPass[] si;

    private PassengerStates[] st;

    private int[] NA;

    private int[] NR;

    private int[] id;

    private PorterStates pSt;

    public CommonProvider(String s) {
        super(s);
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
    public Passenger.SiPass getSi(int id) {
        return si[id];
    }

    @Override
    public PassengerStates getSt(int id) { return st[id]; }

    @Override
    public int getNA(int id) {
        return NA[id];
    }

    @Override
    public int getNR(int id) {
        return NR[id];
    }

    @Override
    public void setSt(int id, PassengerStates st) {
        this.st[id] = st;
    }

    @Override
    public void setNA(int id, int NA) {
        this.NA[id] = NA;
    }

    @Override
    public void setSi(int id, Passenger.SiPass si) { this.si[id] = si; }

    @Override
    public void setNR(int id, int NR) { this.NR[id] = NR; }

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
