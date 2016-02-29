package com.mycompany.omkar.popularmoviesapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

private GridView gridView;
private String TAG = "in MainFragment";
    private int sort_order = 1 ;//by default sort by popularity

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "on Create");
        setHasOptionsMenu(true);
        setRetainInstance(true);        //retains the fragment on a configuration change
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       Log.e(TAG , "on create view called");
        View v = inflater.inflate(R.layout.fragment_main, container, false);
       // setHasOptionsMenu(true);
        gridView = (GridView)v.findViewById(R.id.posters_grid);
        updateGridView(sort_order);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.options_menu , menu);
        return ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.by_popularity :
                sort_order = 1;
                updateGridView(1);
                break;

            case R.id.by_rating:
                sort_order = 2;
                updateGridView(2);
                break;

            default:

        }
    return true;
    }

    private void updateGridView(int i){
        // i specifies sorting order.
        new AsyncTaskForFetchingData(getContext() , gridView).execute(i);
    }
}
