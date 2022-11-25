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

public class LoginActivity extends AppCompatActivity{
    BaseApiService mApiService;
    EditText username, password;
    Context mContext;

    public static Account savedAccount;

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

//    protected Account requestAccount(){
//        mApiService.getAccount(0).enqueue(new Callback<Account>(){
//            @Override
//            public void onResponse(Call<Account> call, Response<Account> response){
//                if(response.isSuccessful()){
//                    Account account;
//                    account = response.body();
//                    System.out.println(account.toString());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Account> call, Throwable t){
//                Toast.makeText(mContext, "no account id=0", Toast.LENGTH_SHORT).show();
//            }
//        });
//        return null;
//    }

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
                Toast.makeText(mContext, "Password atau email salah", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }

}