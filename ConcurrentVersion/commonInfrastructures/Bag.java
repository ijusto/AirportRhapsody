package commonInfrastructures;

/**
 *   Bag belonging to a passenger.
 *
 *    @author Inês Justo
 *    @author Miguel Lopes
 */

public class Bag {

    /**
     *   Enum containing the type of destination a bag can have.
     */

    public enum DestStat { TRANSIT, FINAL }

    /**
     *   Destination of the bag.
     */

    private DestStat destStat;

    /**
     *   Id of the owner of the bag.
     */

    private int idOwner;

    /**
     *   Instantiation of the class Bag.
     *
     *    @param destStat bag's destination.
     *    @param idOwner bag's owner's id.
     */

    public Bag(DestStat destStat, int idOwner){
        this.destStat = destStat;
        this.idOwner = idOwner;
    }

    /**
     *   Gets the destination of the bag.
     *    @return bag's destination.
     */

    public DestStat getDestStat(){
        return destStat;
    }

    /**
     *   Gets the id of the passenger owner of the bag.
     *    @return owner's id.
     */

    public int getIdOwner() {
        return idOwner;
    }
}
