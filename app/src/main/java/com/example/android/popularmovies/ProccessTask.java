package com.example.android.popularmovies;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.URL;

public class ProccessTask extends AsyncTask<URL, Void, String> {

    @Override
    protected void onPreExecute() {
//            super.onPreExecute();
//            mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(URL... params) {
        URL searchUrl = params[0];
        String githubSearchResults = null;
        try {
            Log.e("MovieDetail", searchUrl.toString());
            githubSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return githubSearchResults;
    }

    @Override
    protected void onPostExecute(String json) {
        if (json != null && !json.equals("")) {
            Log.e("MovieDetail", json);
        } else {
        }
    }
}
