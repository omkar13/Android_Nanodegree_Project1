package com.mycompany.omkar.popularmoviesapp;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycompany.omkar.popularmoviesapp.data.FavouriteMoviesContract;

/**
 * Created by omkar on 4/4/16.
 */
public class DetailFragmentFavMovie extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private Cursor mDetailCursor;
    private View mRootView;
    private int mPosition;
    private ImageView mImageView;
   // private TextView mTextView;
    //private TextView mUriText;
    private TextView title;

    private TextView release_date;
    private TextView overview;
    private TextView votes ;

    private Uri mUri;
    private static final int CURSOR_LOADER_ID = 0;



public static DetailFragmentFavMovie newInstance(int position , Uri uri){

    DetailFragmentFavMovie fragment = new DetailFragmentFavMovie();

    Bundle args = new Bundle();
    fragment.mPosition = position;
    fragment.mUri = uri;
    args.putInt("id", position);
    fragment.setArguments(args);
    return fragment;

}

    public DetailFragmentFavMovie(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        mImageView = (ImageView) rootView.findViewById(R.id.details_image);
        title = (TextView) rootView.findViewById(R.id.details_title);
        release_date = (TextView) rootView.findViewById(R.id.details_release_date);
        overview = (TextView) rootView.findViewById(R.id.details_overview);
        votes = (TextView) rootView.findViewById(R.id.details_vote_average);


        Bundle args = this.getArguments();
        getLoaderManager().initLoader(CURSOR_LOADER_ID, args, DetailFragmentFavMovie.this);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        String selection = null;
        String [] selectionArgs = null;
        if (args != null){
            selection = FavouriteMoviesContract.FavMoviesEntry._ID;
            selectionArgs = new String[]{String.valueOf(mPosition)};
        }
        return new CursorLoader(getActivity(),
                mUri,
                null,
                selection,
                selectionArgs,
                null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }

    // Set the cursor in our CursorAdapter once the Cursor is loaded
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mDetailCursor = data;
        mDetailCursor.moveToFirst();
        DatabaseUtils.dumpCursor(data);

        title.setText("Title: " + mDetailCursor.getString(1) + "\n");
        release_date.setText("Release date: " +mDetailCursor.getString(2) + "\n");
        overview.setText("Plot synopsis: " + mDetailCursor.getString(5) + "\n");
        votes.setText("Vote average: " + Double.toString(mDetailCursor.getDouble(4)) + "\n");


        //mImageView.setImageResource(mDetailCursor.getInt(3));
        //mTextView.setText(mDetailCursor.getString(2));
        // set Uri to be displayed
        //mUriText.setText(mUri.toString());
    }

    // reset CursorAdapter on Loader Reset
    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        mDetailCursor = null;
    }


}
