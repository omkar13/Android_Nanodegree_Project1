package com.mycompany.omkar.popularmoviesapp;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;


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
        votes.setText("Vote average: " + Double.toString(movie.getVote_average())+ "\n");

        return view;
    }


    public void setMovie(Movie movie,Context context){
        this.movie = movie;
        this.context = context;
    }

}
