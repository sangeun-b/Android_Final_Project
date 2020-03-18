package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Nasaearth_result extends AppCompatActivity {
    Button re;
    Button saved;
    ProgressBar earthProgressBar;
    ImageView earthImageView;
    TextView earthLatTextView;
    TextView earthLonTextView;
    TextView earthDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasaearth_result);

        // https://api.nasa.gov/planetary/earth/imagery?lon=100.75&lat=1.5&date=2014-02-01&api_key=DEMO_KEY


        earthImageView = findViewById(R.id.earthImage);
        earthLatTextView = findViewById(R.id.earthlat);
        earthLonTextView = findViewById(R.id.earthlon);
        earthDateTextView = findViewById(R.id.earthdate);
        re = findViewById(R.id.reButton);
        saved = findViewById(R.id.save);
        EditText enterEarthLat = findViewById(R.id.enterLat);
        EditText enterEarthLon = findViewById(R.id.enterLon);
        String inLat = earthLatTextView.getText().toString();
        String inLon = earthLonTextView.getText().toString();

        NasaEarthImage nasaEarth = new NasaEarthImage();
        nasaEarth.execute("https:api.nasa.gov/planetary/earth/imagery?lon="+inLon+"&lat="+inLat+"&date=2014-02-01&api_key=DEMO_KEY");

        earthProgressBar = findViewById(R.id.earthprogress);
        earthProgressBar.setVisibility(View.VISIBLE);

        Intent goToPrevious = new Intent(Nasaearth_result.this, NasaEarthActivity.class);
        re.setOnClickListener(click -> startActivity(goToPrevious));

        saved.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), getString(R.string.earthsaved), Toast.LENGTH_LONG).show();
            }
        });
    }
        private class NasaEarthImage  extends AsyncTask<String, Integer, String> {
            String latitude, longitude, date;
            Bitmap image;

            public String doInBackground(String... args) {

                try {
                    URL url = new URL(args[0]);
                    HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                }
                return "Done";
            }

            public void onProgressUpdate(Integer... value) {
                earthProgressBar.setVisibility(View.VISIBLE);
                earthProgressBar.setProgress(value[0]);
            }

            public void onPostExecute(String fromDoInBackground) {
            }


        }



    }

