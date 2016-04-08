package com.mycompany.omkar.popularmoviesapp;

import java.util.List;

/**
 * Created by omkar on 22/2/16.
 */
public class Movie {

    private String title;
    private String release_date;
    private String url;
    private double vote_average;
    private String overview;
    private int id;
    private List<ReviewPOJO> reviewList;
    private List<TrailerPOJO> trailerList;


    public Movie(String title, String release_date, String url, double vote_average, String overview, int id) {
        this.title = title;
        this.release_date = release_date;
        this.url = url;
        this.vote_average = vote_average;
        this.overview = overview;
        this.id = id;
    }

    public Movie(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ReviewPOJO> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<ReviewPOJO> reviewList) {
        this.reviewList = reviewList;
    }

    public List<TrailerPOJO> getTrailerList() {
        return trailerList;
    }

    public void setTrailerList(List<TrailerPOJO> trailerList) {
        this.trailerList = trailerList;
    }
}

