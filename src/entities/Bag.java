package entities;

/**
 * ...
 *
 * @author Inês Justo
 * @author Miguel Lopes
 */

public class Bag {

    public enum DestStat { TRANSIT, FINAL };

    private static final char TRANSIT = 'T';  // in transit
    private static final char FINAL = 'F';  // final

    private DestStat destStat;

    private int idOwner;

    public Bag(DestStat destStat, int idOwner){
        this.destStat = destStat;
        this.idOwner = idOwner;
    }

    public DestStat getDestStat(){
        return destStat; // 'T' means transit, 'F' means final
    }

    public int getIdOwner() {
        return idOwner;
    }
}
