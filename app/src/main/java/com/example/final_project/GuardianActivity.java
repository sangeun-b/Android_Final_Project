package com.example.final_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;


import com.google.android.material.snackbar.Snackbar;


public class GuardianActivity extends AppCompatActivity {
    SharedPreferences prefs = null;
    static String searchInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian);
        EditText searchText=findViewById(R.id.search_news);


        prefs = getSharedPreferences("Guardian", Context.MODE_PRIVATE);
        String saveSearch= prefs.getString("searchNews", "");
        searchText.setText(saveSearch);


        Button search = findViewById(R.id.search_button);
        Intent goToSearch = new Intent(GuardianActivity.this, Guardian_search_results.class);
        search.setOnClickListener(click ->{
                searchInput =searchText.getText().toString();
                if(searchInput.isEmpty()||searchInput==null){
                    Snackbar.make(search,"Please enter the key word!",Snackbar.LENGTH_LONG).show();
                }
                else{
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("searchNews", searchInput);
                    editor.commit();
                    startActivity(goToSearch);
                    }});




        ImageView favourite = findViewById(R.id.favouriteList);
        Intent goToFavourite = new Intent(GuardianActivity.this, Guardian_favourite.class);
        favourite.setOnClickListener(click -> startActivity(goToFavourite));


        }


    }


