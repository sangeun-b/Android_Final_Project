package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * @author Sangeun Baek
 * This activity shows the detail of the image using the fragment,
 * when the device is the phone.
 */

public class NasaEarthEmpty extends AppCompatActivity {

    /**
     * Load the nasaearth_empty layout.
     * get the data that was passed from Fragement.
     * add detail fragment and pass it a bundle for information.
     * Add the fragment in FrameLayout.
     * @param savedInstanceState Bundle object containing the activity's previously saved state.
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
