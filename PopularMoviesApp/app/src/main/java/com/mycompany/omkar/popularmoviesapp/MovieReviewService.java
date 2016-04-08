package com.mycompany.omkar.popularmoviesapp;

/**
 * Created by omkar on 31/3/16.
 */
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class MovieReviewService {

    public interface MovieReviewAPI{

        @GET("3/movie/{movie_id}/reviews")
        Call<Reviews> getReviews(
            @Path("movie_id") int movie_id,
            @Query("api_key") String apiKey
        );


    }


}
