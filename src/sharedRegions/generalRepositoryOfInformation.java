package sharedRegions;
import entities.BusDriver;
import entities.Passenger;
import entities.Porter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Queue;

/**
 * ...
 *
 * @author Inês Justo
 * @author Miguel Lopes
 */

public class generalRepositoryOfInformation {

    /**
     *  Flight number
     */

    private int FN;

    /**
     *  Number of pieces of luggage presently at the plane's hold
     */

    private int BN;

    /*
     *   Porters
     */

    private Queue<Porter> porters;

    /*
     *   BusDrivers
     */

    private Queue<BusDriver> busDrivers;

    /*
     *   Passengers
     */

    private Queue<Passenger> passengers;


    /**
     *
     */

    private synchronized void printLog(){
        /*
        String log =
        String.format("\t\tAIRPORT RHAPSODY - Description of the internal state of the problem" +
        "\nPLANE\tPORTER\t\t\tDRIVER" +
        "\n%3d %3d\t%3d %3d %3d\t\t%3d %3d %3d %3d %3d %3d %3d %3d %3d %3d" +
        "\n\t\t\t\tPASSENGERS" +
        "\n%3d %3d %3d %3d %3d %3d %3d %3d %3d %3d %3d %3d %3d %3d %3d %3d %3d %3d %3d %3d %3d %3d %3d %3d " +
        "\nFinal report" +
        "\nN. of passengers which have this airport as their final destination = %2d" +
        "\nN. of passengers in transit = %2d" +
        "\nN. of bags that should have been transported in the the planes hold = %2d" +
        "\nN. of bags that were lost = %2d", );
         */

        /*
              AIRPORT RHAPSODY - Description of the internal state of the problem
        PLANE       PORTER          DRIVER
        FN BN       Stat CB SR      Stat Q1 Q2 Q3 Q4 Q5 Q6 S1 S2 S3
                                    PASSENGERS
        St1 Si1 NR1 NA1 St2 Si2 NR2 NA2 St3 Si3 NR3 NA3 St4 Si4 NR4 NA4 St5 Si5 NR5 NA5 St6 Si6 NR6 NA6
        ## ## #### ## ## #### # # # # # # # # #
        ### ### # # ### ### # # ### ### # # ### ### # # ### ### # # ### ### # #
        */

        /*
            Final report
            N. of passengers which have this airport as their final destination = ##
            N. of passengers in transit = ##
            N. of bags that should have been transported in the the planes hold = ##
            N. of bags that were lost = ##
        */
    }
}