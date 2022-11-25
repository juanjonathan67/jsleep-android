package JuanjsleepRJ.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

public class RegisterActivity extends AppCompatActivity {
    BaseApiService mApiService;
    EditText username, email, password;
    Context mContext;

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
                Account account = requestRegister();
//                Toast.makeText(LoginActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected Account requestRegister(){
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
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
//                t.printStackTrace();
                Toast.makeText(mContext, "Account already registered!", Toast.LENGTH_SHORT).show();

            }
        });
        return null;
    }
}