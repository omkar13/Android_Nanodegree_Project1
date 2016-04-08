package com.mycompany.omkar.popularmoviesapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omkar on 31/3/16.
 */
public class Reviews {

    private Integer id;

    private Integer page;
    private List<ReviewPOJO> results = new ArrayList<ReviewPOJO>();
    private Integer total_pages;
    private Integer total_results;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<ReviewPOJO> getResults() {
        return results;
    }

    public void setResults(List<ReviewPOJO> results) {
        this.results = results;
    }

    public Integer getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(Integer total_pages) {
        this.total_pages = total_pages;
    }

    public Integer getTotal_results() {
        return total_results;
    }

    public void setTotal_results(Integer total_results) {
        this.total_results = total_results;
    }
}
