package com.example.android.popularmovies.services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.android.popularmovies.NetworkUtils;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReviewService extends IntentService {

    public static final String ACTION = "com.example.android.popularmovies.services.ReviewService";

    public ReviewService() {
        super(ACTION);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        List<Review> reviews = new ArrayList<>();
        int movieId = intent.getIntExtra("id", 0);
        URL url;
        url = NetworkUtils.buildTMDBUrl("/movie/" + movieId + "/reviews");
        String json = null;
        try {
            json = NetworkUtils.getJson(url);
            JSONObject my_obj = new JSONObject(json);
            JSONArray jsonArray = my_obj.getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {
                Review review = fetchMovie(jsonArray.getJSONObject(i));
                reviews.add(review);
            }

            sendBroadcast(reviews);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private Review fetchMovie(JSONObject reviewJason) throws JSONException {
        Review review = new Review();

        review.setId(reviewJason.getString("id"));
        review.setAuthor(reviewJason.getString("author"));
        review.setContent(reviewJason.getString("content"));

        return review;
    }

    private void sendBroadcast(List<Review> reviews) {
        Intent resultIntent = new Intent(ACTION);
        resultIntent.putExtra("resultCode", Activity.RESULT_OK);
        resultIntent.putExtra("reviews", (Serializable) reviews);
        sendBroadcast(resultIntent);
    }
}
