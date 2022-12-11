package JuanjsleepRJ.jsleep_android.model;

import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Account class
 */
public class Account extends Serializable{
    /**
     * Account's name
     */
    public String name;
    /**
     * Account's email
     */
    public String email;
    /**
     * Account's password
     */
    public String password;
    /**
     * Account's balance
     */
    public double balance;
    /**
     * Account's {@link JuanjsleepRJ.jsleep_android.model.Renter}
     */
    public Renter renter;

    /**
     * Converts this class attributes to printable java.lang.String
     * @return String type of all attributes
     */
    @Override
    public String toString(){
        return "Account{" +
                "balance=" + balance + '\'' +
                ", email=" + email + '\'' +
                ", name=" + name + '\'' +
                ", password=" + password + '\'' +
                ", renter=" + renter + '\'' +
                ", id=" + id + '\'' +
                '}';
    }
}
