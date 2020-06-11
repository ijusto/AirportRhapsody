package comInf;

import clientSide.entities.BusDriverStates;

public interface BusDriverInterface {

    public BusDriverStates getStat();


    /**
     *   Gets the number os passengers on the bus.
     *
     *    @return number os passengers on the bus.
     */

    public int getNPassOnTheBus();

    /* ************************************************* Setters ******************************************************/

    /**
     *   Sets the Bus Driver State.
     *
     *    @param stat Bus Driver State.
     */

    public void setStat(BusDriverStates stat);

    /**
     *   Sets the Number os passengers on the bus.
     *
     *    @param nPassOnTheBus Number os passengers on the bus.
     */

    public void setNPassOnTheBus(int nPassOnTheBus);
}
