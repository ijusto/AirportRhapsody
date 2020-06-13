package comInf;

import clientSide.entities.Passenger;
import clientSide.entities.PassengerStates;

public interface PassengerInterface {
    /**
     * Get Passenger Situation
     */

    public Passenger.SiPass getSi(int id);


    /**
     * Get Passenger State
     */

    public PassengerStates getPassStat(int id);

    /**
     * Get Passenger number of pieces of luggage he has presently collected
     */

    public int getNA(int id);

    /**
     * Get Passenger number of pieces of luggage he carried at the start of the journey
     */

    public int getNR(int id);

    /**
     * Set Passenger State
     */

    public void setStatPass(int id, PassengerStates st);

    /**
     * Set Passenger number of pieces of luggage he has presently collected
     */

    public void setNA(int id, int NA);

    public void setSi(int id, Passenger.SiPass si);

    public void setNR(int id, int NR);
}

