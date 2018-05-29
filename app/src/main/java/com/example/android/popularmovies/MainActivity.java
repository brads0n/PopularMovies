package com.example.android.popularmovies;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    GridView gridView;
    List<Movie> movies;
    List<Movie> favoriteMovies;

    BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", Activity.RESULT_CANCELED);
            if(resultCode == RESULT_OK) {
                movies = (List<Movie>) intent.getSerializableExtra("movies");

                setApadtor(movies);
            }
        }
    };

    public void setApadtor(List<Movie> movies) {
        List<String> popularMoviesCovers = new ArrayList<>();
        for (Movie movie : movies
                ) {
            popularMoviesCovers.add(movie.getPoster());
        }
        gridView.setAdapter(new ImageAdapter(MainActivity.this, popularMoviesCovers));
        gridView.invalidateViews();
        // TODO WHATTTTTTTTTTTTTTT?
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        favoriteMovies = new ArrayList<>();

        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.gridView);

        getMovies();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent i = new Intent(getApplicationContext(), MovieDetail.class);
                i.putExtra("id", movies.get(position).getId());
                startActivity(i);
            }
        });
    }

    private void getTMDBMovies(String query) {
        Intent intent = new Intent(this, GetMoviesService.class);
        intent.putExtra("urlString", query);
        startService(intent);
    }

    private void getMovies() {
        String sortBy = PreferenceManager.getDefaultSharedPreferences( this )
                        .getString(getString(R.string.sort_movies_by), "Nothing");
        switch (sortBy) {
            case "favorites" :
                getFavorites();
                break;
             default:
                 getTMDBMovies(sortBy);
        }
    }

    private void getFavorites() {
        SharedPreferences sp = getSharedPreferences("favorites", MODE_PRIVATE);
        for (String key : sp.getAll().keySet()) {
            Movie movie = new Movie();
            movie.setId(Integer.parseInt(key));
            Log.e("Bradson", "Id: " + key);
            movie.setPoster(sp.getString(key, "http://www.51allout.co.uk/2012-02-18-commonwealth-bank-odi-series-round-two-review/image-not-found/"));
            favoriteMovies.add(movie);
        }
        setApadtor(favoriteMovies);
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra( PreferenceActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.GeneralPreferenceFragment.class.getName() );
            intent.putExtra( PreferenceActivity.EXTRA_NO_HEADERS, true );
            MainActivity.this.startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.sort_movies_by))) {
            getMovies();
        }
    }

    @Override
    protected void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        unregisterReceiver(myReceiver);
        super.onDestroy();
    }
}
