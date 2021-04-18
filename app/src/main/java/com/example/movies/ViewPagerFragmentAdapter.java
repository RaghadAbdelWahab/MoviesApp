package com.example.movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewPagerFragmentAdapter extends RecyclerView.Adapter<ViewPagerFragmentAdapter.ViewHolder>{
    private final Context context;
    private ArrayList<MovieRepos> arrayList;
    private ViewPagerFragmentAdapter.MovieClickListener movieClickListener;
    public static final String IMAGE_BASE_URL_300 = "https://image.tmdb.org/t/p/w300";
    public static final String IMAGE_BASE_URL_500 = "https://image.tmdb.org/t/p/w500";
    public static final String IMAGE_BASE_URL_ORIGINAL = "https://image.tmdb.org/t/p/original";

    public ViewPagerFragmentAdapter(Context context,ArrayList<MovieRepos> arrayList) {
        this.context = context;
        this.arrayList = new ArrayList<>();
    }

    public void setArrayList(ArrayList<MovieRepos> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    public void setMovieClickListener(ViewPagerFragmentAdapter.MovieClickListener movieClickListener) {
        this.movieClickListener = movieClickListener;
    }

    @NonNull
    @Override
    public ViewPagerFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewpager_screen, viewGroup, false);
        return new ViewPagerFragmentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewPagerFragmentAdapter.ViewHolder viewHolder, int position) {
        Picasso.get()
                .load(IMAGE_BASE_URL_300 + arrayList.get(position).getPoster_path())
                .placeholder(R.color.design_default_color_background)
                .error(R.drawable.ic_launcher_background)
                .fit()
                .into(viewHolder.poster_path);
        viewHolder.movieTitle.setText(arrayList.get(position).getTitle());
        viewHolder.movieReleaseDate.setText(arrayList.get(position).getReleaseDate());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView poster_path;
        TextView movieTitle;
        TextView movieReleaseDate;
        CardView movieDetailsCard;

        public ViewHolder(View itemView) {
            super(itemView);

            poster_path = itemView.findViewById(R.id.MoviePic);
            movieTitle = itemView.findViewById(R.id.MovieTitle);
            movieReleaseDate = itemView.findViewById(R.id.MovieReleaseDate);
            movieDetailsCard = itemView.findViewById(R.id.MovieDetails);

            movieDetailsCard.setOnClickListener(v -> {
                int movieId = arrayList.get(ViewPagerFragmentAdapter.ViewHolder.this.getAdapterPosition()).getId();
                movieClickListener.onMovieClicked(movieId);
            });

        }
    }

    interface MovieClickListener {
        void onMovieClicked(int movieId);
    }
}