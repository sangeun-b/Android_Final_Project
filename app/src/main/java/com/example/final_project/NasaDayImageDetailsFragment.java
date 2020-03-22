package com.example.final_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class NasaDayImageDetailsFragment extends Fragment {

    private boolean isTablet;
    private AppCompatActivity parentActivity;
    private Bitmap image;

    public void setTablet(boolean tablet) {
        isTablet = tablet;
    }

    public NasaDayImageDetailsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.nasadayimage_fragment_details, container, false);
        Bundle dataFromActivity = getArguments();
        FileInputStream fis;
        String path= dataFromActivity.getString(NasaDayImageMyfavoriteList.ITEM_TITLE);
        File file= getActivity().getBaseContext().getFileStreamPath(path+ ".png");
        if (file.exists()) {
            try {
                fis = getActivity().openFileInput(path + ".png");
                image = BitmapFactory.decodeStream(fis);
                ImageView imagefragment= (ImageView) result.findViewById(R.id.nasaDayImageFragment);
                imagefragment.setImageBitmap(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        /*byte[] byteArray = dataFromActivity.getByteArray(NasaDayImageMyfavoriteList.ITEM_IMAGE);
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ImageView imagefragment= (ImageView) result.findViewById(R.id.nasaDayImageFragment);
        imagefragment.setImageBitmap(bmp);*/

        TextView datefragment = (TextView) result.findViewById(R.id.myfavoriteDateFragment);
        datefragment.setText("DATE: " + dataFromActivity.getString(NasaDayImageMyfavoriteList.ITEM_DATE));

        TextView titlefragment = (TextView) result.findViewById(R.id.myfavoriteTitleFragment);
        titlefragment.setText("TITLE: " + dataFromActivity.getString(NasaDayImageMyfavoriteList.ITEM_TITLE));

        TextView urlfragment = (TextView) result.findViewById(R.id.myfavoriteUrlFragment);
        urlfragment.setText("URL: " + dataFromActivity.getString(NasaDayImageMyfavoriteList.ITEM_URL));

        TextView hdurlfragment = (TextView) result.findViewById(R.id.myfavoriteHDUrlFragment);
        hdurlfragment.setText("HDURL: " + dataFromActivity.getString(NasaDayImageMyfavoriteList.ITEM_HDURL));

        Button hideButton = result.findViewById(R.id.NasaDayFragmentButton);
        hideButton.setOnClickListener(clk -> {
            if (isTablet) { //both the list and details are on the screen
                NasaDayImageMyfavoriteList parent = (NasaDayImageMyfavoriteList) getActivity();
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
            //for phone:
            //You are only looking at the details, you need to go back to the previous list page
            else {
                NasaDayImageEmptyActivity parent = (NasaDayImageEmptyActivity) getActivity();
                Intent backToNasaDayImageMyfavoriteList = new Intent();
                backToNasaDayImageMyfavoriteList.putExtra(NasaDayImageMyfavoriteList.ITEM_ID, dataFromActivity.getLong(NasaDayImageMyfavoriteList.ITEM_ID));
                parent.setResult(Activity.RESULT_OK, backToNasaDayImageMyfavoriteList); //send data back to NasaDayImageMyfavoriteList Activity in onActivityResult()
                parent.finish();
            }
        });
        return result;
    }

        public void onAttach(Context context){
        super.onAttach(context);
        parentActivity = (AppCompatActivity)context;
    }
}
