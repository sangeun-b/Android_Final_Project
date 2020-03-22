package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;


import com.google.android.material.snackbar.Snackbar;

public class Nasaearth_saved extends AppCompatActivity {
    ListView savedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasaearth_saved);
        View v1 = findViewById(R.id.savedList);
        Snackbar snackbar = Snackbar.make(v1, getString(R.string.earthdeleteIn), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(getString(R.string.earthokay), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();

        savedList = findViewById(R.id.savedList);

        savedList.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.earthdelete))
                    //.setMessage(getString(R.string.select)+(position+1) + "\n"+getString(R.string.db)+id)
                    .setPositiveButton(getString(R.string.earthyes), (click,arg)-> {
                        //chatArray.remove(position);
                        //myAdapter.notifyDataSetChanged();

                    })
                    .setNegativeButton(getString(R.string.earthno), (click,arg) -> {

                    }).create().show();

            return true ;
        });

    }
}
