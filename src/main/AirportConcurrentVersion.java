package main;
import commonInfrastructures.MemException;
import sharedRegions.*;
import genclass.GenericIO;

/**
 *  Main program.
 *
 *  @author Inês Justo
 *  @author Miguel Lopes
 */

public class AirportConcurrentVersion {

    /**
     * Minimum milliseconds to wakeup.
     * Affects ...(see functions)
     */
    public static final int minSleep = 5;

    /**
     * Maximum milliseconds to wakeup.
     * Affects ...(see functions)
     */
    public static final int maxSleep = 10;

    private static final int maxBags4Passenger = 2;

    public static void main(final String[] args) throws MemException {

        GenReposInfo repos;
        BaggageColPoint bagColPoint;
        BaggageReclaimOffice bagRecOffice;
        ArrivalLounge arrivLounge;
        ArrivalTermTransfQuay arrivalQuay;
        DepartureTermTransfQuay departureQuay;
        ArrivalTerminalExit arrivalTerm;
        DepartureTerminalEntrance departureTerm;

        String fileName;
        char[][] destStat;
        int[][] nBags;
        int[][] nBagsPHold;
        char opt;
        boolean success;

        do {
            /* ... */
            {
                do {
                    GenericIO.writeString("There is already a file with this name. Delete it (y - yes; n - no)? ");
                    opt = GenericIO.readlnChar();
                } while( (opt != 'y') && (opt != 'n'));
            } else {
                success = true;
            }
        } while(success);

        for(int land = 0; land < SimulationParameters.K; land++){
            for(int nPass = 0; nPass < SimulationParameters.N; nPass++){
                destStat[nPass][land] = (Math.random() < 0.4) ? 'F' : 'T';
                nBags[nPass][land] = (Math.random() < 0.5) ? 2 : (Math.random() < 0.5) ? 1 : 0;
                nBagsPHold[nPass][land] = nBags[nPass][land];
                if(nBagsPHold[nPass][land] > 0){
                    if(Math.random() < 0.2){
                        nBagsPHold[nPass][land] -= 1;
                        if( (nBagsPHold[nPass][land] > 0) && (Math.random() < 0.2)){
                            nBagsPHold[nPass][land] -= 1;
                        }
                    }
                }
            }
        }

        /*
         *  instantiation of the shared regions
         */
        repos = new GenReposInfo(fileName);
        bagColPoint = new BaggageColPoint(repos);
        bagRecOffice = new BaggageReclaimOffice(repos);
        arrivLounge = new ArrivalLounge(destStat, nBagsPHold, bagColPoint, repos);
        arrivalQuay = new ArrivalTermTransfQuay(repos);
        departureQuay = new DepartureTermTransfQuay(repos);
        arrivalTerm = new ArrivalTerminalExit(arrivLounge, arrivalQuay, repos);
        departureTerm = new DepartureTerminalEntrance(arrivLounge, arrivalQuay, repos);
        arrivalTerm.setDepartureTerminalRef(departureTerm);
        departureTerm.setArrivalTerminalRef(arrivalTerm);
        arrivLounge.setArrivalTerminalRef(arrivalTerm);
        bagColPoint.setArrivalTerminalRef(arrivalTerm);
        bagRecOffice.setArrivalTerminalRef(arrivalTerm);


    }
}
