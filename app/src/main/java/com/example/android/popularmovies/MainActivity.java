package com.example.android.popularmovies;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.popularmovies.adapter.ImageAdapter;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.services.GetMoviesService;

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
            for (Movie movie : popularMovies
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

//        getMovies("/movie/popular");
        getMovies("/movie/top_rated");

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
        Intent intent = new Intent(this, GetMoviesService.class);
        intent.putExtra("urlString", query);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(GetMoviesService.ACTION);
        registerReceiver(myReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            MainActivity.this.startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
