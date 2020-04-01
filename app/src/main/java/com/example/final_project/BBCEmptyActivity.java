package com.example.final_project;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class BBCEmptyActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbcempty);

        Bundle dataToPass = getIntent().getExtras();
        BBCDetailsFragment fragment = new BBCDetailsFragment();
        fragment.setArguments(dataToPass);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentLocation, fragment)
                .commit();
    }
}
