package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button Guardianb= findViewById(R.id.b1);
        Button NasaDayb= findViewById(R.id.b2);
        Button NasaEarthb= findViewById(R.id.b3);
        Button BBCb= findViewById(R.id.b4);

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
