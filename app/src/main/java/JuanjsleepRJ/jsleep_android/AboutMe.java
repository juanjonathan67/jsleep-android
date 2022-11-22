package JuanjsleepRJ.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class AboutMe extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        TextView acc_name = findViewById(R.id.account_name_2);
        acc_name.setText(LoginActivity.savedAccount.name);
        TextView acc_email = findViewById(R.id.account_email);
        acc_email.setText(LoginActivity.savedAccount.email);
        TextView acc_balance = findViewById(R.id.account_balance);
        String bal = String.valueOf(LoginActivity.savedAccount.balance);
        acc_balance.setText(bal);
    }
}