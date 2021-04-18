package com.example.movies;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.movies.R.id;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.movies.MovieDetailsFragment.newInstance;

public class MainActivity extends AppCompatActivity {

    public static final String MOVIE_ID = "com.example.movies.DETAILS_MESSAGE";
    private ArrayList<MovieRepos> arrayList = new ArrayList<>();
    private ViewPagerFragmentAdapter viewPagerFragmentAdapter;
   /* private MovieAdapter movieAdapter;
    RecyclerView recyclerView;*/
    ViewPager2 viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

 //       recyclerView = findViewById(id.MovieList);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(id.tab_layout);
/*
        movieAdapter = new MovieAdapter(this);
        movieAdapter.setMovieClickListener(movieId -> {
            MovieDetailsFragment movieDetailsFragment = newInstance(movieId);
            MainActivity.this.showFragment(movieDetailsFragment);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(movieAdapter);*/

        viewPagerFragmentAdapter = new ViewPagerFragmentAdapter(this,arrayList);
        viewPagerFragmentAdapter.setMovieClickListener(movieId -> {
            MovieDetailsFragment movieDetailsFragment = newInstance(movieId);
            MainActivity.this.showFragment(movieDetailsFragment);
        });
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager.setAdapter(viewPagerFragmentAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText("OBJECT " + (position + 1))
        ).attach();

        String api_BASE_URL = "https://api.themoviedb.org/";

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(api_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create()
                        );

        Retrofit retrofit = builder.client(httpClient.build()).build();
        MoviesApiService client = retrofit.create(MoviesApiService.class);
        Call<MovieResponse> call = client.getResults("f216e296e1ea283fa1d6e50f5f9a3c6a");

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.body() == null) throw new AssertionError();
                ArrayList<MovieRepos> repos = response.body().getResults();
                if (repos != null && repos.size() > 0) {
                    viewPagerFragmentAdapter.setArrayList(repos);
                } else {
                    Toast.makeText(MainActivity.this, "Empty!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(id.moviesContainer, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}