package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasaearth_result);

        // https://api.nasa.gov/planetary/earth/imagery?lon=100.75&lat=1.5&date=2014-02-01&api_key=DEMO_KEY

        NasaEarthImage nasaEarth = new NasaEarthImage();
        nasaEarth.execute("https://api.nasa.gov/planetary/earth/imagery/?lon=" + NasaEarthActivity.inputLon + "&lat=" + NasaEarthActivity.inputLat +"&date=2014-02-01&api_key=DEMO_KEY");
        //nasaEarth.execute();

        earthImageView = findViewById(R.id.earthImage);
        earthLatTextView = findViewById(R.id.earthlat);
        earthLonTextView = findViewById(R.id.earthlon);
        earthDateTextView = findViewById(R.id.earthdate);
        re = findViewById(R.id.reButton);
        saved = findViewById(R.id.save);


        ContentValues newRowValues= new ContentValues();

        earthProgressBar = findViewById(R.id.earthprogress);
        earthProgressBar.setVisibility(View.VISIBLE);

        Intent goToPrevious = new Intent(Nasaearth_result.this, NasaEarthActivity.class);
        re.setOnClickListener(click -> startActivity(goToPrevious));

        saved.setOnClickListener(click->{
            String inDate = earthDateTextView.getText().toString();
                db= NasaDayActivity.dbOpener.getWritableDatabase();
                newRowValues.put(NasaEarthMyOpener.COL_LATITUDE, NasaEarthActivity.inputLat);
                newRowValues.put(NasaEarthMyOpener.COL_LONGITUDE, NasaEarthActivity.inputLon );
                newRowValues.put(NasaEarthMyOpener.COL_DATE, inDate);

                long id= db.insert(NasaEarthMyOpener.TABLE_NAME,null, newRowValues);
                Toast.makeText(Nasaearth_result.this, getString(R.string.earthsaved) , Toast.LENGTH_LONG).show();

        });
    }
        private class NasaEarthImage extends AsyncTask<String, Integer, String> {
            String date=null, url=null, id=null;
            Bitmap image;

            public String doInBackground(String... args) {
                try {
                    //URL url = new URL();
                    URL infoUrl = new URL(args[0]);
                    HttpURLConnection urlConnection = (HttpURLConnection) infoUrl.openConnection();
                    InputStream response = urlConnection.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line= null;

                    while ((line = reader.readLine()) != null) {sb.append(line + "\n"); }
                    String result = sb.toString();
                    JSONObject json = new JSONObject(result);

                    id = json.getString("id");
                    publishProgress(25);
                    date= json.getString("date");
                    publishProgress(50);
                    url = json.getString("url");
                    publishProgress(75);

                    FileInputStream fis;
                    if(fileExistance(date + ".png")){
                        fis= openFileInput(date +".png");
                        image= BitmapFactory.decodeStream(fis);
                        Log.i("file", "this is the local file.");
                    }else{
                        URL urlImage= new URL(url);
                        HttpURLConnection imageConnection= (HttpURLConnection) urlImage.openConnection();
                        imageConnection.connect();
                        int responseCode= imageConnection.getResponseCode();
                        if(responseCode==200){
                            image= BitmapFactory.decodeStream(imageConnection.getInputStream());
                            Log.i("file", "this file is from online.");
                            FileOutputStream outputStream = openFileOutput( date + ".png", Context.MODE_PRIVATE );
                            image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                            publishProgress(100);
                            outputStream.flush();
                            outputStream.close();
                        }
                    }

                    return "Done";
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                }
                return "Done";
            }

            public void onProgressUpdate(Integer... value) {
                super.onProgressUpdate(value);
                earthProgressBar.setVisibility(View.VISIBLE);
                earthProgressBar.setProgress(value[0]);
            }

            public void onPostExecute(String fromDoInBackground) {
                super.onPostExecute(fromDoInBackground);
                earthImageView.setImageBitmap(image);
                earthLatTextView.setText(getString(R.string.earthlat) + NasaEarthActivity.inputLon);
                earthLonTextView.setText(getString(R.string.earthlon) + NasaEarthActivity.inputLat);
                earthDateTextView.setText(getString(R.string.earthdate) + date);
                earthProgressBar.setVisibility(View.INVISIBLE);
            }


        }
    public boolean fileExistance(String fname){
        File file= getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }



    }

