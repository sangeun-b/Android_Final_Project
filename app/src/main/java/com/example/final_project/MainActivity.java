package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton Guardianb= findViewById(R.id.guardianButoon);
        ImageButton NasaDayb= findViewById(R.id.nasaDayButton);
        ImageButton NasaEarthb= findViewById(R.id.nasaEarthButton);
        ImageButton BBCb= findViewById(R.id.bbcButton);

        Intent goToGuardian= new Intent(MainActivity.this, GuardianActivity.class);
        Guardianb.setOnClickListener(click->startActivity(goToGuardian));

        Intent goToNasaDay= new Intent(MainActivity.this, NasaDayActivity.class);
        NasaDayb.setOnClickListener(click->startActivity(goToNasaDay));

        Intent goToNasaEarth= new Intent(MainActivity.this , NasaEarthActivity.class);
        NasaEarthb.setOnClickListener(click->startActivity(goToNasaEarth));

        Intent goToBBC= new Intent(MainActivity.this, BBCActivity.class);
        BBCb.setOnClickListener(click->startActivity(goToBBC));


        

    }
}
