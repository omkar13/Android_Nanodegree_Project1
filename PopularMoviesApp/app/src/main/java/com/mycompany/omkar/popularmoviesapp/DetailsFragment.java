package com.mycompany.omkar.popularmoviesapp;


import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mycompany.omkar.popularmoviesapp.data.FavouriteMoviesContract;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    private Movie movie = null;
    private Context context;

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_details, container, false);
        ImageView imageView = (ImageView)view.findViewById(R.id.details_image);
        Picasso.with(context).load(movie.getUrl()).into(imageView);

        TextView title = (TextView)view.findViewById(R.id.details_title);
        title.setText("Title: " + movie.getTitle() + "\n");

        TextView release_date = (TextView)view.findViewById(R.id.details_release_date);
        release_date.setText("Release date: " + movie.getRelease_date() + "\n");

        TextView overvire = (TextView) view.findViewById(R.id.details_overview);
        overvire.setText("Plot synopsis: " + movie.getOverview()+ "\n" );

        TextView votes = (TextView) view.findViewById(R.id.details_vote_average);
        votes.setText("Vote average: " + Double.toString(movie.getVote_average()) + "\n");

        ViewGroup viewGroup = (ViewGroup)view.findViewById(R.id.relative_layout);


        List<ReviewPOJO> list = movie.getReviewList();

        if (list!=null) {
            for (int i = 0; i < list.size(); i++) {

                TextView review = new TextView(context);
                review.setText("Review no : " + i + "\n" + list.get(i).getContent() + "\n\n\n");
                viewGroup.addView(review, -1);

            }
        }

        final List<TrailerPOJO> list1 = movie.getTrailerList();

        if(list1!=null) {
            for (int i = 0; i < list1.size(); i++) {

                TextView trailer = new TextView(context);
                trailer.setTextSize(TypedValue.COMPLEX_UNIT_SP , 20);
                trailer.setText("Trailer no : " + i + " link" + "\n");

                trailer.setOnClickListener(new myCustomOnClickListener(i, list1));

                viewGroup.addView(trailer, -1);

            }

        }

        ToggleButton toggleButton = (ToggleButton) view.findViewById(R.id.favorite_button);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            Uri favMovieUri = null;
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    //we need to add it to the database !

                    ContentValues contentValue = new ContentValues();

                    contentValue.put(FavouriteMoviesContract.FavMoviesEntry.COLUMN_TITLE , movie.getTitle());
                    contentValue.put(FavouriteMoviesContract.FavMoviesEntry.COLUMN_RELEASE_DATE , movie.getRelease_date());
                    contentValue.put(FavouriteMoviesContract.FavMoviesEntry.COLUMN_URL , movie.getUrl());
                    contentValue.put(FavouriteMoviesContract.FavMoviesEntry.COLUMN_VOTE_AVERAGE , movie.getVote_average());
                    contentValue.put(FavouriteMoviesContract.FavMoviesEntry.COLUMN_OVERVIEW , movie.getOverview());
                    contentValue.put(FavouriteMoviesContract.FavMoviesEntry.COLUMN_MOVIE_ID , movie.getId());

                    favMovieUri = getActivity().getContentResolver().insert(FavouriteMoviesContract.FavMoviesEntry.CONTENT_URI , contentValue);

                }
                else{
                    //delete it from database
                    if(favMovieUri != null)
                            getActivity().getContentResolver().delete(favMovieUri , null , null);
                }
            }
        });

        return view;
    }


    public void setMovie(Movie movie,Context context){
        this.movie = movie;
        this.context = context;
    }


    public class myCustomOnClickListener implements View.OnClickListener{

        int i;
        List<TrailerPOJO> list1;


        public myCustomOnClickListener(int i,List<TrailerPOJO> list1){
            this.i = i;
            this.list1 = list1;
        }

        @Override
        public void onClick(View v) {
            TrailerPOJO trailerPOJO;
            trailerPOJO = list1.get(i);
            String url = "https://www.youtube.com/watch?v=" + trailerPOJO.getKey();


            Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse(url));
            startActivity(intent);

        }

    }

}
