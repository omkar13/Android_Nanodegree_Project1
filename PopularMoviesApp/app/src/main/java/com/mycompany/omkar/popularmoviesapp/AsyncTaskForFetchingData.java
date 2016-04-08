package com.mycompany.omkar.popularmoviesapp;

/*
* This class is no longer being used as retrofit library is being used for network calls.
*
* */





import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

import com.mycompany.omkar.popularmoviesapp.MovieReviewService.MovieReviewAPI;
/**
 * Created by omkar on 22/2/16.
 */
public class AsyncTaskForFetchingData extends AsyncTask<Integer,Void , Void>{

    private Movie[] movies = new Movie[20];
    private Context context;
    private FragmentActivity activity_context;
    private GridView gridView;
    private String TAG = "AsyncTask" ;
    private boolean internetAccess;
    public static String BASE_URL_REVIEWS ;
    public static String BASE_URL_TRAILERS;

    private Call<Reviews> call;


    public AsyncTaskForFetchingData(Context context, GridView gridView){
        this.context = context;
        BASE_URL_REVIEWS = context.getString( R.string.base_url);
        BASE_URL_TRAILERS = context.getString( R.string.base_url);
        this.activity_context = (FragmentActivity) context ;
        this.gridView = gridView;
        this.internetAccess = true;
    }

  /*we will make 3 api calls. One for the basic movie info. Another one for the trailers info
  * and the last one for the reviews info.
  * */
    @Override
    protected Void doInBackground(Integer... arr) {

        String sorting_order;

        Log.d(TAG , "do in background starting" );
        if(arr[0] == 1)     // by popularity
            sorting_order = context.getString(R.string.sorting_order_by_popularity) ;
        else                //by rating
            sorting_order = context.getString(R.string.sorting_order_by_rating);

        final String FETCH_MOVIE_BASE_URL = context.getString(R.string.base_url_movie_data);
        final String SORT_BY = "sort_by";
        final String API_KEY = "api_key";
        final String myApiKey = context.getString(R.string.api_key_tmdb) ;
        final String TAG = "Inside AsyncTask";

        final String url_string_basic_movie_info = FETCH_MOVIE_BASE_URL + SORT_BY + "=" + sorting_order + "&" + API_KEY + "=" + myApiKey ;


        InputStream is =null;

        try{
            URL url = new URL(url_string_basic_movie_info);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // start query

            conn.connect();
            int response = conn.getResponseCode();
            Log.d(TAG , "the response of connection is : " + response);

            is = conn.getInputStream();

            //convert inputstream into a string

            String contentAsString = convertInputToString(is);

            // now let us parse the json
            if(contentAsString!=null)
                parseJSON(contentAsString);
            else {
                Log.e(TAG , "json string null");
                return null;
                }

            conn.disconnect();
        }
        catch (java.net.MalformedURLException excep){
            Log.e(TAG,"url malformed");
        }
        catch (java.io.IOException excep){
            Log.e(TAG,"problem in opening connection");
            internetAccess = false;
        }
        catch (org.json.JSONException excep){
            Log.e(TAG , "json exception");
            internetAccess = false;
        }
        finally {
           if(is!=null)
                try {
                    is.close();
                }
                catch (java.io.IOException e){
                    Log.e(TAG , "error while closing inputstream");
                }
        }
    return null;
    }
    /*will run on the UI thread. So we fetch review and trailer data for each movie from here.*/


    /*
    @Override
    protected void onPostExecute(Void v) {



        if(internetAccess == false) {

            Toast t = Toast.makeText(context,"No internet access !!", Toast.LENGTH_SHORT);
            t.show();
            return;

        }
        Log.d(TAG , "on post execute");

        //get the reviews and trailers as well so that if a user clicks on a movie poster, the reviews and trailer info
        //is also passed on to the detail fragment.
        //call update adapter after both trailers and reviews have been received. i.e. in callback.

        Retrofit retrofit  = new Retrofit.Builder()
                .baseUrl(BASE_URL_REVIEWS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieReviewAPI movieReviewAPI = retrofit.create(MovieReviewAPI.class);



        for(int i=0; i< movies.length ; i++)
        {
            call =  movieReviewAPI.getReviews(movies[i].getId() , context.getString(R.string.api_key_tmdb));
            call.enqueue(new CustomCallbackReview(movies , i , context ,gridView, activity_context));

        }




    return ;
    }
*/

    private String convertInputToString(InputStream inputStream) throws IOException{
        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(inputStream));        //UTF-8 req ??

        StringBuffer buffer = new StringBuffer();
        String line;

        while((line = reader.readLine())!=null){
            // new line doesn't affect json parsing. It will make debugging easy. good practice.
            buffer.append(line + "\n");
        }

        String s = buffer.toString();

        return s;


    }

    private void parseJSON(String jsonString) throws JSONException{

        Log.d(TAG , "in parseJSON function");
        JSONObject jsonObject = new JSONObject(jsonString);
        String baseUrl = "http://image.tmdb.org/t/p/";

        JSONArray results = jsonObject.getJSONArray("results");

        for(int i=0; i<results.length();i++){

        JSONObject jsonObject1 = results.getJSONObject(i);
        Movie movie = new Movie();

        movie.setTitle(jsonObject1.getString("title"));
        movie.setOverview(jsonObject1.getString("overview"));
        movie.setRelease_date(jsonObject1.getString("release_date"));
        movie.setVote_average(jsonObject1.getDouble("vote_average"));
        movie.setId(jsonObject1.getInt("id"));

            //movie.setPoster_path(jsonObject1.getString("poster_path"));
        String url = baseUrl + "w185" + jsonObject1.getString("poster_path");
        movie.setUrl(url);


            movies[i] = movie;

        }


    }


}
