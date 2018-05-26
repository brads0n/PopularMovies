package com.example.android.popularmovies.broadcast;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import info.movito.themoviedbapi.model.core.MovieResultsPage;

public class PopularMoviesRecever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int resultCode = intent.getIntExtra("resultCode", Activity.RESULT_CANCELED);
        MovieResultsPage resultValue = intent.getParcelableExtra("movies");
    }
}
