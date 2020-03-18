package com.example.final_project;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GuardianActivity extends AppCompatActivity {
    TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian);

        Button search = findViewById(R.id.search_button);
        Intent goToSearch = new Intent(GuardianActivity.this, Guardian_search_results.class);
        search.setOnClickListener(click -> startActivity(goToSearch));

        ImageButton favourite = findViewById(R.id.favouriteList);
        Intent goToFavourite = new Intent(GuardianActivity.this, Guardian_favourite.class);
        favourite.setOnClickListener(click -> startActivity(goToFavourite));


        title = findViewById(R.id.guardian_news);
        MyHttpRequest req = new MyHttpRequest();
        req.execute("https://content.guardianapis.com/search?api-key=1fb36b70-1588-4259-b703-2570ea1fac6a&q=Tesla");
    }

        private class MyHttpRequest extends AsyncTask<String, Integer, String>{
          String newsTitle="";

                   //Type3                Type1
            public String doInBackground(String ... args){
                try{
                    //create a URL object of what server to contact:
                    URL url=new URL(args [0]);

                    //open the connection
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    //wait for data:
                    InputStream response = urlConnection.getInputStream();

                    //JSON reading:
                    //Build the entire string response:
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    String result = sb.toString(); //result is the whole string

                    // convert string to JSON:
                    JSONObject titleReport = new JSONObject(result);

                     newsTitle=titleReport.getString("tilte");

                }catch(Exception e){
                    Log.e("Error",e.getMessage());
                }

                return "Done";

            }

            protected void onPostExecute(String fromDoInBackground) {
             title.setText("The news title is:"+ newsTitle);
            }



        }


    }


