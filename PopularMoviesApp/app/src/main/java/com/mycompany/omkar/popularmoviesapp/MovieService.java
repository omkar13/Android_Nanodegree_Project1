package com.mycompany.omkar.popularmoviesapp;

/**
 * Created by omkar on 1/4/16.
 */

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class MovieService {

    public interface MovieAPI{

        @GET("3/discover/movie")
        Call<MovieList> getMovies(
                @Query("api_key") String apiKey,
                @Query("sort_by") String sortingOrder
        );

    }

}
