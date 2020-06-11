package comInf;

import clientSide.entities.Passenger;
import clientSide.entities.PassengerStates;

public interface PassengerInterface {
    /**
     * Get Passenger Situation
     */

    public Passenger.SiPass getSi();


    /**
     * Get Passenger State
     */

    public PassengerStates getSt();

    /**
     * Get Passenger number of pieces of luggage he has presently collected
     */

    public int getNA();

    /**
     * Get Passenger number of pieces of luggage he carried at the start of the journey
     */

    public int getNR();

    /**
     * Get Passenger ID
     */

    public int getPassengerID();

    /**
     * Set Passenger State
     */

    public void setSt(PassengerStates st);

    /**
     * Set Passenger number of pieces of luggage he has presently collected
     */

    public void setNA(int NA);

}

