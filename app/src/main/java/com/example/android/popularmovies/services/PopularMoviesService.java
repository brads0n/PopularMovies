package com.example.android.popularmovies.services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.graphics.Movie;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.popularmovies.beans.MyMovie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;


public class PopularMoviesService extends IntentService {

    public static final String ACTION = "com.example.android.popularmovies.services.PopularMoviesService";

    public PopularMoviesService() {
        super(ACTION);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        TmdbMovies movies = new TmdbApi("a2c6a156954b7c6b6a1827c7eb1806ad").getMovies();
        MovieResultsPage moviesList = movies.getPopularMovies("", 1);

        List<MyMovie> myMovies = new ArrayList<>();
        for (MovieDb m : moviesList) {
            MyMovie myMovie = new MyMovie();
            myMovie.setId(m.getId());
            myMovie.setTitle(m.getTitle());
            myMovie.setOverview(m.getOverview());
            myMovie.setDuration(m.getId());
            myMovie.setRating(m.getUserRating());
            Log.e("MovieDetail", String.valueOf(m.getUserRating()));
            myMovie.setPoster(m.getPosterPath());

            myMovies.add(myMovie);
        }

        Intent resultIntent = new Intent(ACTION);
        resultIntent.putExtra("resultCode", Activity.RESULT_OK);
        resultIntent.putExtra("movies", (Serializable) myMovies);
        sendBroadcast(resultIntent);

    }
}
