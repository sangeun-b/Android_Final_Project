package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class NasaEarth extends AppCompatActivity {
    private long id;
    private String latitude, longitude, date;

    public NasaEarth(){
    }
    public NasaEarth(long id, String latitude, String longitude, String date){
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
    }
    public String getLatitude(){
        return latitude;
    }
    public String getLongitude(){
        return longitude;
    }
    public String getDate(){
        return date;
    }
    public long getId(){
        return id;
    }
}
