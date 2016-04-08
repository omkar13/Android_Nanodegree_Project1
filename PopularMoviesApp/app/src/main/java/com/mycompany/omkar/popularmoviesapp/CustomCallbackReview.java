package com.mycompany.omkar.popularmoviesapp;

/**
 * Created by omkar on 31/3/16.
 */

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class CustomCallbackReview implements Callback<Reviews> {
    private Reviews reviews;
    private List<ReviewPOJO> reviewList;
    private Movie[] movies;
    private Context context;
    private Toast toast;
    private int i;
    private GridView gridView;
    private Activity activity_context;
    private boolean mTabletMode;

    public CustomCallbackReview(Movie[] movies, int i, Context context, GridView g, Activity activity_context , boolean mTabletMode){
        this.movies = movies;
        this.i = i;
        this.context = context;
        this.gridView =g;
        this.activity_context = activity_context;
        this.mTabletMode = mTabletMode;
    }

    @Override
    public void onResponse(Response<Reviews> response) {
        try {
            reviews = response.body();
            reviewList = reviews.getResults();
            movies[i].setReviewList(reviewList);

            if(i==19){
                //if its the last call update the view.
                updateAdapter();
            }

        } catch (NullPointerException e) {

            if (response.code() == 401) {
                toast = Toast.makeText(context.getApplicationContext(), "Unauthenticated", Toast.LENGTH_SHORT);
            } else if (response.code() >= 400) {
                toast = Toast.makeText(context.getApplicationContext(), "Client Error " + response.code()
                        + " " + response.message(), Toast.LENGTH_SHORT);
            }
            toast.show();

        }

    }

    @Override
    public void onFailure(Throwable t) {
        Log.e("getReviews threw: ", t.getMessage());
    }

    public void updateAdapter(){

        CustomAdapterForGridView adapter = new CustomAdapterForGridView(context, Arrays.asList(movies));

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Toast t = Toast.makeText(context,"i am at postition :" + position,Toast.LENGTH_SHORT);
                //t.show();
                FragmentManager fm = activity_context.getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                DetailsFragment fragment = new DetailsFragment();
                fragment.setMovie(movies[position], context);

                if(!mTabletMode)
                    ft.replace(R.id.fragment_container, fragment, "details_fragment");
                else
                    ft.replace(R.id.fragment_container1, fragment, "details_fragment");
                ft.addToBackStack(null);
                ft.commit();


            }
        });



    }


}