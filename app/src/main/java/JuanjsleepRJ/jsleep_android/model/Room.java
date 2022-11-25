package JuanjsleepRJ.jsleep_android.model;

import java.util.Date;
import java.util.ArrayList;

public class Room extends Serializable{
    public int accountId;
    public int size;
    public String name;
    public ArrayList<Facility> facility = new ArrayList<Facility>();
    public Price price;
    public String address;
    public City city;
    public BedType bedType;
    public ArrayList<Date> booked = new ArrayList<Date>();
}
