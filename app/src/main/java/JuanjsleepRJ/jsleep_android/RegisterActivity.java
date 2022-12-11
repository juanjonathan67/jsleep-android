package JuanjsleepRJ.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import JuanjsleepRJ.jsleep_android.model.Account;
import JuanjsleepRJ.jsleep_android.request.BaseApiService;
import JuanjsleepRJ.jsleep_android.request.UtilsApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity which handles newly registered account.
 * @author juanjonathan67
 * @version 1.0.0
 */
public class RegisterActivity extends AppCompatActivity {
    /**
     * {@link JuanjsleepRJ.jsleep_android.request.BaseApiService} attribute
     */
    BaseApiService mApiService;
    /**
     * Editable text view for renter's information
     */
    EditText username, email, password;
    /**
     * Context of activity
     */
    Context mContext;

    /**
     * Create activity
     * @param savedInstanceState Instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mApiService = UtilsApi.getApiService();
        mContext = this;
        Button registerConfirmButton = findViewById(R.id.RegisterConfirmButton);

        username = findViewById(R.id.editTextRegisterName);
        email = findViewById(R.id.editTextRegisterEmail);
        password = findViewById(R.id.editTextRegisterPassword);

        registerConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestRegister();
            }
        });
    }

    /**
     * Method used to make POST request with implemented method {@link JuanjsleepRJ.jsleep_android.request.BaseApiService#requestRegister(String, String, String)}.
     * Creates a new {@link JuanjsleepRJ.jsleep_android.model.Account} if it passes the REGEX
     */
    protected void requestRegister(){
        mApiService.requestRegister(
                username.getText().toString(),
                email.getText().toString(),
                password.getText().toString()
        ).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                Account account = response.body();
                System.out.println(account);
                Toast.makeText(mContext, "Account successfully registered!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
//                t.printStackTrace();
                Toast.makeText(mContext, "Account already registered!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    /**
     * Method used to destroy activity when back is pressed and goes back to {@link JuanjsleepRJ.jsleep_android.LoginActivity}.
     */
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }
}