package JuanjsleepRJ.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;

import com.google.gson.*;

import JuanjsleepRJ.jsleep_android.model.Room;
import JuanjsleepRJ.jsleep_android.request.BaseApiService;
import JuanjsleepRJ.jsleep_android.request.UtilsApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.*;
import android.view.*;


public class MainActivity extends AppCompatActivity {
    BaseApiService mApiService;
    Context mContext;
    List<Room> roomList = new ArrayList<Room>();
    EditText page;
    List<String> roomNameList = new ArrayList<String>();
    public int pageNumber;

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.topmenu, menu);
        MenuItem addButton = menu.findItem(R.id.add_room);
        if(LoginActivity.savedAccount.renter == null) {
            addButton.setVisible(true);
        }else{
            addButton.setVisible(false);
        }
        return true;
    }

    public static ArrayList<String> getName(ArrayList<Room> roomList){
        Gson gson = new Gson();
        ArrayList<String> nameList = new ArrayList<String>();
        for (int i = 0; i < roomList.size(); i++) {
            nameList.add(roomList.get(i).name);
        }
        return nameList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.account_button:
                Intent move = new Intent(MainActivity.this, AboutMeActivity.class);
                startActivity(move);
                return true;
            case R.id.add_room:
                Intent move2 = new Intent(MainActivity.this, CreateRoomActivity.class);
                startActivity(move2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mApiService = UtilsApi.getApiService();
        mContext = this;
        Button nextButton = findViewById(R.id.nextButton);
        Button prevButton = findViewById(R.id.prevButton);
        Button goButton = findViewById(R.id.goButton);

        Gson gson = new Gson();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageNumber++;
                int pageMax = (roomList.size()/10);

                if(pageMax != 0){
                    pageMax++;
                }
                if(pageNumber > roomList.size()){
                    pageNumber = pageMax;
                    return;
                }
                String pageText = String.valueOf(pageNumber);
                page.setText(pageText);
                try {
                    roomList = getAllRoom();  //return null
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageNumber--;
                if(pageNumber < 1){
                    pageNumber = 1;
                    return;
                }
                String pageString = String.valueOf(pageNumber);
                page.setText(pageString);
                try {
                    roomList = getAllRoom();  //return null
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageNumber = 0;

                if (!"".equals(page.getText().toString())) {
                    pageNumber = Integer.parseInt(page.getText().toString());
                }
                try {
                    roomList = getAllRoom();  //return null
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected ArrayList<Room> getAllRoom(){
        mApiService.getAllRoom(
                pageNumber,
                10
        ).enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                roomList = response.body();
                ArrayAdapter<Room> itemAdapter = new ArrayAdapter<Room>(
                        mContext,
                        android.R.layout.simple_list_item_1,
                        roomList
                );
                ListView listView = findViewById(R.id.List);
                listView.setAdapter(itemAdapter);
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return null;
    }
}