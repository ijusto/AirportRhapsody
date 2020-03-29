package entities;

/**
 * ...
 *
 * @author Inês Justo
 * @author Miguel Lopes
 */

public class Bag {

    private static final char TRANSIT = 'T';  // in transit
    private static final char FINAL = 'F';  // final

    private char destStat;

    private int idOwner;

    public Bag(char destStat, int idOwner){
        this.destStat = destStat;
        this.idOwner = idOwner;
    }

    public void setDestStat(char destStat) {
        this.destStat = destStat;
    }

    public char getDestStat(){
        return destStat; // 'T' means transit, 'F' means final
    }

    public int getIdOwner() {
        return idOwner;
    }
}
