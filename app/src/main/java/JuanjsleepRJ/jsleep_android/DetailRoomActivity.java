package JuanjsleepRJ.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import JuanjsleepRJ.jsleep_android.model.Facility;
import JuanjsleepRJ.jsleep_android.model.Room;

/**
 * Activity which displays room details
 * @author juanjonathan67
 * @version 1.0.0
 */
public class DetailRoomActivity extends AppCompatActivity {
    /**
     * Text view to display room details
     */
    TextView name, bedType, size, price, address, city;
    /**
     * Checkboxes for the room's {@link JuanjsleepRJ.jsleep_android.model.Facility}
     */
    CheckBox AC, Balcony, Bathtub, FitnessCenter, Refrigerator,
    Restaurant, SwimmingPool, WiFi;
    /**
     * Button to book room
     */
    Button book;

    /**
     * Create activity
     * @param savedInstanceState Instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_room);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.textViewSelectedRoomName);
        bedType = findViewById(R.id.textViewSelectedRoomBedType);
        size = findViewById(R.id.textViewSelectedRoomSize);
        price = findViewById(R.id.textViewSelectedRoomPrice);
        address = findViewById(R.id.textViewSelectedRoomAddress);
        city = findViewById(R.id.textViewSelectedRoomCity);
        AC = findViewById(R.id.hasAC);
        Balcony = findViewById(R.id.hasBalcony);
        Bathtub = findViewById(R.id.hasBathtub);
        FitnessCenter = findViewById(R.id.hasFitness);
        Refrigerator = findViewById(R.id.hasRefrigerator);
        Restaurant = findViewById(R.id.hasRestaurant);
        SwimmingPool = findViewById(R.id.hasPool);
        WiFi = findViewById(R.id.hasWiFi);
        book = findViewById(R.id.bookButton);
        int index;

        List<Room> currentRoomList;

        if(MainActivity.isRenter) {
            index = YourRoomsActivity.rentedRoomIndex;
            currentRoomList = YourRoomsActivity.rentedRoomList;
            book.setVisibility(View.INVISIBLE);
        } else {
            index = MainActivity.roomIndex;
            currentRoomList = MainActivity.roomPaginatedList;
        }

        name.setText(currentRoomList.get(index).name);
        bedType.setText(currentRoomList.get(index).bedType.toString());
        size.setText(String.valueOf(currentRoomList.get(index).size));
        price.setText(String.format("Rp.%s", currentRoomList.get(index).price.price));
        address.setText(currentRoomList.get(index).address);
        city.setText(String.valueOf(currentRoomList.get(index).city));

        for(Facility f : currentRoomList.get(index).facility){
            if(f == Facility.AC)
                AC.setChecked(true);
            else if(f == Facility.Balcony)
                Balcony.setChecked(true);
            else if(f == Facility.Bathtub)
                Bathtub.setChecked(true);
            else if(f == Facility.FitnessCenter)
                FitnessCenter.setChecked(true);
            else if(f == Facility.Refrigerator)
                Refrigerator.setChecked(true);
            else if(f == Facility.Restaurant)
                Restaurant.setChecked(true);
            else if(f == Facility.SwimmingPool)
                SwimmingPool.setChecked(true);
            else if(f == Facility.WiFi)
                WiFi.setChecked(true);
        }

        book.setOnClickListener(view -> startActivity(
                new Intent(DetailRoomActivity.this,
                        BookRoomActivity.class)));
    }

    /**
     * Set back activity depending on renter status
     * @param item Clicked item
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            if(MainActivity.isRenter)
                startActivity(new Intent(DetailRoomActivity.this, YourRoomsActivity.class));
            else
                startActivity(new Intent(DetailRoomActivity.this, MainActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        return true;
    }

    /**
     * Method to destroy activity when back button is pressed and return to {@link JuanjsleepRJ.jsleep_android.MainActivity} or {@link JuanjsleepRJ.jsleep_android.YourRoomsActivity}.
     */
    public void onBackPressed() {
        super.onBackPressed();
        if(MainActivity.isRenter)
            startActivity(new Intent(DetailRoomActivity.this, YourRoomsActivity.class));
        else
            startActivity(new Intent(DetailRoomActivity.this, MainActivity.class));
        finish();
    }
}