package JuanjsleepRJ.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import JuanjsleepRJ.jsleep_android.model.Account;
import JuanjsleepRJ.jsleep_android.model.Renter;
import JuanjsleepRJ.jsleep_android.request.BaseApiService;
import JuanjsleepRJ.jsleep_android.request.UtilsApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity which contains information of the account used when logging in, as well as implementing related methods such as top up and register renter.
 * @author juanjonathan67
 * @version 1.0.0
 */
public class AboutMeActivity extends AppCompatActivity {
    /**
     * {@link JuanjsleepRJ.jsleep_android.request.BaseApiService} attribute
     */
    BaseApiService mApiService;
    /**
     * Editable text view for renter's email
     */
    EditText email;
    /**
     * Editable text view for renter's address
     */
    EditText address;
    /**
     * Editable text view for renter's phone number
     */
    EditText phoneNumber;
    /**
     * Editable text view for amount of money to top up
     */
    EditText amount;
    /**
     * Context of activity
     */
    Context mContext;
    /**
     * Card view for registering new renter
     */
    CardView renterCardView;
    /**
     * Card view for renter information
     */
    CardView renterNotNullCardView;

    /**
     * Create activity
     * @param savedInstanceState Instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        mApiService = UtilsApi.getApiService();
        mContext = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        renterCardView = findViewById(R.id.RenterLayout);
        renterNotNullCardView = findViewById(R.id.RenterNotNull);

        Button registerRenterButton = findViewById(R.id.registerRenterButton);
        Button registerFormButton = findViewById(R.id.registerFormButton);
        Button topUp = findViewById(R.id.top_up);

        registerRenterButton.setVisibility(View.INVISIBLE);
        TextView renterName = findViewById(R.id.textViewRenterName2);
        TextView renterAddress = findViewById(R.id.textViewRenterAddress2);
        TextView renterPhone = findViewById(R.id.textViewRenterPhone2);

        email = findViewById(R.id.editTextRenterName);
        address = findViewById(R.id.editTextRenterAddress);
        phoneNumber = findViewById(R.id.editTextRenterPhone);
        amount = findViewById(R.id.top_up_amount);

        if(LoginActivity.savedAccount.renter == null){
            renterNotNullCardView.setVisibility(View.GONE);
            renterCardView.setVisibility(View.GONE);
            registerFormButton.setVisibility(View.GONE);
            registerRenterButton.setVisibility(View.VISIBLE);
            registerRenterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    renterCardView.setVisibility(View.VISIBLE);
                    registerRenterButton.setVisibility(View.INVISIBLE);
                    registerFormButton.setVisibility(View.VISIBLE);
                }
            });

            registerFormButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestRegisterRenter();
                }
            });
        }else{
            renterCardView.setVisibility(View.GONE);
            registerFormButton.setVisibility(View.GONE);
            renterName.setText(LoginActivity.savedAccount.renter.username);
            renterAddress.setText(LoginActivity.savedAccount.renter.address);
            renterPhone.setText(LoginActivity.savedAccount.renter.phoneNumber);
            renterNotNullCardView.setVisibility(View.VISIBLE);
        }
        TextView acc_name = findViewById(R.id.account_name_2);
        acc_name.setText(LoginActivity.savedAccount.name);
        TextView acc_email = findViewById(R.id.account_email);
        acc_email.setText(LoginActivity.savedAccount.email);
        TextView acc_balance = findViewById(R.id.account_balance);
        String bal = String.valueOf(LoginActivity.savedAccount.balance);
        acc_balance.setText(bal);

        topUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestTopUp();
            }
        });
    }

    /**
     * Method used to make POST request with implemented method {@link JuanjsleepRJ.jsleep_android.request.BaseApiService#requestRegisterRenter(int, String, String, String)}.
     * Registers a new renter on the current account.
     * @return null
     */
    protected Renter requestRegisterRenter(){
        mApiService.requestRegisterRenter(
                LoginActivity.savedAccount.id,
                email.getText().toString(),
                address.getText().toString(),
                phoneNumber.getText().toString()
        ).enqueue(new Callback<Renter>() {
            @Override
            public void onResponse(Call<Renter> call, Response<Renter> response) {
                Renter renter = response.body();
                System.out.println(renter);
                Toast.makeText(mContext, "Renter successfully registered!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AboutMeActivity.this, MainActivity.class));
            }

            @Override
            public void onFailure(Call<Renter> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(mContext, "Register renter failed", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }

    /**
     * Method used to make POST request with implemented method {@link JuanjsleepRJ.jsleep_android.request.BaseApiService#requestTopUp(int, double)}.
     * Adds amount to the balance of {@link JuanjsleepRJ.jsleep_android.LoginActivity#savedAccount}.
     * @return null
     */
    protected Boolean requestTopUp(){
        mApiService.requestTopUp(
                LoginActivity.savedAccount.id,
                Double.parseDouble(amount.getText().toString())
        ).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Boolean bool = response.body();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return null;
    }

    /**
     * Method used to destroy activity when back is pressed and goes back to {@link JuanjsleepRJ.jsleep_android.MainActivity}.
     */
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AboutMeActivity.this, MainActivity.class));
        finish();
    }
}