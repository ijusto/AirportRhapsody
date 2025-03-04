package clientSide.entities;
import clientSide.sharedRegionsStubs.ArrivalLoungeStub;
import clientSide.sharedRegionsStubs.BaggageColPointStub;
import clientSide.sharedRegionsStubs.TemporaryStorageAreaStub;
import comInf.Bag;
import comInf.PorterInterface;

/**
 * ...
 *
 * @author Inês Justo
 * @author Miguel Lopes
 */

public class Porter extends Thread{

    /**
     *  State of the porter
     *
     *    @serialField Stat
     */

    private PorterStates Stat;

    /**
     *  ...
     *
     *    @serialField arrivalLoungeStub
     */

    ArrivalLoungeStub arrivalLoungeStub;

    /**
     *  ...
     *
     *    @serialField tempStoreStub
     */

    TemporaryStorageAreaStub tempStoreStub;

    /**
     *  ...
     *
     *    @serialField bColPntStub
     */

    BaggageColPointStub bColPntStub;

    /**
     *  Instantiation of the thread Porter.
     *
     *    @param Stat state of the porter
     *    @param arrivalLoungeStub  ...
     *    @param tempStoreStub ...
     *    @param bColPntStub ....
     */

    public Porter(PorterStates Stat, ArrivalLoungeStub arrivalLoungeStub, TemporaryStorageAreaStub tempStoreStub,
                  BaggageColPointStub bColPntStub){
        super ("Porter");
        this.Stat = Stat;
        this.arrivalLoungeStub = arrivalLoungeStub;
        this.tempStoreStub = tempStoreStub;
        this.bColPntStub = bColPntStub;
    }

    /**
     *  Life cycle of the thread Porter.
     */

    @Override
    public void run(){
        Bag bag;
        boolean planeHoldEmpty;

        while ( arrivalLoungeStub.takeARest() != 'E' ){	// 'E' character return means end of state
            planeHoldEmpty = false;
            while (!planeHoldEmpty) {
                bag = arrivalLoungeStub.tryToCollectABag();
                if (bag == null) {
                    planeHoldEmpty = true;
                } else if (bag.getDestStat() == Bag.DestStat.TRANSIT){
                    tempStoreStub.carryItToAppropriateStore(bag);
                } else {
                    bColPntStub.carryItToAppropriateStore(bag);
                }
            }
            arrivalLoungeStub.noMoreBagsToCollect();
        }
    }

    /* ******************************************** Getters and Setters ***********************************************/

    /**
     * Get Porter State
     */


    public PorterStates getStatPorter() {
        return this.Stat;
    }

    /**
     * Set Porter State
     */


    public void setStatPorter(PorterStates stat) {
        this.Stat = stat;
    }

}
