package com.example.final_project;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


/**
 * A simple {@link Fragment} subclass.
 */
public class NasaEarthDetailsFragment extends Fragment {
    private boolean isTablet;
    //private AppCompatActivity parentActivity;
    private Bundle dataFromActivity;
    private Bitmap image;

    public NasaEarthDetailsFragment() {
        // Required empty public constructor
    }

    public void setTablet(boolean tablet){
        isTablet = tablet;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dataFromActivity = getArguments();
        FileInputStream fis;
        String path = dataFromActivity.getString(Nasaearth_saved.EARTH_DATE);
        View result = inflater.inflate(R.layout.nasaearth_details_fragment, container, false);
        File file = getActivity().getBaseContext().getFileStreamPath(path + ".png");
        if (file.exists()) {
            try {
                fis = getActivity().openFileInput(path + ".png");
                image = BitmapFactory.decodeStream(fis);
                ImageView earthDetailImage = result.findViewById(R.id.detailEarthImage);
                earthDetailImage.setImageBitmap(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        TextView earthDetailLat = result.findViewById(R.id.detailEarthLat);
        earthDetailLat.setText(getString(R.string.earthlat)+dataFromActivity.getString(Nasaearth_saved.EARTH_LATITUDE));

        TextView earthDetailLon = result.findViewById(R.id.detailEarthLon);
        earthDetailLon.setText(getString(R.string.earthlon)+dataFromActivity.getString(Nasaearth_saved.EARTH_LONGITUDE));

        TextView earthDetailDate = result.findViewById(R.id.detailEarthDate);
        earthDetailDate.setText(getString(R.string.earthdate)+dataFromActivity.getString(Nasaearth_saved.EARTH_DATE));

        Button hButton = result.findViewById(R.id.detailEarthHide);
        hButton.setOnClickListener(btn -> {
            if (isTablet){
                Nasaearth_saved parent = (Nasaearth_saved) getActivity();
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();

            }else{
                NasaEarthEmpty parent = (NasaEarthEmpty) getActivity();
                Intent backToNasaearthSaved = new Intent();
                backToNasaearthSaved.putExtra(Nasaearth_saved.EARTH_ID,dataFromActivity.getLong(Nasaearth_saved.EARTH_ID));
                parent.setResult(Activity.RESULT_OK, backToNasaearthSaved); //send data back to FragmentExample in onActivityResult()
                parent.finish(); //go back
            }
        });

        return result;
    }
}
