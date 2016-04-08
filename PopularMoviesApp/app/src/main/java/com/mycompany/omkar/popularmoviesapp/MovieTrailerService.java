package com.mycompany.omkar.popularmoviesapp;

/**
 * Created by omkar on 1/4/16.
 */


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class MovieTrailerService {

    public interface MovieTrailerAPI{

        @GET("3/movie/{movie_id}/videos")
        Call<Trailers> getTrailers(
                @Path("movie_id") int movie_id,
                @Query("api_key") String apiKey
        );

    }


}
