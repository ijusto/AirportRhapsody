package entities;

/**
 * ...
 *
 * @author Inês Justo
 * @author Miguel Lopes
 */

public class Bag {

    public enum DestStat { TRANSIT, FINAL };

    private DestStat destStat;

    private int idOwner;

    public Bag(DestStat destStat, int idOwner){
        this.destStat = destStat;
        this.idOwner = idOwner;
    }

    public DestStat getDestStat(){
        return destStat;
    }

    public int getIdOwner() {
        return idOwner;
    }
}
