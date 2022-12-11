package JuanjsleepRJ.jsleep_android.model;

/**
 * Payment class
 */
public class Payment extends Invoice{
    /**
     * Id of booked room
     */
    private int roomId;

    /**
     * Getter method to get room Id
     * @return The room's id
     */
    public int getRoomId(){
        return this.roomId;
    }
}
