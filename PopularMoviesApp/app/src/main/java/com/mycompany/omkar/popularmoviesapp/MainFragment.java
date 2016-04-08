package com.mycompany.omkar.popularmoviesapp;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

import com.mycompany.omkar.popularmoviesapp.MovieService.MovieAPI;
import com.mycompany.omkar.popularmoviesapp.data.FavouriteMoviesContract;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{


    private GridView gridView;
    private String TAG = "in MainFragment";
    private Movie[] movies = new Movie[20];

    private final int CURSOR_LOADER_ID = 0;
    private MovieAdapterForFavourites mMovieAdapter;

    private int sort_order = 1 ;//by default sort by popularity

    private Boolean mTabletMode = false;    //for tablet mode


    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "on Create");
        Bundle args = getArguments();
        mTabletMode = args.getBoolean("isTablet");
        setHasOptionsMenu(true);
        setRetainInstance(true);        //retains the fragment on a configuration change
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       Log.e(TAG, "on create view called");
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
        inflater.inflate(R.menu.options_menu, menu);
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

            case R.id.favourites:
                sort_order = 3;
                updateGridViewWithFavourites();
                break;
            default:

        }
    return true;
    }

    /*called when we need to update gridview from favourites database*/
    private void updateGridViewWithFavourites(){

         mMovieAdapter = new MovieAdapterForFavourites(getActivity(), null , 0 , CURSOR_LOADER_ID );
        gridView.setAdapter(mMovieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FragmentManager fm = getActivity().getFragmentManager();


                FragmentTransaction ft = fm.beginTransaction();

                //DetailsFragment fragment = new DetailsFragment();

                //to be completed. handle two cases -online and offline seperately.

                //make a query operation using cursor and get the specific movie details and pass them to the
                //detail fragment

                //fragment.setMovie(movies[position], context);


                // increment the position to match Database Ids indexed starting at 1
                int uriId = position + 1;
                // append Id to uri
                Uri uri = ContentUris.withAppendedId(FavouriteMoviesContract.FavMoviesEntry.CONTENT_URI,
                        uriId);

                DetailFragmentFavMovie detailFragmentFavMovie = DetailFragmentFavMovie.newInstance( uriId,uri);

                if(!mTabletMode)
                    ft.replace(R.id.fragment_container, detailFragmentFavMovie, "details_fragment");
                else
                    ft.replace(R.id.fragment_container1, detailFragmentFavMovie, "details_fragment");

                ft.addToBackStack(null);
                ft.commit();


            }
        });

        //make arrangements for the loader

        getLoaderManager().initLoader(CURSOR_LOADER_ID ,null , this);




    }


    /*called whenever gridview is changed e.g. sorting order is changed to by rating or by popularity.*/
    private void updateGridView(int i){
        // i specifies sorting order.
//        new AsyncTaskForFetchingData(getContext() , gridView).execute(i);

        Retrofit r = new Retrofit.Builder()
                    .baseUrl(getActivity().getString(R.string.base_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        String sorting_order;

        if(i == 1)                                                                        // by popularity
            sorting_order = getActivity().getString(R.string.sorting_order_by_popularity) ;
        else                                                                             //by rating
            sorting_order = getActivity().getString(R.string.sorting_order_by_rating);

        String myApiKey = getActivity().getString(R.string.api_key_tmdb) ;

        MovieAPI movieAPI = r.create(MovieAPI.class);

        Call<MovieList> call;


        call = movieAPI.getMovies(myApiKey , sorting_order);
        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Response<MovieList> response) {

                try {
                    //store the movies information


                    MovieList movieList;
                    movieList = response.body();
                    List<MoviePOJO> list = movieList.getResults();
                    String baseUrlForPoster = "http://image.tmdb.org/t/p/";

                    for (int i = 0; i < list.size(); i++) {
                        //copy required data to movie list.
                        MoviePOJO moviePOJO = list.get(i);
                        String completeUrlForPoster = baseUrlForPoster + "w185" + moviePOJO.getPoster_path();
                        movies[i] = new Movie(moviePOJO.getTitle(), moviePOJO.getRelease_date(), completeUrlForPoster,
                                moviePOJO.getVote_average(), moviePOJO.getOverview(), moviePOJO.getId());

                    }

                    //now make network calls to get the review and trailers to each movie.
                    //after they are done update the adapter.


                    //network calls for the review info
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(getActivity().getString(R.string.base_url))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    MovieReviewService.MovieReviewAPI movieReviewAPI = retrofit.create(MovieReviewService.MovieReviewAPI.class);

                    Call<Reviews> call;

                    for (int i = 0; i < movies.length; i++) {
                        call = movieReviewAPI.getReviews(movies[i].getId(), getActivity().getString(R.string.api_key_tmdb));
                        call.enqueue(new CustomCallbackReview(movies, i, getActivity(), gridView, (FragmentActivity) getActivity(),mTabletMode));

                    }


                    //now we make network calls for the trailers info.
                    Retrofit retrofit1 = new Retrofit.Builder()
                            .baseUrl(getActivity().getString(R.string.base_url))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    MovieTrailerService.MovieTrailerAPI movieTrailerAPI = retrofit1.create(MovieTrailerService.MovieTrailerAPI.class);

                    Call<Trailers> call1;

                    for (int i = 0; i < movies.length; i++) {
                        call1 = movieTrailerAPI.getTrailers(movies[i].getId(), getString(R.string.api_key_tmdb));
                        call1.enqueue(new CustomCallbackTrailer(movies, i, getActivity(), gridView, (FragmentActivity) getActivity(),mTabletMode));

                    }

                } catch (NullPointerException e) {

                    Toast toast = null;
                    if (response.code() == 401) {
                        toast = Toast.makeText(getActivity().getApplicationContext(), "Unauthenticated", Toast.LENGTH_SHORT);
                    } else if (response.code() >= 400) {
                        toast = Toast.makeText(getActivity().getApplicationContext(), "Client Error " + response.code()
                                + " " + response.message(), Toast.LENGTH_SHORT);
                    }
                    toast.show();

                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("getMovies threw: ", t.getMessage());
            }
        });


    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){

        return  new CursorLoader(getActivity(), FavouriteMoviesContract.FavMoviesEntry.CONTENT_URI, null,null,null,null);


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }

    // Set the cursor in our CursorAdapter once the Cursor is loaded
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mMovieAdapter.swapCursor(data);

    }

    // reset CursorAdapter on Loader Reset
    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        mMovieAdapter.swapCursor(null);
    }


}
