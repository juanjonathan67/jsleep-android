package JuanjsleepRJ.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import JuanjsleepRJ.jsleep_android.model.Account;
import JuanjsleepRJ.jsleep_android.request.BaseApiService;
import JuanjsleepRJ.jsleep_android.request.UtilsApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity which controls login activity
 * @author juanjonathan67
 * @version 1.0.0
 */
public class LoginActivity extends AppCompatActivity{
    /**
     * {@link JuanjsleepRJ.jsleep_android.request.BaseApiService} attribute
     */
    BaseApiService mApiService;
    /**
     * Editable text view for entering username
     */
    EditText username;
    /**
     * Editable text view for entering password
     */
    EditText password;
    /**
     * Context of activity
     */
    Context mContext;
    /**
     * {@link JuanjsleepRJ.jsleep_android.model.Account} used for login
     */
    public static Account savedAccount;

    /**
     * Create activity
     * @param savedInstanceState Instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mApiService = UtilsApi.getApiService();
        mContext = this;
        Button loginButton = findViewById(R.id.LoginButton);
        TextView register = findViewById(R.id.RegisterButton);

        username = findViewById(R.id.editTextUsername);
        password = findViewById(R.id.editTextPassword);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Account account = requestLogin();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent move = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(move);
            }
        });
    }

    /**
     * Method used to make GET request with implemented method {@link JuanjsleepRJ.jsleep_android.request.BaseApiService#requestLogin(String, String)}.
     * Checks if email and password are correct, then saves the account at {@link JuanjsleepRJ.jsleep_android.LoginActivity#savedAccount}.
     * @return null
     */
    protected Account requestLogin(){
        mApiService.requestLogin(username.getText().toString(), password.getText().toString()).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                savedAccount = response.body();
                Intent move = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(move);
                Toast.makeText(LoginActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();
                System.out.println(savedAccount.toString());

            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(mContext, "Password atau email salah", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }

}