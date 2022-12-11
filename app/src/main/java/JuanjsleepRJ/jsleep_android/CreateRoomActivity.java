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

/**
 * Activity which controls creating rooms
 * @author juanjonathan67
 * @version 1.0.0
 */
public class CreateRoomActivity extends AppCompatActivity {
    /**
     * {@link JuanjsleepRJ.jsleep_android.request.BaseApiService} attribute
     */
    BaseApiService mApiService;
    /**
     * Context of activity
     */
    Context mContext;
    /**
     * Editable text view for the room details to be created
     */
    EditText roomName, roomPrice, roomAddress, roomSize;
    /**
     * Checkboxes for the room's {@link JuanjsleepRJ.jsleep_android.model.Facility}
     */
    CheckBox checkBoxAC, checkBoxRefrigerator, checkBoxWiFi,
    checkBoxBathtub, checkBoxBalcony, checkBoxRestaurant,
    checkBoxSwimPool, checkBoxFitCenter;
    /**
     * The {@link JuanjsleepRJ.jsleep_android.model.City} of the room
     */
    City city;
    /**
     * The {@link JuanjsleepRJ.jsleep_android.model.BedType} of the room
     */
    BedType bedType;

    /**
     * Create activity
     * @param savedInstanceState Instance state
     */
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
            ArrayList<Facility> facilities = new ArrayList<>();
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

                requestCreateRoom(facilities);
            }
        });
    }

    /**
     * Method used to make POST request with implemented method {@link JuanjsleepRJ.jsleep_android.request.BaseApiService#requestCreateRoom(int, String, int, int, ArrayList, City, BedType, String)}.
     * @param facilities List of {@link JuanjsleepRJ.jsleep_android.model.Facility}  of the room
     * @return null
     */
    protected Room requestCreateRoom(ArrayList<Facility> facilities){
        mApiService.requestCreateRoom(
                LoginActivity.savedAccount.renter.id,
                roomName.getText().toString(),
                Integer.parseInt(roomSize.getText().toString()),
                Integer.parseInt(roomPrice.getText().toString()),
                facilities,
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

    /**
     * Method to destroy activity when back button is pressed and return to {@link JuanjsleepRJ.jsleep_android.MainActivity}.
     */
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CreateRoomActivity.this, MainActivity.class));
        finish();
    }
}