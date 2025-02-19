package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @author Sangeun Baek
 * This activity allows users to search for the earth's image using the latitude and longitude that the users entered.
 * Save the latitude and longtitude to the SharedPreference.
 */

public class NasaEarthActivity extends AppCompatActivity {
    Button search;
    ImageView saved;
    EditText earthLat;
    EditText earthLon;
    static String inputLat;
    static String inputLon;
    ImageView back;

    /**
     * Loads the nasaearth layout.
     * User enter the latitude and longitude to search the earth image,
     * if user doesn't enter the value of lat and lon, can't search the image,
     * ask user to enter the value of lat and lon.
     * if user click the saved list, it goes to the saved list.
     * if user click the back icon, it go back to the previous page which is main page.
     * @param savedInstanceState Bundle object containing the activity's previously saved state.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nasaearth);
        search = findViewById(R.id.earthsearch);
        saved = findViewById(R.id.earthsaved);
        earthLat = findViewById(R.id.enterLat);
        earthLon = findViewById(R.id.enterLon);

        SharedPreferences prefs = getSharedPreferences("NasaEarth", Context.MODE_PRIVATE);
        String s1 = prefs.getString("Lat", "");
        earthLat.setText(s1);
        String s2 = prefs.getString("Lon", "");
        earthLon.setText(s2);

        //Intent goToSearch= new Intent(NasaEarthActivity.this,Nasaearth_result.class);
        //search.setOnClickListener(click->startActivity(goToSearch));
        search.setOnClickListener(click-> {
            inputLat = earthLat.getText().toString();
            inputLon = earthLon.getText().toString();
                    if (inputLat.isEmpty()) {
                        Toast.makeText(getApplicationContext(), getString(R.string.earthcklat), Toast.LENGTH_SHORT).show();
                    } else if(inputLon.isEmpty()) {
                        Toast.makeText(getApplicationContext(), getString(R.string.earthcklon), Toast.LENGTH_SHORT).show();
                    }else if(!inputLat.isEmpty()&&!inputLon.isEmpty()) {
                        Intent goToSearch= new Intent(NasaEarthActivity.this,Nasaearth_result.class);
                        startActivity(goToSearch);
                    }
        });

        Intent goToSaved = new Intent(NasaEarthActivity.this,Nasaearth_saved.class);
        saved.setOnClickListener(click->startActivity(goToSaved));

        Intent goBack = new Intent(NasaEarthActivity.this,MainActivity.class);
        back = findViewById(R.id.earthback);
        back.setOnClickListener(click -> startActivity(goBack));

    }
    @Override
    protected void onPause(){
        super.onPause();
        earthLat = findViewById(R.id.enterLat);
        earthLon = findViewById(R.id.enterLon);

        SharedPreferences prefs = getSharedPreferences("NasaEarth",Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("Lon",earthLon.getText().toString());
        edit.putString("Lat",earthLat.getText().toString());
        edit.commit();

    }
}
