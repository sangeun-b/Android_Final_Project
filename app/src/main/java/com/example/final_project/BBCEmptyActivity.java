package com.example.final_project;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * This class extends AppCompatActivity. With the fragment manager object, begin a FragmentTransaction to either add, replace, or remove a fragment.
 * @author Xin Guo
 * @version 1.0
 */
public class BBCEmptyActivity extends AppCompatActivity{
    /**
     *  It sets the content view with an empty layout at first.
     *  When you select something from the ListView, you should load the Fragment into the FrameLayout.
     *  To load a fragment, you need a FragmentManager object to remove any Fragments that are currently there before adding the new fragment.
     * @param savedInstanceState a reference to a Bundle object that is passed into the onCreate method of every Android Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbcempty);

        Bundle dataToPass = getIntent().getExtras();
        BBCDetailsFragment fragment = new BBCDetailsFragment();
        fragment.setArguments(dataToPass);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentLocation, fragment)
                .commit();
    }
}
