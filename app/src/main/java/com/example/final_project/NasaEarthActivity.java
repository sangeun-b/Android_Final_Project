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

public class NasaEarthActivity extends AppCompatActivity {
    Button search;
    ImageView saved;
    EditText earthLat;
    EditText earthLon;
    static String inputLat;
    static String inputLon;

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
