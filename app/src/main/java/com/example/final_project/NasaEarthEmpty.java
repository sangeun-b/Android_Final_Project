package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class NasaEarthEmpty extends AppCompatActivity {

    /**
     * Load the nasaearth_empty layout.
     * get the data that was passed from Fragement.
     * add detail fragment and pass it a bundle for information.
     * Add the fragment in FrameLayout.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nasaearth_empty);

        Bundle dataToPass = getIntent().getExtras();
        NasaEarthDetailsFragment earthFragment = new NasaEarthDetailsFragment();
        earthFragment.setArguments(dataToPass);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.earthFragment, earthFragment)
                .commit();

    }
}
