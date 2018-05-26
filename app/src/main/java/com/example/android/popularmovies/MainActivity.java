package com.example.android.popularmovies;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.popularmovies.beans.MyMovie;
import com.example.android.popularmovies.services.PopularMoviesService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    List<MyMovie> popularMovies;

    BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", Activity.RESULT_CANCELED);
            popularMovies = (List<MyMovie>) intent.getSerializableExtra("movies");

            List<String> popularMoviesCovers = new ArrayList<>();
            for (MyMovie movie: popularMovies
                 ) {
                popularMoviesCovers.add(movie.getPoster());
            }
            gridView.setAdapter(new ImageAdapter(MainActivity.this, popularMoviesCovers));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.gridView);

//        // Get List of popular movies
//        URL url = NetworkUtils.buildUrl("/movie/popular", "");
//        new ProccessTask().execute(url);
//        // How the hell am I suppose to know the results????????????

        Intent intent = new Intent(this, PopularMoviesService.class);
        intent.putExtra("urlString", "/movie/popular");
        startService(intent);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent i = new Intent(getApplicationContext(), MovieDetail.class);
                // TODO qet query based on possition
                i.putExtra("movie", popularMovies.get(position));
                startActivity(i);
            }
        });

        IntentFilter filter = new IntentFilter(PopularMoviesService.ACTION);
        registerReceiver(myReceiver, filter);
    }
}
