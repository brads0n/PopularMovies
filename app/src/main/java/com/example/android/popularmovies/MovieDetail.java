package com.example.android.popularmovies;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.beans.Movie;
import com.example.android.popularmovies.services.MovieService;
import com.example.android.popularmovies.services.PopularMoviesService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieDetail extends AppCompatActivity {

    Movie movie;

    TextView tvMovieTitle;
    ImageView ivMovieCover;
    TextView tvMovieRelease;
    TextView tvMovieDuration;
    TextView tvMovieRating;
    Button btnMovieFavorite;
    TextView tvMovieOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();

//        TODO: Implement Error message
        if(intent.getExtras() == null) {
            finish();
        }

        int id = intent.getIntExtra("id", 0);
        getMovie(id);

    }

    BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", Activity.RESULT_CANCELED);

            if(resultCode == RESULT_OK) {
                movie = (Movie) intent.getSerializableExtra("movie");
                populateFields();
            }
        }
    };

    public void populateFields() {
        tvMovieTitle = findViewById(R.id.tv_movie_title);
        ivMovieCover = findViewById(R.id.iv_movie_cover);
        tvMovieRelease = findViewById(R.id.tv_movie_release_year);
        tvMovieDuration = findViewById(R.id.tv_movie_duration);
        tvMovieRating = findViewById(R.id.tv_movie_rating);
        btnMovieFavorite = findViewById(R.id.btn_movie_favorite);
        tvMovieOverview = findViewById(R.id.tv_movie_overview);

        Picasso.get().load("https://image.tmdb.org/t/p/w300" + movie.getPoster()).into(ivMovieCover);

        tvMovieTitle.setText(movie.getTitle());
        tvMovieRelease.setText(movie.getRelease_date());
        tvMovieDuration.setText("" + movie.getDuration());
        tvMovieRating.setText("" + movie.getRating());
        tvMovieOverview.setText(movie.getOverview());
    }

    private void getMovie(int id) {
        Intent intent = new Intent(this, MovieService.class);
        intent.putExtra("urlString", String.valueOf(id));
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(MovieService.ACTION);
        registerReceiver(myReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
    }
}
