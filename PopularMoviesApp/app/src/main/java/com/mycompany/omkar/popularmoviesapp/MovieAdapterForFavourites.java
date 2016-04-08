package com.mycompany.omkar.popularmoviesapp;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycompany.omkar.popularmoviesapp.data.FavouriteMoviesContract;

/**
 * Created by omkar on 3/4/16.
 */
public class MovieAdapterForFavourites extends CursorAdapter{

    private final String LOG_TAG = MovieAdapterForFavourites.class.getSimpleName();


    public MovieAdapterForFavourites(Context context , Cursor c , int flags , int loaderID){
        super(context , c , flags);
        Log.d(LOG_TAG , "MovieAdapterForFavourites constructor");

    }

    public static class ViewHolder{
        //public final ImageView imageView;
        public final TextView textView;

        public ViewHolder(View view){
            textView = (TextView ) view;
        }

    }


    //will have to be changed . Instead of returning an image view, return a view containing image view and
    //text view.

    @Override
    public View newView(Context context , Cursor cursor , ViewGroup parent){

        Log.d(LOG_TAG, "in new view");

        //ImageView imageView = new ImageView(context);
        //imageView.setLayoutParams(new GridView.LayoutParams(400, 300));
        //imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        TextView textView = new TextView(context);

        ViewHolder viewHolder = new ViewHolder(textView);

        textView.setTag(viewHolder);

        return textView;
    }

    @Override
    public void bindView(View view , Context context , Cursor cursor){

        ViewHolder viewHolder = (ViewHolder)view.getTag();

        Log.d(LOG_TAG , "in bind view");

  //       to be completed
//make a query for the title if in offline mode

        int titleIndex = cursor.getColumnIndex(FavouriteMoviesContract.FavMoviesEntry.COLUMN_TITLE);

        String title = cursor.getString(titleIndex);
        viewHolder.textView.setText(title + "\n");
    }


}
