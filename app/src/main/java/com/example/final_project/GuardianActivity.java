package com.example.final_project;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class GuardianActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian);

        Button search =findViewById(R.id.search_button);
        Intent goToSearch = new Intent(GuardianActivity.this, Guardian_search_results.class);
        search.setOnClickListener(click->startActivity(goToSearch));

        ImageButton favourite=findViewById(R.id.favouriteList);
        Intent goToFavourite=new Intent(GuardianActivity.this,Guardian_favourite.class);
        favourite.setOnClickListener(click->startActivity(goToFavourite));
        







    }

}
