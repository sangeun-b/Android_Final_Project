package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Extract data from the specified http server and depend on the user search to show the news title in a list
 * @author Qi Wang
 * @version April 01, 2020
 */
public class Guardian_search_results extends AppCompatActivity {
    ListView titleListView;
    ArrayList<GuardianNews> list=new ArrayList<>();
    ProgressBar progressBar;
    public static final String TITLE = "TITLE";
    public static final String URL = "URL";
    public static final String SECTION = "SECTION NAME";
    public static final String ID= "ID";

    /**
     * Call this method when the activity is starting,and call MyHttpRequest to get the news information
     * Start with the tablet layout and put your listView on the left,and FrameLayout on the right.
     * For the phone, just have a listView.
     * For a phone, when a user selects something from a list, use startActivity() to go to a new activity that is only a FrameLayout.
     * @param savedInstanceState-a Bundle containing the activity's previously frozen state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guardian_search_results);

        boolean isTablet=findViewById(R.id.fragmentLocation)!=null;
        FrameLayout frameLayout = findViewById(R.id.fragmentLocation);

        titleListView = findViewById(R.id.guardian_list);
        MyHttpRequest req = new MyHttpRequest();
        req.execute("https://content.guardianapis.com/search?api-key=1fb36b70-1588-4259-b703-2570ea1fac6a&q=" + GuardianActivity.searchInput);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        titleListView.setOnItemClickListener((l, view, pos, id) -> {
            //Create a bundle to pass data to the new fragment
            Bundle dataToPass=new Bundle();
            dataToPass.putString(TITLE, list.get(pos).getTitle());
            dataToPass.putString(URL,list.get(pos).getUrl());
            dataToPass.putString(SECTION,list.get(pos).getSection());
            dataToPass.putLong(ID,id);


            if(isTablet){
                Guardian_details_fragment dFragment=new Guardian_details_fragment();//add a DetailFragment
                dFragment.setArguments(dataToPass);
                dFragment.setTablet(true);
                //pass it a bundle for information
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation,dFragment) //Add the fragment in FrameLayout
                        .commit(); //actually load the fragment.
            }

            else {//isPhone
                Intent nextActivity = new Intent(Guardian_search_results.this, GuardianEmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition
            }

    });
    }

    /**
     * use AsyncTask to retrieve data from an http server
     * has 3 important functions: doInBackground, onProgressUpdate, onPostExecute
     */
    private class MyHttpRequest extends AsyncTask<String, Integer, String> {
        //JSONObject jo;
        JSONArray jsa;

        /**
         * where you do your work
         * @param args-The first generic parameter is for the doInBackground array type.
         *             The “...” means variable arguments.
         * @return  an object of Type3, which represents your result.
         *          in this case it returns an arrayList of items which include news title, url and section name.
         */
               //Type3                Type1
        public String doInBackground(String... args) {
            try {
                //create a URL object of what server to contact:
                URL url = new URL(args[0]);
                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //wait for data:
                InputStream response = urlConnection.getInputStream();
                //JSON reading:
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string

                JSONObject jo1 = new JSONObject(result);
                //JSONObject jo2= jo1.getJSONObject("response");
                //String s= jo.getString("userTier");
                //Log.i("String s",s);
                jsa= jo1.getJSONObject("response").getJSONArray("results");
                for(int i=0; i<jsa.length(); i++){
                    JSONObject item = jsa.getJSONObject(i);
                    GuardianNews gd= new GuardianNews();
                    gd.setTitle(item.getString("webTitle"));

                    gd.setUrl(item.getString("webUrl"));
                    gd.setSection(item.getString("sectionName"));

                    list.add(gd);
                }

                publishProgress(25);
                publishProgress(50);
                publishProgress(75);
                publishProgress(100);

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
            }

            return "Done";
        }

        /**
         * from inside the doInBackground function to update your GUI.
         * set the progressBar attributes
         * @param value-The values indicating progress.
         */
        protected void onProgressUpdate(Integer... value) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(value[0]);
        }

        /**
         * this means that doInBackground has finished. You can do your final GUI update
         * @param fromDoInBackground-the exact same object that was returned by doInBackground.
         */
        protected void onPostExecute(String fromDoInBackground) {
            titleListView.setAdapter(new GuardianListAdapter());
            progressBar.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * same as Guardian_favourite class
     */
    private class GuardianListAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public GuardianNews getItem(int position)
        {
            return list.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View newView=convertView;
            LayoutInflater inflater = getLayoutInflater();
            GuardianNews gnews= getItem(position);
            newView=inflater.inflate(R.layout.guardian_searchresult_item, parent,false);
            TextView tView= newView.findViewById(R.id.guardian_search_title);
            tView.setText( gnews.getTitle());
            return newView;
        }

        //we return the object's database id
        @Override
        public long getItemId(int position)
        {
            return getItem(position).getId();
        }
    }

}