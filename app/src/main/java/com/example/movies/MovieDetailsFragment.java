package com.example.movies;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.picasso.Picasso;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetailsFragment extends Fragment {

    public static final String ARG_MOVIE_ID = "movieId";
    private ImageView posterPath;
    private TextView title;
    private TextView releaseDate;
    private TextView adult;
    private TextView originalLanguage;
    private TextView overview;
    private TextView popularity;
    private TextView voteAverage;
    private int movieIdParam;

    public MovieDetailsFragment() {
    }

    public static MovieDetailsFragment newInstance(int movieId) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MOVIE_ID, movieId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieIdParam = getArguments().getInt(ARG_MOVIE_ID);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        posterPath = view.findViewById(R.id.MoviePic);
        title = view.findViewById(R.id.MovieTitle);
        releaseDate = view.findViewById(R.id.MovieReleaseDate);
        adult = view.findViewById(R.id.adult);
        originalLanguage = view.findViewById(R.id.originalLanguage);
        overview = view.findViewById(R.id.overview);
        popularity = view.findViewById(R.id.popularity);
        voteAverage = view.findViewById(R.id.voteAverage);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String api_BASE_URL = "https://api.themoviedb.org/";
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(api_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create()
                        );

        Retrofit retrofit = builder.client(httpClient.build()).build();
        MoviesApiService client = retrofit.create(MoviesApiService.class);
        Call<MovieDetails> call = client.getDetails(movieIdParam, "f216e296e1ea283fa1d6e50f5f9a3c6a");

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
                    title.setText(String.format("Title: %s", repos.getTitle()));
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
                    Toast.makeText(getContext(), "Empty!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieDetails> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}