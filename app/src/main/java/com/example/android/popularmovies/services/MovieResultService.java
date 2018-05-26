package com.example.android.popularmovies.services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.popularmovies.beans.MyMovie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbFind;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.FindResults;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;


public class MovieResultService extends IntentService {

    public static final String ACTION = "com.example.android.popularmovies.services.MovieResultService";

    public MovieResultService() {
        super(ACTION);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        int id = intent.getExtras().getInt("id");

        TmdbFind tmdbFind = new TmdbApi("a2c6a156954b7c6b6a1827c7eb1806ad").getFind();
        FindResults results = tmdbFind.find(String.valueOf(id), null, "");
        MovieDb movieDb = results.getMovieResults().get(0);

        Log.e("MovieDetail", movieDb.getTitle());

        MyMovie myMovie = new MyMovie();
        myMovie.setId(movieDb.getId());
        myMovie.setTitle(movieDb.getTitle());
        myMovie.setOverview(movieDb.getOverview());
        //     myMovie.setScore(m.getStatus());
        myMovie.setPoster(movieDb.getPosterPath());

        Intent resultIntent = new Intent(ACTION);
        resultIntent.putExtra("resultCode", Activity.RESULT_OK);
        resultIntent.putExtra("movie", (Serializable) myMovie);
        sendBroadcast(resultIntent);
    }
}
