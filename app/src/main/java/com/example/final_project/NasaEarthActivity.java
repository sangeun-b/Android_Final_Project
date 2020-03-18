package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class NasaEarthActivity extends AppCompatActivity {
    Button search;
    ImageView saved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasaearth);
        search = findViewById(R.id.earthsearch);
        saved = findViewById(R.id.earthsaved);

        Intent goToSearch= new Intent(NasaEarthActivity.this,Nasaearth_result.class);
        search.setOnClickListener(click->startActivity(goToSearch));

        Intent goToSaved = new Intent(NasaEarthActivity.this,Nasaearth_saved.class);
        saved.setOnClickListener(click->startActivity(goToSaved));


    }
}
