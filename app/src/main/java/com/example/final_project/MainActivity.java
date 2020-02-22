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

        Button Guardianb= (Button)findViewById(R.id.b1);
        Button NasaDayb= (Button)findViewById(R.id.b2);
        Button NasaEarthb= (Button)findViewById(R.id.b3);
        Button BBCb= (Button)findViewById(R.id.b4);

        Intent goToGuardian= new Intent(MainActivity.this, GuardianActivity.class);
        Guardianb.setOnClickListener(click->startActivity(goToGuardian));

        Intent goToNasaDayb= new Intent(MainActivity.this, NasaDayActivity.class);
        Guardianb.setOnClickListener(click->startActivity(goToNasaDayb));

        Intent goToNasaEarth= new Intent(MainActivity.this , NasaEarthActivity.class);
        Guardianb.setOnClickListener(click->startActivity(goToNasaEarth));

        Intent goToBBC= new Intent(MainActivity.this, BBCActivity.class);
        Guardianb.setOnClickListener(click->startActivity(goToBBC));







    }
}
