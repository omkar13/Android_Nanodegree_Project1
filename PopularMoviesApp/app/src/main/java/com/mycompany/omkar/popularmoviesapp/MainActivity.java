package com.mycompany.omkar.popularmoviesapp;


import android.os.Bundle;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.stetho.Stetho;

public class MainActivity extends AppCompatActivity {


    private Boolean mTabletMode = false;    //for tablet mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String TAG = "in MainActivity";

        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_main);


        //if tablet present, fragment_container1 is not null
        if(findViewById(R.id.fragment_container1)!=null)
            mTabletMode=true;


        FragmentManager fm = this.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        MainFragment frag = (MainFragment)fm.findFragmentByTag("main_fragment");


        if(frag == null) {
            Log.d(TAG , "frag is null");
            frag = new MainFragment();

            Bundle bundle = new Bundle();
            bundle.putBoolean("isTablet",mTabletMode);
            frag.setArguments(bundle);
            ft.add(R.id.fragment_container, frag , "main_fragment");
            ft.addToBackStack(null);
            ft.commit();
        }

        //documentation : http://facebook.github.io/stetho/
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());

    }

    @Override
    public void onBackPressed() {
        if(this.getFragmentManager().getBackStackEntryCount() != 0) {
            this.getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

}
