package entities;

public class Bag {

    private static final char TRANSIT = 'T';  // in transit
    private static final char FINAL = 'F';  // final

    private char destStat;

    public void setDestStat(char destStat) {
        this.destStat = destStat;
    }

    public char getDestStat(){
        return destStat; // 'T' means transit, 'F' means final
    }
}
