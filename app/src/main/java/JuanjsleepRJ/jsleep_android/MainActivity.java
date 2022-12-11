package JuanjsleepRJ.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import JuanjsleepRJ.jsleep_android.model.BedType;
import JuanjsleepRJ.jsleep_android.model.City;
import JuanjsleepRJ.jsleep_android.model.Facility;
import JuanjsleepRJ.jsleep_android.model.Room;
import JuanjsleepRJ.jsleep_android.request.BaseApiService;
import JuanjsleepRJ.jsleep_android.request.UtilsApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;

/**
 * Activity which displays List of bookable rooms. List of rooms can be filtered, searched, etc.
 * @author juanjonathan67
 * @version 1.0.0
 */
public class MainActivity extends AppCompatActivity{
    /**
     * {@link JuanjsleepRJ.jsleep_android.request.BaseApiService} attribute
     */
    BaseApiService mApiService;
    /**
     * Context of activity
     */
    Context mContext;
    /**
     * Paginated room list
     */
    public static List<Room> roomPaginatedList = new ArrayList<>();
    /**
     * List of all room
     */
    public static List<Room> allRoomList = new ArrayList<>();
    /**
     * List of filtered rooms
     */
    List<Room> filteredRoomList = new ArrayList<>();
    /**
     * List of searched room names
     */
    List<String> searchedRooms = new ArrayList<>();
    /**
     * List of filtered room names
     */
    List<String> filteredRooms = new ArrayList<>();
    /**
     * Text view of page number
     */
    EditText page;
    /**
     * List view of rooms
     */
    ListView listView;
    /**
     * List of room names
     */
    List<String> roomNameList = new ArrayList<>();
    /**
     * Page number
     */
    int pageNumber = 1;
    /**
     * Index of selected room
     */
    public static int roomIndex;
    /**
     * Status of renter
     */
    public static boolean isRenter = false;
    /**
     * Status if list is filtered
     */
    boolean filter = false;
    /**
     * List of filter categories
     */
    List<String> filterCategory;
    /**
     * List of filter category options
     */
    List<String> categoryOptions;
    /**
     * HashMap of filter category and its options
     */
    Map<String, List<String>> filterCollection;
    /**
     * Expandable list view used to display filters
     */
    ExpandableListView expandableListView;
    /**
     * Adapter for expandable list view
     */
    ExpandableListAdapter expandableListAdapter;
    /**
     * Filter button
     */
    ImageButton filterButton;
    /**
     * Filter detail
     */
    TextView filteredWith;

    /**
     * Create top menu
     * @param menu Menu to be inflated
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.topmenu, menu);
        MenuItem addButton = menu.findItem(R.id.add_room);
        addButton.setVisible(LoginActivity.savedAccount.renter != null);
        return true;
    }

    /**
     * Get the room names of a list of rooms
     * @param roomList List of rooms
     * @return ArrayList of room names
     */
    public static ArrayList<String> getName(ArrayList<Room> roomList){
        ArrayList<String> nameList = new ArrayList<String>();
        for (int i = 0; i < roomList.size(); i++) {
            nameList.add(roomList.get(i).name);
        }
        return nameList;
    }

    /**
     * Handles top view selection
     * @param item Menu item which was clicked
     * @return true
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.account_button:
                startActivity(new Intent(MainActivity.this, AboutMeActivity.class));
                finish();
                return true;
            case R.id.add_room:
                startActivity(new Intent(MainActivity.this, CreateRoomActivity.class));
                finish();
                return true;
            case R.id.payments:
                startActivity(new Intent(MainActivity.this, PaymentActivity.class));
                finish();
                return true;
            case R.id.rooms:
                isRenter = true;
                startActivity(new Intent(MainActivity.this, YourRoomsActivity.class));
                finish();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Create activity
     * @param savedInstanceState Instance state
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mApiService = UtilsApi.getApiService();
        mContext = this;
        Button nextButton = findViewById(R.id.nextButton);
        Button prevButton = findViewById(R.id.prevButton);
        Button goButton = findViewById(R.id.goButton);
        listView = findViewById(R.id.List);

        filterButton = findViewById(R.id.filterRoomButton);
        filterButton.setImageResource(R.drawable.ic_baseline_filter_list_24);

        initFilterCategory();
        initCategoryOptions();
        expandableListView = findViewById(R.id.filterRoomList);
        expandableListAdapter = new ExpandableListAdapter(this, filterCategory, filterCollection);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setVisibility(View.GONE);

        filteredWith = findViewById(R.id.textViewRoomFilter1);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(filter) {
                    expandableListView.setVisibility(View.GONE);
                    filter = false;
                }
                else {
                    expandableListView.setVisibility(View.VISIBLE);
                    filter = true;
                }
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int lastExpanded = -1;
            @Override
            public void onGroupExpand(int i) {
                if(lastExpanded != -1 && i != lastExpanded)
                    expandableListView.collapseGroup(lastExpanded);
                lastExpanded = i;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                String selected = expandableListAdapter.getChild(i, i1).toString();
                filteredWith.setText(selected);
                if(i == 0) { //price, city, facilities, bed type
                    if(i1 == 0)
                        getFilteredRoomByPrice(0, 500000, false);
                    else if(i1 == 1)
                        getFilteredRoomByPrice(500000, 1000000, false);
                    else if(i1 == 2)
                        getFilteredRoomByPrice(1000000, 5000000, false);
                    else
                        getFilteredRoomByPrice(5000000, 100000000, false);
                }else if(i == 1){
                    getFilteredRoomByCity(City.valueOf(selected), false);
                }else if(i == 2){
                    getFilteredRoomByFacilities(Facility.valueOf(selected), false);
                }else{
                    getFilteredRoomByBedType(BedType.valueOf(selected), false);
                }
                expandableListView.collapseGroup(i);
                return true;
            }
        });

        SearchView searchView = findViewById(R.id.searchRentedRoom);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                ArrayList<String> temp = new ArrayList<>();
                for(String str : roomNameList){
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
                for(String str : roomNameList){
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

        page = findViewById(R.id.page_number);
        page.setText(String.valueOf(pageNumber));
        getPaginatedRoom(false);
        getAllRoom();


        nextButton.setOnClickListener(view -> {
            pageNumber++;
            getPaginatedRoom(false);
        });

        prevButton.setOnClickListener(view -> {
            pageNumber--;
            if(pageNumber < 1){
                pageNumber = 1;
                return;
            }
            page.setText(String.valueOf(pageNumber));
        });

        goButton.setOnClickListener(view -> {
            if (!"".equals(page.getText().toString())) {
                pageNumber = Integer.parseInt(page.getText().toString());
            }
            try {
                getPaginatedRoom(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            List<String> temp;
            if(filter) {
                temp = filteredRooms;
                filter = false;
            }
            else temp = searchedRooms;

            if(temp.size() > 0) {
                for (int j = 0; j < roomNameList.size(); j++) {
                    if (roomNameList.get(j).compareTo(temp.get(i) ) == 0) {
                        roomIndex = j;
                    }
                }
            }else{
                roomIndex = i;
            }
            startActivity(new Intent(MainActivity.this, DetailRoomActivity.class));
            finish();
        });
    }

    /**
     * Initialize filter category options for each category
     */
    private void initCategoryOptions() {
        int i = 0;
        String[] priceRangeFilter = {
                "0 - 500.000",
                "500.000 - 1.000.000",
                "1.000.000 - 5.000.000",
                "> 5.000.000"
        };
        String[] cityFilter = new String[9];
        for (City c : City.values()) {
            cityFilter[i] = c.name();
            i++;
;        }
        i = 0;
        String[] facilityFilter = new String[8];
        for (Facility f : Facility.values()) {
            facilityFilter[i] = f.name();
            i++;
        }
        String[] bedTypeFilter = new String[4];
        i = 0;
        for (BedType b : BedType.values()) {
            bedTypeFilter[i] = b.name();
            i++;
        }
        filterCollection = new HashMap<String, List<String>>();
        for(String category : filterCategory){
            switch (category) {
                case "Price":
                    loadChild(priceRangeFilter);
                    break;
                case "City":
                    loadChild(cityFilter);
                    break;
                case "Facilities":
                    loadChild(facilityFilter);
                    break;
                default:
                    loadChild(bedTypeFilter);
                    break;
            }
            filterCollection.put(category, categoryOptions);
        }
    }

    /**
     * Load the options list of selected filter category
     * @param filter Selected filter
     */
    private void loadChild(String[] filter) {
        categoryOptions = new ArrayList<>();
        Collections.addAll(categoryOptions, filter);
    }

    /**
     * Initialize filter categories
     */
    private void initFilterCategory() {
        filterCategory = new ArrayList<>();
        filterCategory.add("Price");
        filterCategory.add("City");
        filterCategory.add("Facilities");
        filterCategory.add("BedType");
    }

    /**
     * Paginate a list of rooms
     * @param isRenter Renter status
     */
    protected void getPaginatedRoom(boolean isRenter){
        mApiService.getPaginatedRoom(
                LoginActivity.savedAccount.id,
                pageNumber - 1,
                20,
                isRenter
        ).enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                roomPaginatedList = response.body();
                if(!roomPaginatedList.isEmpty()) {
                    page.setText(String.valueOf(pageNumber));
                    roomNameList = getName((ArrayList<Room>) roomPaginatedList);
                    ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(
                            mContext,
                            android.R.layout.simple_list_item_1,
                            roomNameList
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
                    searchedRooms = roomNameList;
                    filteredRooms = roomNameList;
                }else{
                    pageNumber--;
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * Get all rooms
     */
    protected void getAllRoom(){
        mApiService.getAllRoom().enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                allRoomList = response.body();
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * Method used to make GET request with implemented method {@link JuanjsleepRJ.jsleep_android.request.BaseApiService#getFilteredRoomByCity(int, City, boolean)}.
     * Filter list of room by its {@link JuanjsleepRJ.jsleep_android.model.City}
     * @param city City to be filtered
     * @param isRenter Renter status
     */
    protected void getFilteredRoomByCity(City city, boolean isRenter){
        mApiService.getFilteredRoomByCity(
                LoginActivity.savedAccount.id,
                city,
                isRenter
        ).enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                filteredRoomList = response.body();
                filteredRoomAdapter();
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * Method used to make GET request with implemented method {@link JuanjsleepRJ.jsleep_android.request.BaseApiService#getFilteredRoomByFacilities(int, Facility, boolean)}.
     * Filter list of room by its {@link JuanjsleepRJ.jsleep_android.model.Facility}
     * @param facility Facility to be filtered
     * @param isRenter Renter status
     */
    protected void getFilteredRoomByFacilities(Facility facility, boolean isRenter){
        mApiService.getFilteredRoomByFacilities(
                LoginActivity.savedAccount.id,
                facility,
                isRenter
        ).enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                filteredRoomList = response.body();
                filteredRoomAdapter();
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * Method used to make GET request with implemented method {@link JuanjsleepRJ.jsleep_android.request.BaseApiService#getFilteredRoomByBedType(int, BedType, boolean)}.
     * Filter list of room by its {@link JuanjsleepRJ.jsleep_android.model.BedType}
     * @param bedType Bed type to be filtered
     * @param isRenter Renter status
     */
    protected void getFilteredRoomByBedType(BedType bedType, boolean isRenter){
        mApiService.getFilteredRoomByBedType(
                LoginActivity.savedAccount.id,
                bedType,
                isRenter
        ).enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                filteredRoomList = response.body();
                filteredRoomAdapter();
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * Method used to make GET request with implemented method {@link JuanjsleepRJ.jsleep_android.request.BaseApiService#getFilteredRoomByPrice(int, double, double, boolean)}.
     * Filter list of room by its {@link JuanjsleepRJ.jsleep_android.model.Price#price}
     * @param priceLow Lower bound of price
     * @param priceHigh Upper bound of price
     * @param isRenter Renter status
     */
    protected void getFilteredRoomByPrice(double priceLow, double priceHigh, boolean isRenter){
        mApiService.getFilteredRoomByPrice(
                LoginActivity.savedAccount.id,
                priceLow,
                priceHigh,
                isRenter
        ).enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                filteredRoomList = response.body();
                filteredRoomAdapter();
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * Method used to destroy activity when back is pressed and goes back to {@link JuanjsleepRJ.jsleep_android.LoginActivity}.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    /**
     * Adapter for filtered room list
     */
    private void filteredRoomAdapter(){
        filteredRooms = getName((ArrayList<Room>) filteredRoomList);
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(
                mContext,
                android.R.layout.simple_list_item_1,
                filteredRooms
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
    }
}