package com.example.movies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetailsActivity extends AppCompatActivity {

    private ImageView posterPath;
    private TextView title;
    private TextView releaseDate;
    private TextView adult;
    private TextView originalLanguage;
    private TextView overview;
    private TextView popularity;
    private TextView voteAverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        posterPath = findViewById(R.id.MoviePic);
        title = findViewById(R.id.MovieTitle);
        releaseDate = findViewById(R.id.MovieReleaseDate);
        adult = findViewById(R.id.adult);
        originalLanguage = findViewById(R.id.originalLanguage);
        overview = findViewById(R.id.overview);
        popularity = findViewById(R.id.popularity);
        voteAverage = findViewById(R.id.voteAverage);

        Intent intent = getIntent();
        int movieId = intent.getIntExtra(MainActivity.MOVIE_ID, 0);

        String api_BASE_URL = "https://api.themoviedb.org/";
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(api_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create()
                        );

        Retrofit retrofit = builder.client(httpClient.build()).build();
        MoviesApiService client = retrofit.create(MoviesApiService.class);
        Call<MovieDetails> call = client.getDetails(movieId, "f216e296e1ea283fa1d6e50f5f9a3c6a");

        call.enqueue(new Callback<MovieDetails>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<MovieDetails> call, @NonNull Response<MovieDetails> response) {
                MovieDetails repos = response.body();
                if (repos != null) {
                    Picasso.get()
                            .load(MovieAdapter.IMAGE_BASE_URL_ORIGINAL + repos.getPoster_path())
                            .placeholder(R.color.design_default_color_background)
                            .error(R.drawable.ic_launcher_background)
                            .fit()
                            .into(posterPath);
                    title.setText(getString(R.string.Title) + ": " + repos.getTitle());
                    releaseDate.setText(getString(R.string.ReleaseDate) + ": " + repos.getRelease_date());
                    if (!repos.isAdult())
                        adult.setText("All Ages");
                    else
                        adult.setText("For Adults");
                    overview.setText(getString(R.string.Overview) + ": " + repos.getOverview());
                    originalLanguage.setText(getString(R.string.OriginalLanguage) + ": " + repos.getOriginal_language());
                    popularity.setText(String.format(getString(R.string.popularity) + ": %.1f", repos.getPopularity()));
                    voteAverage.setText(String.format(getString(R.string.voteAverage) + ": %.1f", repos.getVote_average()));
                } else {
                    Toast.makeText(MovieDetailsActivity.this, "Empty!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieDetails> call, @NonNull Throwable t) {
                Toast.makeText(MovieDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
