package JuanjsleepRJ.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import java.io.BufferedReader;
import java.io.InputStream;

import com.google.gson.*;

import JuanjsleepRJ.jsleep_android.model.Room;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.*;
import android.view.*;


public class MainActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.topmenu, menu);
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
                Intent move = new Intent(MainActivity.this, AboutMe.class);
                startActivity(move);
                return true;
            case R.id.add_account:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.List);
        List<String> nameList = new ArrayList<String>();
        Gson gson = new Gson();
        try {
            InputStream stream = getAssets().open("randomRoomList.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            ArrayList<Room> roomList = new ArrayList<Room>();
            Room[] acc = gson.fromJson(reader, Room[].class);
            Collections.addAll(roomList, acc);
            nameList = getName(roomList);
            ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameList){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view =super.getView(position, convertView, parent);

                    TextView textView=(TextView) view.findViewById(android.R.id.text1);
                    textView.setTextColor(Color.BLACK);

                    return view;
                }
            };
            listView.setAdapter(itemAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}