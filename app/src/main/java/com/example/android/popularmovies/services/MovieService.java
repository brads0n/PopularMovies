package com.example.android.popularmovies.services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.android.popularmovies.NetworkUtils;
import com.example.android.popularmovies.model.Movie;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class MovieService extends IntentService {

    public static final String ACTION = "com.example.android.popularmovies.services.MovieService";

    public MovieService() {
        super(ACTION);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Movie movie;
        String id = intent.getStringExtra("urlString");
        URL url;
        // TODO Missing id
        url = NetworkUtils.buildTMDBUrl("/movie/" + id);
        String json = null;
        try {
            json = NetworkUtils.getJson(url);

            JSONObject movieJason = new JSONObject(json);
            movie = fetchMovie(movieJason);

            sendBroadcast(movie);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private Movie fetchMovie(JSONObject movieJason) throws JSONException {
        Movie movie = new Movie();
        movie.setId(movieJason.getInt("id"));
        movie.setTitle(movieJason.getString("title"));
        movie.setOverview(movieJason.getString("overview"));
        movie.setRelease_date(movieJason.getString("release_date"));
        movie.setRating(movieJason.getInt("vote_average"));
        movie.setDuration(movieJason.getInt("runtime"));
        movie.setPoster(movieJason.getString("poster_path"));

        return movie;
    }

    private void sendBroadcast(Movie movie) {
        Intent resultIntent = new Intent(ACTION);
        resultIntent.putExtra("resultCode", Activity.RESULT_OK);
        resultIntent.putExtra("movie", movie);
        sendBroadcast(resultIntent);
    }
}
