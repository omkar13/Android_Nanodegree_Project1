package com.mycompany.omkar.popularmoviesapp;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String TAG = "in MainActivity";

        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_main);


        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();

        MainFragment frag = (MainFragment)fm.findFragmentByTag("main_fragment");


        if(frag == null) {
            Log.d(TAG , "frag is null");
            frag = new MainFragment();
            ft.add(R.id.fragment_container, frag , "main_fragment");
            ft.addToBackStack(null);
            ft.commit();
        }

    }

}
