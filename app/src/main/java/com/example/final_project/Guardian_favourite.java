package com.example.final_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Guardian_favourite extends AppCompatActivity {
    private ArrayList<GuardianNews> list=new ArrayList<>();
    private MyListAdapter myAdapter;
    SQLiteDatabase db;

    public static final String TITLE = "TITLE";
    public static final String URL = "URL";
    public static final String SECTION = "SECTION NAME";
    public static final String ID= "ID";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guardian_favourite_list);

        ListView guardianList = findViewById(R.id.guardian_favourite);
        FrameLayout frameLayout = findViewById(R.id.fragmentLocation);
        loadDataFromDatabase();

        myAdapter = new MyListAdapter();
        guardianList.setAdapter(myAdapter);


        guardianList.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Do you want to delete this?")
                    .setMessage("The selected row is:" + (position + 1) + "\n The database id is: " + id)
                    .setPositiveButton("Yes", (click, arg) -> {
                        if (frameLayout != null) {
                            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                                if (fragment.getArguments().getLong(ID) == Long.valueOf(myAdapter.getItemId(position))) {
                                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                                    break;
                                }
                            }
                        }
                        list.remove(position);
                        db.delete(GuardianMyOpener.TABLE_NAME, GuardianMyOpener.COL_ID + "= ?", new String[]{Long.toString(id)});
                        myAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", (click, arg) -> {
                    })

                    .create().show();

            return true;

        });

        guardianList.setOnItemClickListener((l, view, pos, id) -> {
            //Create a bundle to pass data to the new fragment
            Bundle dataToPass = new Bundle();
            dataToPass.putString(TITLE, list.get(pos).getTitle());
            dataToPass.putString(URL, list.get(pos).getUrl());
            dataToPass.putString(SECTION, list.get(pos).getSection());
            dataToPass.putLong(ID, id);

            //for phone and tablet
            Intent nextActivity = new Intent(Guardian_favourite.this, Guardian_favourite_empty.class);
            nextActivity.putExtras(dataToPass); //send data to next activity
            startActivity(nextActivity); //make the transition



        });
    }
        private class MyListAdapter extends BaseAdapter {
            public int getCount() {
                return list.size();
            }

            public GuardianNews getItem(int position) {
                return list.get(position);
            }

            public long getItemId(int position) {
                return getItem(position).getId();
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                View newView = convertView;
                GuardianNews news = getItem(position);
                LayoutInflater inflater = getLayoutInflater();
                newView = inflater.inflate(R.layout.guardian_favouritelist_item, parent, false);
                TextView tView = newView.findViewById(R.id.guardian_favouritelist_item);
                tView.setText(news.getTitle());
                return newView;

            }

        }


    public void loadDataFromDatabase(){
        //get a database connection:
        GuardianMyOpener guardianDB= new GuardianMyOpener(this);
        db = guardianDB.getWritableDatabase();
        //db=GuardianActivity.dbOpener.getWritableDatabase();
        String[]columns={GuardianMyOpener.COL_ID,GuardianMyOpener.COL_TITLE,GuardianMyOpener.COL_URL,GuardianMyOpener.COL_SECTION};
        Cursor results = db.query(false, GuardianMyOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int titleColIndex = results.getColumnIndex(GuardianMyOpener.COL_TITLE);
        int urlColIndex = results.getColumnIndex(GuardianMyOpener.COL_URL);
        int sectionColIndex = results.getColumnIndex(GuardianMyOpener.COL_SECTION);
        int idColIndex = results.getColumnIndex(GuardianMyOpener.COL_ID);

        while(results.moveToNext())
        {
            String title = results.getString(titleColIndex);
            String url = results.getString(urlColIndex);
            String section = results.getString(sectionColIndex);
            long id = results.getLong(idColIndex);


            //add the new Message to the array list:
            //Message m=new Message(message,send,id);
            list.add((new GuardianNews(title,url,section,id)));
        }



    }


}
