package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
}
