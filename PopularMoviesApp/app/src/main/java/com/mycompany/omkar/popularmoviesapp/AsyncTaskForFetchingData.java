package com.mycompany.omkar.popularmoviesapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

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

    public AsyncTaskForFetchingData(Context context, GridView gridView){
        this.context = context;
        this.activity_context = (FragmentActivity) context ;
        this.gridView = gridView;
        this.internetAccess = true;
    }

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

        final String url_string = FETCH_MOVIE_BASE_URL + SORT_BY + "=" + sorting_order + "&" + API_KEY + "=" + myApiKey ;

        InputStream is =null;

        try{
            URL url = new URL(url_string);
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

    @Override
    protected void onPostExecute(Void v) {

        if(internetAccess == false) {

            Toast t = Toast.makeText(context,"No internet access !!", Toast.LENGTH_SHORT);
            t.show();
            return;

        }
        Log.d(TAG , "on post execute");
        CustomAdapterForGridView adapter = new CustomAdapterForGridView(context, Arrays.asList(movies));

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Toast t = Toast.makeText(context,"i am at postition :" + position,Toast.LENGTH_SHORT);
                //t.show();
                android.support.v4.app.FragmentManager fm= activity_context.getSupportFragmentManager() ;
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();

                DetailsFragment fragment = new DetailsFragment();
                fragment.setMovie(movies[position],context);
                ft.replace(R.id.fragment_container, fragment, "details_fragment");
                ft.addToBackStack(null);
                ft.commit();


            }
        });

    return ;
    }


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

            //movie.setPoster_path(jsonObject1.getString("poster_path"));
        String url = baseUrl + "w185" + jsonObject1.getString("poster_path");
        movie.setUrl(url);


            movies[i] = movie;

        }


    }


}
