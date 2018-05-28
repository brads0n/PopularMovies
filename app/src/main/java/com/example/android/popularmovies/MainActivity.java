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

import com.example.android.popularmovies.adapter.ImageAdapter;
import com.example.android.popularmovies.beans.Movie;
import com.example.android.popularmovies.services.PopularMoviesService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    List<Movie> popularMovies;

    BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", Activity.RESULT_CANCELED);
            popularMovies = (List<Movie>) intent.getSerializableExtra("movies");

            List<String> popularMoviesCovers = new ArrayList<>();
            for (Movie movie: popularMovies
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

        getMovies("/movie/popular");

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent i = new Intent(getApplicationContext(), MovieDetail.class);
                // TODO qet query based on possition
                i.putExtra("id", popularMovies.get(position).getId());
                startActivity(i);
            }
        });
    }

    private void getMovies(String query) {
        Intent intent = new Intent(this, PopularMoviesService.class);
        intent.putExtra("urlString", query);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(PopularMoviesService.ACTION);
        registerReceiver(myReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
    }
}
