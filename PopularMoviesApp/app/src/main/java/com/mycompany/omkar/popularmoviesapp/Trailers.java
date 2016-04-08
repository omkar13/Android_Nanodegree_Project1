package com.mycompany.omkar.popularmoviesapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omkar on 1/4/16.
 */
public class Trailers {

    private Integer id;
    private List<TrailerPOJO> results = new ArrayList<TrailerPOJO>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<TrailerPOJO> getResults() {
        return results;
    }

    public void setResults(List<TrailerPOJO> results) {
        this.results = results;
    }
}
