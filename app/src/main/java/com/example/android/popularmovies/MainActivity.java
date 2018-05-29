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
    List<Movie> popularMovies;
    String sortBy;

    BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("Bradson", "Received");
            int resultCode = intent.getIntExtra("resultCode", Activity.RESULT_CANCELED);
            if(resultCode == RESULT_OK) {
                popularMovies = (List<Movie>) intent.getSerializableExtra("movies");

                List<String> popularMoviesCovers = new ArrayList<>();
                for (Movie movie : popularMovies
                        ) {
                    popularMoviesCovers.add(movie.getPoster());
                }
                gridView.setAdapter(new ImageAdapter(MainActivity.this, popularMoviesCovers));
                gridView.invalidateViews();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.gridView);

        sortBy =
                PreferenceManager.getDefaultSharedPreferences( this )
                        .getString(getString(R.string.sort_movies_by), "Nothing");

//        String sortBy = getPreferences(MODE_PRIVATE).getString(getString(R.string.sort_movies_by), "/movie/popular");

//        getMovies("/movie/popular");
//        getMovies("/movie/top_rated");
        getMovies(sortBy);

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
            Log.e("Bradson", "Preferences changed");
            sortBy =
                    PreferenceManager.getDefaultSharedPreferences( this )
                            .getString(getString(R.string.sort_movies_by), "Nothing");
//            gridView.setAdapter(null);
            getMovies(sortBy);
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
