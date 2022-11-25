package JuanjsleepRJ.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import JuanjsleepRJ.jsleep_android.model.BedType;
import JuanjsleepRJ.jsleep_android.model.City;

import JuanjsleepRJ.jsleep_android.model.Facility;
import JuanjsleepRJ.jsleep_android.model.Room;
import JuanjsleepRJ.jsleep_android.request.BaseApiService;
import JuanjsleepRJ.jsleep_android.request.UtilsApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreateRoomActivity extends AppCompatActivity {
    BaseApiService mApiService;
    Context mContext;
    EditText roomName, roomPrice, roomAddress, roomSize;
    CheckBox checkBoxAC, checkBoxRefrigerator, checkBoxWiFi,
    checkBoxBathtub, checkBoxBalcony, checkBoxRestaurant,
    checkBoxSwimPool, checkBoxFitCenter;
    City city;
    BedType bedType;
    ArrayList<Facility> facilities = new ArrayList<Facility>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);
        mApiService = UtilsApi.getApiService();
        mContext = this;
        roomName = findViewById(R.id.editTextRoomName);
        roomPrice = findViewById(R.id.editTextRoomPrice);
        roomAddress = findViewById(R.id.editTextRoomAddress);
        roomSize = findViewById(R.id.editTextRoomSize);

        checkBoxAC = findViewById(R.id.checkBoxAC);
        checkBoxRefrigerator = findViewById(R.id.checkBoxRefrigerator);
        checkBoxWiFi = findViewById(R.id.checkBoxWiFi);
        checkBoxBathtub = findViewById(R.id.checkBoxBathtub);
        checkBoxBalcony = findViewById(R.id.checkBoxBalcony);
        checkBoxRestaurant = findViewById(R.id.checkBoxRestaurant);
        checkBoxSwimPool = findViewById(R.id.checkBoxSwimPool);
        checkBoxFitCenter = findViewById(R.id.checkBoxFitCenter);

        Button createRoomButton = findViewById(R.id.createRoomButton);
        Button cancelCreateRoomButton = findViewById(R.id.cancelCreateRoomButton);

        Spinner spinnerCity = (Spinner) findViewById(R.id.spinnerCity);
        ArrayAdapter<City> cityList = new ArrayAdapter<City>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                City.values()
        );
        Spinner spinnerBedType = (Spinner) findViewById(R.id.spinnerBedType);
        ArrayAdapter<BedType> bedTypeList = new ArrayAdapter<BedType>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                BedType.values()
        );

        spinnerCity.setAdapter(cityList);
        spinnerBedType.setAdapter(bedTypeList);

        createRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBoxAC.isChecked())
                    facilities.add(Facility.AC);
                if(checkBoxRefrigerator.isChecked())
                    facilities.add(Facility.Refrigerator);
                if(checkBoxWiFi.isChecked())
                    facilities.add(Facility.WiFi);
                if(checkBoxBathtub.isChecked())
                    facilities.add(Facility.Bathtub);
                if(checkBoxBalcony.isChecked())
                    facilities.add(Facility.Balcony);
                if(checkBoxRestaurant.isChecked())
                    facilities.add(Facility.Restaurant);
                if(checkBoxSwimPool.isChecked())
                    facilities.add(Facility.SwimmingPool);
                if(checkBoxFitCenter.isChecked())
                    facilities.add(Facility.FitnessCenter);
                city = (City) spinnerCity.getSelectedItem();
                bedType = (BedType) spinnerBedType.getSelectedItem();

                Room room = requestCreateRoom();
            }
        });

        cancelCreateRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent move = new Intent(CreateRoomActivity.this, MainActivity.class);
                startActivity(move);
            }
        });
    }

    protected Room requestCreateRoom(){
        mApiService.requestCreateRoom(
                LoginActivity.savedAccount.id,
                roomName.getText().toString(),
                Integer.parseInt(roomSize.getText().toString()),
                Integer.parseInt(roomPrice.getText().toString()),
                this.facilities,
                this.city,
                this.bedType,
                roomAddress.getText().toString()
        ).enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {
                Room room = response.body();
                Toast.makeText(mContext, "Room successfully registered", Toast.LENGTH_SHORT).show();
                System.out.println(room);
            }

            @Override
            public void onFailure(Call<Room> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(mContext, "Create room failed!", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }
}