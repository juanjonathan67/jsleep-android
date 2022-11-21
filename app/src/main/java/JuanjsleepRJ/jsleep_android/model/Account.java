package JuanjsleepRJ.jsleep_android.model;

import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Account extends Serializable{
    public String name;
    public String email;
    public String password;
    public double balance;
    public Renter renter;

    @Override
    public String toString(){
        return "Account{" +
                "balance=" + balance + '\'' +
                ", email=" + email + '\'' +
                ", name=" + name + '\'' +
                ", password=" + password + '\'' +
                ", renter=" + renter +
                '}';
    }
}
