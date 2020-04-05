package entities;

/**
 *   Bag belonging to a passenger.
 *
 *    @author Inês Justo
 *    @author Miguel Lopes
 */

public class Bag {

    /**
     *
     */

    public enum DestStat { TRANSIT, FINAL };

    /**
     *
     */

    private DestStat destStat;

    /**
     *
     */

    private int idOwner;

    /**
     *   Instantiation of the class Bag.
     *
     *    @param destStat
     *    @param idOwner
     */

    public Bag(DestStat destStat, int idOwner){
        this.destStat = destStat;
        this.idOwner = idOwner;
    }

    /**
     *
     *    @return
     */

    public DestStat getDestStat(){
        return destStat;
    }

    /**
     *
     *    @return
     */

    public int getIdOwner() {
        return idOwner;
    }
}
