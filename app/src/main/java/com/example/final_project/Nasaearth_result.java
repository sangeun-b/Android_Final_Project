package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class Nasaearth_result extends AppCompatActivity {
    Button re;
    Button saved;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasaearth_result);

        re = findViewById(R.id.reButton);

        Intent goToPrevious= new Intent(Nasaearth_result.this,NasaEarthActivity.class);
        re.setOnClickListener(click->startActivity(goToPrevious));


        saved = findViewById(R.id.save);
        saved.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), getString(R.string.saved), Toast.LENGTH_LONG).show();
            }
        });


    }
}
