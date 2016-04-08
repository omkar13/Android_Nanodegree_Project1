package com.mycompany.omkar.popularmoviesapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omkar on 1/4/16.
 */
public class MovieList {


    private Integer page;
    private List<MoviePOJO> results = new ArrayList<MoviePOJO>();
    private Integer totalResults;
    private Integer totalPages;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<MoviePOJO> getResults() {
        return results;
    }

    public void setResults(List<MoviePOJO> results) {
        this.results = results;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
}

