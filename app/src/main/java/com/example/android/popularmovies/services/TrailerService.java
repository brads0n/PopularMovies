package com.example.android.popularmovies.services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.popularmovies.NetworkUtils;
import com.example.android.popularmovies.beans.Movie;
import com.example.android.popularmovies.beans.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TrailerService extends IntentService {

    public static final String ACTION = "com.example.android.popularmovies.services.TrailerService";

    public TrailerService() {
        super(ACTION);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        List<Trailer> trailers = new ArrayList<>();
        String id = intent.getStringExtra("urlString");
        URL url;
        url = NetworkUtils.buildTMDBUrl("/movie/" + id + "/videos");
        String json = null;
        try {
            json = NetworkUtils.getJson(url);

            JSONObject my_obj = new JSONObject(json);
            JSONArray jsonArray = my_obj.getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {
                Trailer trailer = fetchTrailer(jsonArray.getJSONObject(i));

                trailers.add(trailer);

            }

            sendBroadcast(trailers);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private Trailer fetchTrailer(JSONObject trailerJason) throws JSONException {
        Trailer trailer = new Trailer();
        trailer.setId(trailerJason.getString("id"));
        trailer.setTitle(trailerJason.getString("name"));
        String urlString = trailerJason.getString("key");
        trailer.setUrl(NetworkUtils.buildYOUTBUrl(urlString));

        return trailer;
    }

    private void sendBroadcast(List<Trailer> trailers) {
        Intent resultIntent = new Intent(ACTION);
        resultIntent.putExtra("resultCode", Activity.RESULT_OK);
        resultIntent.putExtra("trailers", (Serializable) trailers);
        sendBroadcast(resultIntent);
    }
}
