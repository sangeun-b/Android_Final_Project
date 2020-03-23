package com.example.final_project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Guardian_favourite_empty extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.guardian_empty);

            Bundle dataToPass = getIntent().getExtras();

            Guardian_favourite_details dFragment = new Guardian_favourite_details();//add a DetailFragment
            dFragment.setArguments(dataToPass);//pass it a bundle for information
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                    .commit(); //actually load the fragment.


        }
    }

