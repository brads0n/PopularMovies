package com.example.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.beans.MyMovie;
import com.example.android.popularmovies.services.MovieResultService;
import com.example.android.popularmovies.services.PopularMoviesService;
import com.example.android.popularmovies.services.ProccessService;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbFind;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.FindResults;

public class MovieDetail extends AppCompatActivity {

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
        MyMovie movie = (MyMovie) intent.getSerializableExtra("movie");

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
}
