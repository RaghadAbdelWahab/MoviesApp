package com.example.movies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesApiService {
    @GET("3/movie/top_rated")
    Call<MovieResponse> getResults(@Query("api_key") String api_key);

    @GET("3/movie/{id}")
    Call<MovieDetails> getDetails(@Path("id") int id, @Query("api_key") String api_key);
}
