package JuanjsleepRJ.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
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

public class AboutMeActivity extends AppCompatActivity {
    BaseApiService mApiService;
    EditText email, address, phoneNumber;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        mApiService = UtilsApi.getApiService();
        mContext = this;

        LinearLayout renterForm = (LinearLayout) findViewById(R.id.registerFormLayout);
        renterForm.setVisibility(View.INVISIBLE);
        Button registerRenterButton = (Button) findViewById(R.id.registerRenterButton);
        registerRenterButton.setVisibility(View.INVISIBLE);
        Button registerFormButton = (Button) findViewById(R.id.registerFormButton);
        registerFormButton.setVisibility(View.INVISIBLE);
        Button cancelFormButton = (Button) findViewById(R.id.cancelFormButton);
        cancelFormButton.setVisibility(View.INVISIBLE);
        RelativeLayout renterNotNull = (RelativeLayout) findViewById(R.id.RenterNotNull);
        renterNotNull.setVisibility(View.INVISIBLE);
        TextView renterName = (TextView) findViewById(R.id.textViewRenterName2);
        TextView renterAddress = (TextView) findViewById(R.id.textViewRenterAddress2);
        TextView renterPhone = (TextView) findViewById(R.id.textViewRenterPhone2);

        email = findViewById(R.id.editTextRenterName);
        address = findViewById(R.id.editTextRenterAddress);
        phoneNumber = findViewById(R.id.editTextRenterPhone);

        if(LoginActivity.savedAccount.renter == null){
            registerRenterButton.setVisibility(View.VISIBLE);
            registerRenterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    registerRenterButton.setVisibility(View.INVISIBLE);
                    renterForm.setVisibility(View.VISIBLE);
                    registerFormButton.setVisibility(View.VISIBLE);
                    cancelFormButton.setVisibility(View.VISIBLE);
                }
            });

            registerFormButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Renter renter = requestRegisterRenter();
                }
            });

            cancelFormButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    registerRenterButton.setVisibility(View.VISIBLE);
                    renterForm.setVisibility(View.INVISIBLE);
                    registerFormButton.setVisibility(View.INVISIBLE);
                    cancelFormButton.setVisibility(View.INVISIBLE);
                }
            });
        }else{
            renterName.setText(LoginActivity.savedAccount.renter.username);
            renterAddress.setText(LoginActivity.savedAccount.renter.address);
            renterPhone.setText(LoginActivity.savedAccount.renter.phoneNumber);
            renterNotNull.setVisibility(View.VISIBLE);
        }
        TextView acc_name = findViewById(R.id.account_name_2);
        acc_name.setText(LoginActivity.savedAccount.name);
        TextView acc_email = findViewById(R.id.account_email);
        acc_email.setText(LoginActivity.savedAccount.email);
        TextView acc_balance = findViewById(R.id.account_balance);
        String bal = String.valueOf(LoginActivity.savedAccount.balance);
        acc_balance.setText(bal);
    }

//    protected Account requestRegisterRenter(){
//        mApiService.requestRegisterRenter(
//                LoginActivity.savedAccount.id,
//                email.getText().toString(),
//                address.getText().toString(),
//                phoneNumber.getText().toString()
//        ).enqueue(new Callback<Account>() {
//            @Override
//            public void onResponse(Call<Account> call, Response<Account> response) {
//                Account account = response.body();
//                System.out.println(account);
//                Toast.makeText(mContext, "Renter successfully registered!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Call<Account> call, Throwable t) {
//                t.printStackTrace();
//                Toast.makeText(mContext, "Register renter failed", Toast.LENGTH_SHORT).show();
//            }
//        });
//        return null;
//    }
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
            }

            @Override
            public void onFailure(Call<Renter> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(mContext, "Register renter failed", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }
}