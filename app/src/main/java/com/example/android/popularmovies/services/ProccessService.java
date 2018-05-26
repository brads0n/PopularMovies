package com.example.android.popularmovies.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.popularmovies.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;

public class ProccessService extends IntentService {

    public static final String ACTION = "com.example.android.popularmovies.ProccessService";

    public ProccessService() {
        super(ACTION);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String urlString = intent.getStringExtra("urlString");
        URL url;
        if(intent.hasExtra("query")) {
            String query = intent.getStringExtra("query");
            url = NetworkUtils.buildUrl(urlString, query);
        } else {
            url = NetworkUtils.buildUrl(urlString);
        }
        String json = null;
        try {
            Log.e("MovieDetail", url.toString());
            json = NetworkUtils.getResponseFromHttpUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO: Find a way to return the values.
        Log.e("MovieDetail", json);


        TmdbMovies movies = new TmdbApi("a2c6a156954b7c6b6a1827c7eb1806ad").getMovies();
        MovieDb movie = movies.getMovie(5353, "en");
        Log.e("MovieDetail", movie.getTitle());

        try {
            JSONObject jsonObject = new JSONObject(json);
            String aJsonString = jsonObject.getString("page.results.title");
            Log.e("MovieDetail", "Will write now");
            Log.e("MovieDetail", "onHandleIntent: " + aJsonString );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
