package com.example.final_project;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class InfoToolBar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_tool_bar);

        Toolbar toolbar = findViewById(R.id.toolbarinfo);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
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


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alert = builder.create();
        builder.setTitle("How To Use ");
        switch(item.getItemId())
        {
            case R.id.item_guardian:
                Intent guardianIntent= new Intent(InfoToolBar.this, GuardianActivity.class);
                startActivity(guardianIntent);
                break;
            case R.id.item_nasaday:
                Intent nasadayIntent= new Intent(InfoToolBar.this, NasaDayActivity.class);
                startActivity(nasadayIntent);
                break;
            case R.id.item_nasaearth:
                Intent nasaearthIntent= new Intent(InfoToolBar.this, NasaEarthActivity.class);
                startActivity(nasaearthIntent);
                break;
            case R.id.item_bbc:
                Intent bbcIntent= new Intent(InfoToolBar.this, BBCActivity.class);
                startActivity(bbcIntent);
                break;
            case R.id.guide_guardian:
                builder.setMessage("Enter a search word to find related articles from The Guardian newspaper.")
                        .setPositiveButton("OK", (click, arg)-> alert.cancel())
                        .show();
                break;
            case R.id.guide_nasaday:
                builder.setMessage("Enter a day to view the image of that day pick by NASA. ")
                        .setPositiveButton("OK", (click, arg)-> alert.cancel())
                        .show();
                break;
            case R.id.guide_nasaearth:
                builder.setMessage("Enter the latitude and longitude to view the satellite image of that position. ")
                        .setPositiveButton("OK", (click, arg)-> alert.cancel())
                        .show();
                break;
            case R.id.guide_bbc:
                builder.setMessage("View the articles from BBC. ")
                        .setPositiveButton("OK", (click, arg)-> alert.cancel())
                        .show();
                break;
        }
        DrawerLayout drawyer= (DrawerLayout) findViewById(R.id.drawer_layout);
        drawyer.closeDrawer(GravityCompat.START);
        return true;
    }
}
