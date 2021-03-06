package com.example.android.popularmovies;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    final static String TMDB_BASE_URL = "http://api.themoviedb.org/3";
    final static String YOUTB_BASE_URL = "https://www.youtube.com/watch";

    final static String API_KEY = "api_key";
    final static String API_KEY_VALUE = "a2c6a156954b7c6b6a1827c7eb1806ad";
    final static String YOUTB_QUERY = "v";

    public static URL buildTMDBUrl(String urlString) {
        Uri builtUri = Uri.parse(TMDB_BASE_URL + urlString).buildUpon()
                .appendQueryParameter(API_KEY, API_KEY_VALUE)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildYOUTBUrl(String video) {
        Uri builtUri = Uri.parse(YOUTB_BASE_URL).buildUpon()
                .appendQueryParameter(YOUTB_QUERY, video)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getJson(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
