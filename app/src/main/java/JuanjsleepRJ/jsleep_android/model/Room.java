package JuanjsleepRJ.jsleep_android.model;

import java.util.Date;
import java.util.ArrayList;

/**
 * Room class
 */
public class Room extends Serializable{
    /**
     * Room's renter Id
     */
    public int accountId;
    /**
     * Room's size
     */
    public int size;
    /**
     * Room's name
     */
    public String name;
    /**
     * Room's List of facilities
     */
    public ArrayList<Facility> facility;
    /**
     * Room's {@link JuanjsleepRJ.jsleep_android.model.Price}
     */
    public Price price;
    /**
     * Room's address
     */
    public String address;
    /**
     * Room's {@link JuanjsleepRJ.jsleep_android.model.City}
     */
    public City city;
    /**
     * Room's {@link JuanjsleepRJ.jsleep_android.model.BedType}
     */
    public BedType bedType;
    /**
     * Room's booked dates
     */
    public ArrayList<Date> booked;
}
