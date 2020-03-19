package com.example.final_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class InfoToolBar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_tool_bar);

        Toolbar toolbar = findViewById(R.id.toolbarinfo);
        setSupportActionBar(toolbar);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent nextActivity = null;
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.choice1:
                nextActivity= new Intent(InfoToolBar.this, GuardianActivity.class);
                break;
            case R.id.choice2:
                nextActivity= new Intent(InfoToolBar.this, NasaDayActivity.class);
                break;
            case R.id.choice3:
                nextActivity= new Intent(InfoToolBar.this, NasaEarthActivity.class);
                break;
            case R.id.choice4:
                nextActivity= new Intent(InfoToolBar.this, BBCActivity.class);
                break;
        }
        startActivity(nextActivity);
        return true;
    }


}
