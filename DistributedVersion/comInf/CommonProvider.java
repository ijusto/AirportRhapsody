package comInf;


import serverSide.ServerCom;
import clientSide.entities.*;


public class CommonProvider extends Thread implements PassengerInterface, PorterInterface, BusDriverInterface{

    private BusDriverStates busStates;

    private int nPass;

    private Passenger.SiPass[] si = new Passenger.SiPass[SimulPar.N_PASS_PER_FLIGHT];

    private PassengerStates[] st = new PassengerStates[SimulPar.N_PASS_PER_FLIGHT];

    private int[] NA = new int[SimulPar.N_PASS_PER_FLIGHT];

    private int[] NR = new int[SimulPar.N_PASS_PER_FLIGHT];

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
    public void setBDStat(BusDriverStates stat) {
        this.busStates = stat;
    }

    @Override
    public void setNPassOnTheBus(int nPassOnTheBus) {
        this.nPass = nPassOnTheBus;
    }

    @Override
    public BusDriverStates getBDStat() {
        return busStates;
    }

    /********************************* PASSENGER ****************************************/

    @Override
    public Passenger.SiPass getSi(int id) {
        return si[id];
    }

    @Override
    public PassengerStates getPassStat(int id) { return st[id]; }

    @Override
    public int getNA(int id) {
        return NA[id];
    }

    @Override
    public int getNR(int id) {
        return NR[id];
    }

    @Override
    public void setStatPass(int id, PassengerStates st) {
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
