package com.example.final_project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * for the phone, the fragment using the same code that you used in the tablet.
 */
public class GuardianEmptyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guardian_empty);

        Bundle dataToPass = getIntent().getExtras();

        Guardian_details_fragment dFragment=new Guardian_details_fragment();//add a DetailFragment
        dFragment.setArguments(dataToPass);//pass it a bundle for information
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentLocation,dFragment) //Add the fragment in FrameLayout
                .commit(); //actually load the fragment.


    }

}
