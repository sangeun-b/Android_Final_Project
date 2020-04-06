package com.example.final_project;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * The nasadayemptyactivity class instantiate an instance of nasadayimagedetailsfragment object
 * @author Hsing-I Wang
 * @version 1.0
 */
public class NasaDayImageEmptyActivity extends AppCompatActivity{

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.nasadayimage_empty);

            Bundle dataToPass = getIntent().getExtras();
            NasaDayImageDetailsFragment dFragment = new NasaDayImageDetailsFragment();
            dFragment.setArguments(dataToPass);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentLocation, dFragment)
                    .commit();
        }
}
