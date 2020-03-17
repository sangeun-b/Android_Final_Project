package com.example.final_project;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class NasaDayActivity extends FragmentActivity {

    private DatePickerFragment datePickerFragment;
    private Intent goToImage;
    private static EditText textDate;
    static SharedPreferences prefs = null;
    static NasaDayImageMyOpener dbOpener;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasaday);

        textDate= findViewById(R.id.date);
        prefs = getSharedPreferences("NasaDayImage", Context.MODE_PRIVATE);
        String savedString = prefs.getString("date", " ");
        textDate.setText("The date you pick is "+ savedString);

        dbOpener= new NasaDayImageMyOpener(this);
        db= dbOpener.getWritableDatabase();

        datePickerFragment = new DatePickerFragment();
        Button dateButton = findViewById(R.id.dateButton);
        dateButton.setOnClickListener(click -> {
            new DatePickerFragment().show(getSupportFragmentManager(), "datePicker");
        });

        Button vieImageButton = findViewById(R.id.viewImageButton);
        goToImage = new Intent(this, NasaDayImage.class);
        vieImageButton.setOnClickListener(click -> {
            startActivity(goToImage);
        });

    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private static final String TAG = "datePicker";
        public static String date;
        final Calendar cal = Calendar.getInstance();

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, day);
            date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(cal.getTime());
            Log.d(TAG, "onDateSet: " + date);
            //new NasaDayActivity().setDate(date);
            textDate.setText("The date you pick is " + date);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("date", date);
            editor.commit();
            //https://www.truiton.com/2013/03/android-pick-date-time-from-edittext-onclick-event/
        }
    }
}