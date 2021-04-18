package com.example.movies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MovieResponse {

    @SerializedName("results")
    @Expose
    private final ArrayList<MovieRepos> results;
    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("total_pages")
    @Expose
    private int total_pages;
    @SerializedName("total_results")
    @Expose
    private int total_results;

    public MovieResponse(ArrayList<MovieRepos> results) {
        this.results = results;
    }

    public ArrayList<MovieRepos> getResults() {
        return results;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

}
