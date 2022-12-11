package JuanjsleepRJ.jsleep_android.model;

/**
 * Invoice class
 */
public class Invoice extends Serializable{
    /**
     * Buyer's Id
     */
    public int buyerId;
    /**
     * Renter's Id
     */
    public int renterId;

    /**
     * Enum of Room Ratings
     */
    public enum RoomRating{
        NONE,
        BAD,
        NEUTRAL,
        GOOD;
    }

    /**
     * Enum of Payment Statuses
     */
    public enum PaymentStatus{
        FAILED,
        WAITING,
        SUCCESS;
    }

    /**
     * The Invoice's payment status
     */
    public PaymentStatus status = PaymentStatus.WAITING;
    /**
     * The Room's rating
     */
    public RoomRating rating = RoomRating.NONE;
}
