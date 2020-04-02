package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


public class NasaEarth extends AppCompatActivity {
    private long id;
    private String latitude, longitude, date, url;

    /**
     * default constructor
     */
    public NasaEarth(){
    }

    /**
     * construct takes four parameter
     * @param id
     * @param latitude
     * @param longitude
     * @param date
     */
    public NasaEarth(long id, String latitude, String longitude, String date){
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
    }

    /**
     * This getLatitude method is used to retrive the value of latitude.
     * @return a string data type
     */
    public String getLatitude(){
        return latitude;
    }

    /**
     * This getLongitude method is used to retrive the value of longitude.
     * @return a string data type
     */
    public String getLongitude(){
        return longitude;
    }

    /**
     * This getDate method is uesd to retrive the value of date.
     * @return a string data type
     */
    public String getDate(){
        return date;
    }

    /**
     * This getId method is used to retirve the value of Id.
     * @return a long data type
     */
    public long getId(){
        return id;
    }
}
