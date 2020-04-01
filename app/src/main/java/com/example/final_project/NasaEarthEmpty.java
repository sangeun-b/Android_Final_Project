package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class NasaEarthEmpty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nasaearth_empty);

        Bundle dataToPass = getIntent().getExtras(); //get the data that was passed from FragmentExample
        //This is copied directly from FragmentExample.java lines 47-54
        NasaEarthDetailsFragment earthFragment = new NasaEarthDetailsFragment(); //add a DetailFragment
        earthFragment.setArguments(dataToPass);//pass it a bundle for information
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.earthFragment, earthFragment) //Add the fragment in FrameLayout
                .commit();

    }
}
