package com.example.android.popularmovies.services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.popularmovies.NetworkUtils;
import com.example.android.popularmovies.beans.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PopularMoviesService extends IntentService {

    public static final String ACTION = "com.example.android.popularmovies.services.PopularMoviesService";

    public PopularMoviesService() {
        super(ACTION);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        List<Movie> movies = new ArrayList<>();
        String urlString = intent.getStringExtra("urlString");
        URL url;
        url = NetworkUtils.buildTMDBUrl(urlString);
        String json = null;
        try {
            json = NetworkUtils.getJson(url);
            JSONObject my_obj = new JSONObject(json);
            JSONArray jsonArray = my_obj.getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {
                Movie movie = fetchMovie(jsonArray.getJSONObject(i));

                movies.add(movie);

            }

            sendBroadcast(movies);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getMovie(String id) {
        Intent intent = new Intent(this, MovieService.class);
        intent.putExtra("urlString", id);
        startService(intent);
    }

    private Movie fetchMovie(JSONObject movieJason) throws JSONException {
        Movie movie = new Movie();
        movie.setId(movieJason.getInt("id"));
        movie.setTitle(movieJason.getString("title"));
        movie.setOverview(movieJason.getString("overview"));
        movie.setRelease_date(movieJason.getString("release_date"));
        movie.setRating(movieJason.getInt("vote_average"));
        movie.setPoster(movieJason.getString("poster_path"));

        return movie;
    }

    private void sendBroadcast(List<Movie> movies) {
        Intent resultIntent = new Intent(ACTION);
        resultIntent.putExtra("resultCode", Activity.RESULT_OK);
        resultIntent.putExtra("movies", (Serializable) movies);
        sendBroadcast(resultIntent);
    }
}
