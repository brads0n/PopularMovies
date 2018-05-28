package com.example.android.popularmovies;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.beans.Movie;
import com.example.android.popularmovies.beans.Trailer;
import com.example.android.popularmovies.services.MovieService;
import com.example.android.popularmovies.services.PopularMoviesService;
import com.example.android.popularmovies.services.TrailerService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieDetail extends AppCompatActivity {

    Movie movie;
    List<Trailer> trailers;

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
        getTrailers(id);

    }

    BroadcastReceiver movieReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", Activity.RESULT_CANCELED);

            if(resultCode == RESULT_OK) {
                movie = (Movie) intent.getSerializableExtra("movie");
                populateMovie();
            }
        }
    };

    BroadcastReceiver trailersReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", Activity.RESULT_CANCELED);

            if(resultCode == RESULT_OK) {
                trailers = (List<Trailer>) intent.getSerializableExtra("trailers");
                populateTrailers();
            }
        }
    };

    public void populateMovie() {
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

    public void populateTrailers() {
        for (Trailer trailer :  trailers ) {
            Log.e("Bradson", "Trailer: " + trailer.getTitle() + ": " + trailer.getUrl());
        }
    }

    private void getMovie(int id) {
        Intent intent = new Intent(this, MovieService.class);
        intent.putExtra("urlString", String.valueOf(id));
        startService(intent);
    }
    private void getTrailers(int id) {
        Intent intent = new Intent(this, TrailerService.class);
        intent.putExtra("urlString", String.valueOf(id));
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter movieFilter = new IntentFilter(MovieService.ACTION);
        registerReceiver(movieReceiver, movieFilter);
        IntentFilter trailerFilter = new IntentFilter(TrailerService.ACTION);
        registerReceiver(trailersReceiver, trailerFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(movieReceiver);
        unregisterReceiver(trailersReceiver);
    }
}
