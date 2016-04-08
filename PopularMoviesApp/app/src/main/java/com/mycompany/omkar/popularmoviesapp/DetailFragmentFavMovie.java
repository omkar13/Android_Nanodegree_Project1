package com.mycompany.omkar.popularmoviesapp;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mycompany.omkar.popularmoviesapp.data.FavouriteMoviesContract;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by omkar on 4/4/16.
 */
public class DetailFragmentFavMovie extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private Cursor mDetailCursor;
    private View mRootView;
    private int mPosition;
    private ImageView mImageView;
    // private TextView mTextView;
    //private TextView mUriText;
    private TextView title;

    private TextView release_date;
    private TextView overview;
    private TextView votes;

    private Reviews reviews;
    private List<ReviewPOJO> reviewList;

    private Trailers trailers;
    private List<TrailerPOJO> trailerList;


    private Toast toast;
    private Uri mUri;
    private static final int CURSOR_LOADER_ID = 0;


    public static DetailFragmentFavMovie newInstance(int position, Uri uri) {

        DetailFragmentFavMovie fragment = new DetailFragmentFavMovie();

        Bundle args = new Bundle();
        fragment.mPosition = position;
        fragment.mUri = uri;
        args.putInt("id", position);
        fragment.setArguments(args);
        return fragment;

    }

    public DetailFragmentFavMovie() {

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
        mRootView = rootView.findViewById(R.id.relative_layout);

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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = null;
        String[] selectionArgs = null;
        if (args != null) {
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    // Set the cursor in our CursorAdapter once the Cursor is loaded
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mDetailCursor = data;
        mDetailCursor.moveToFirst();
        DatabaseUtils.dumpCursor(data);

        title.setText("Title: " + mDetailCursor.getString(1) + "\n");
        release_date.setText("Release date: " + mDetailCursor.getString(2) + "\n");
        overview.setText("Plot synopsis: " + mDetailCursor.getString(5) + "\n");
        votes.setText("Vote average: " + Double.toString(mDetailCursor.getDouble(4)) + "\n");

        //if internet connection is present, load the poster as well
        Picasso.with(getActivity()).load(mDetailCursor.getString(3)).into(mImageView);


        //try loading the trailer and review info if internet is present

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getActivity().getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieReviewService.MovieReviewAPI movieReviewAPI = retrofit.create(MovieReviewService.MovieReviewAPI.class);

        Call<Reviews> call;

        call = movieReviewAPI.getReviews(mDetailCursor.getInt(6), getActivity().getString(R.string.api_key_tmdb));

        call.enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(Response<Reviews> response) {

                try {
                    reviews = response.body();
                    reviewList = reviews.getResults();
                    if (reviewList != null) {
                        for (int i = 0; i < reviewList.size(); i++) {

                            TextView review = new TextView(getActivity());
                            review.setText("Review no : " + i + "\n" + reviewList.get(i).getContent() + "\n\n\n");
                            ((ViewGroup) mRootView).addView(review, -1);

                        }
                    }
                } catch (NullPointerException e) {

                    if (response.code() == 401) {
                        toast = Toast.makeText(getActivity(), "Unauthenticated", Toast.LENGTH_SHORT);
                    } else if (response.code() >= 400) {
                        toast = Toast.makeText(getActivity(), "Client Error " + response.code()
                                + " " + response.message(), Toast.LENGTH_SHORT);
                    }
                    toast.show();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

        //now we make network calls for the trailers info. if internet is present


        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(getActivity().getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieTrailerService.MovieTrailerAPI movieTrailerAPI = retrofit1.create(MovieTrailerService.MovieTrailerAPI.class);

        Call<Trailers> call1;

        call1 = movieTrailerAPI.getTrailers(mDetailCursor.getInt(6), getString(R.string.api_key_tmdb));

        call1.enqueue(new Callback<Trailers>() {
            @Override
            public void onResponse(Response<Trailers> response) {


                try {

                    trailers = response.body();
                    trailerList = trailers.getResults();


                    if (trailerList != null) {
                        for (int i = 0; i < trailerList.size(); i++) {

                            TextView trailer = new TextView(getActivity());
                            trailer.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                            trailer.setText("Trailer no : " + i + " link" + "\n");

                            trailer.setOnClickListener(new myCustomOnClickListener(i, trailerList));

                            ((ViewGroup) mRootView).addView(trailer, -1);

                        }
                    }

                } catch (NullPointerException e) {
                    if (response.code() == 401) {
                        toast = Toast.makeText(getActivity(), "Unauthenticated", Toast.LENGTH_SHORT);
                    } else if (response.code() >= 400) {
                        toast = Toast.makeText(getActivity(), "Client Error " + response.code()
                                + " " + response.message(), Toast.LENGTH_SHORT);
                    }
                    toast.show();

                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });


    }

    // reset CursorAdapter on Loader Reset
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mDetailCursor = null;
    }

    public class myCustomOnClickListener implements View.OnClickListener {

        int i;
        List<TrailerPOJO> list1;


        public myCustomOnClickListener(int i, List<TrailerPOJO> list1) {
            this.i = i;
            this.list1 = list1;
        }

        @Override
        public void onClick(View v) {
            TrailerPOJO trailerPOJO;
            trailerPOJO = list1.get(i);
            String url = "https://www.youtube.com/watch?v=" + trailerPOJO.getKey();


            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);

        }
    }

}
