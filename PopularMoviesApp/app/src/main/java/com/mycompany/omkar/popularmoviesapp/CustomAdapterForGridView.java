package com.mycompany.omkar.popularmoviesapp;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by omkar on 22/2/16.
 */
public class CustomAdapterForGridView extends ArrayAdapter<Movie> {

    private static final String LOG_TAG = "In the adapter";
    private final Context context;

    public CustomAdapterForGridView(Context context, List<Movie> movieList) {


        super(context, 0, movieList);

        Log.d(LOG_TAG, "adapter created.");
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d(LOG_TAG , "get view called at position :" + position);
        Movie movie = getItem(position);


        ImageView imageView;

        if (convertView == null) {

            // if it's not recycled, initialize some attributes
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(400, 300));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
           // imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }


        Picasso.with(context).load(movie.getUrl()).into(imageView);

        return imageView;
    }
}