package JuanjsleepRJ.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import JuanjsleepRJ.jsleep_android.model.Room;
import JuanjsleepRJ.jsleep_android.request.BaseApiService;
import JuanjsleepRJ.jsleep_android.request.UtilsApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity which displays list of rooms which the logged account rented
 * @author juanjonathan67
 * @version 1.0.0
 */
public class YourRoomsActivity extends AppCompatActivity {
    /**
     * {@link JuanjsleepRJ.jsleep_android.request.BaseApiService} attribute
     */
    BaseApiService mApiService;
    /**
     * Context of activity
     */
    Context mContext;
    /**
     * Rented room list
     */
    public static List<Room> rentedRoomList = new ArrayList<>();
    /**
     * Rented room names
     */
    ArrayList<String> rentedRoomName = new ArrayList<>();
    /**
     * Searched room list
     */
    ArrayList<String> searchedRooms = new ArrayList<>();
    /**
     * Selected room index
     */
    public static int rentedRoomIndex;
    /**
     * List view to display list of rooms
     */
    ListView listView;

    /**
     * Create activity
     * @param savedInstanceState Instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_rooms);
        mApiService = UtilsApi.getApiService();
        mContext = this;

        listView = findViewById(R.id.rentedRoomListView);
        getRentedRooms(true);

        SearchView searchView = findViewById(R.id.searchRentedRoom);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                ArrayList<String> temp = new ArrayList<>();
                for(String str : rentedRoomName){
                    if(str.toLowerCase().contains(s.toLowerCase()))
                        temp.add(str);
                }
                ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(
                        mContext,
                        android.R.layout.simple_list_item_1,
                        temp
                ){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view =super.getView(position, convertView, parent);
                        TextView textView=(TextView) view.findViewById(android.R.id.text1);
                        textView.setTextColor(Color.BLACK);
                        return view;
                    }
                };
                listView.setAdapter(itemAdapter);
                searchedRooms = temp;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<String> temp = new ArrayList<>();
                for(String str : rentedRoomName){
                    if(str.toLowerCase().contains(s.toLowerCase()))
                        temp.add(str);
                }
                ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(
                        mContext,
                        android.R.layout.simple_list_item_1,
                        temp
                ){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view =super.getView(position, convertView, parent);
                        TextView textView=(TextView) view.findViewById(android.R.id.text1);
                        textView.setTextColor(Color.BLACK);
                        return view;
                    }
                };
                listView.setAdapter(itemAdapter);
                searchedRooms = temp;
                return false;
            }
        });

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            if(searchedRooms.size() > 0) {
                for (int j = 0; j < rentedRoomList.size(); j++) {
                    if (rentedRoomName.get(j).compareTo(searchedRooms.get(i) ) == 0) {
                        rentedRoomIndex = j;
                    }
                }
            }else{
                rentedRoomIndex = i;
            }
            startActivity(new Intent(YourRoomsActivity.this, DetailRoomActivity.class));
            finish();
        });
    }

    /**
     * Method used to make GET request with implemented method {@link JuanjsleepRJ.jsleep_android.request.BaseApiService#getPaginatedRoom(int, int, int, boolean)}.
     * Get list of rented rooms
     * @param isRenter Renter status
     */
    protected void getRentedRooms(boolean isRenter){
        mApiService.getPaginatedRoom(
                LoginActivity.savedAccount.id,
                0,
                100,
                isRenter
        ).enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                rentedRoomList = response.body();
                for(Room r : rentedRoomList){
                    rentedRoomName.add(r.name);
                }
                System.out.println(rentedRoomName);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        mContext,
                        android.R.layout.simple_list_item_1,
                        rentedRoomName
                ){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view =super.getView(position, convertView, parent);
                        TextView textView=(TextView) view.findViewById(android.R.id.text1);
                        textView.setTextColor(Color.BLACK);
                        return view;
                    }
                };
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * Method used to destroy activity when back is pressed and goes back to {@link JuanjsleepRJ.jsleep_android.MainActivity}.
     */
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.isRenter = false;
        startActivity(new Intent(YourRoomsActivity.this, MainActivity.class));
        finish();
    }
}