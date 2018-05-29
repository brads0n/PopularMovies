package com.example.android.popularmovies;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.adapter.ReviewAdapter;
import com.example.android.popularmovies.adapter.TrailerAdapter;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.model.Trailer;
import com.example.android.popularmovies.services.MovieService;
import com.example.android.popularmovies.services.ReviewService;
import com.example.android.popularmovies.services.TrailerService;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieDetail extends AppCompatActivity {

    Movie movie;
    List<Trailer> trailers;
    List<Review> reviews;
    RecyclerView rvTrailers;
    RecyclerView rvReviews;

    TextView tvMovieTitle;
    ImageView ivMovieCover;
    TextView tvMovieRelease;
    TextView tvMovieDuration;
    TextView tvMovieRating;
    Button btnMovieFavorite;
    TextView tvMovieOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();

//        TODO: Implement Error message
        if(intent.getExtras() == null) {
            finish();
        }

        int id = intent.getIntExtra("id", 0);
        getMovie(id);
        getTrailers(id);
        getReviews(id);

    }

    BroadcastReceiver movieReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", Activity.RESULT_CANCELED);

            if(resultCode == RESULT_OK) {
                movie = (Movie) intent.getSerializableExtra("movie");
                populateMovie();
            }
        }
    };

    BroadcastReceiver trailersReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", Activity.RESULT_CANCELED);

            if(resultCode == RESULT_OK) {
                trailers = (List<Trailer>) intent.getSerializableExtra("trailers");
                populateTrailers();
            }
        }
    };

    BroadcastReceiver reviewsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", Activity.RESULT_CANCELED);

            if(resultCode == RESULT_OK) {
                reviews = (List<Review>) intent.getSerializableExtra("reviews");
                populateReviews();
            }
        }
    };

    public void populateMovie() {
        tvMovieTitle = findViewById(R.id.tv_movie_title);
        ivMovieCover = findViewById(R.id.iv_movie_cover);
        tvMovieRelease = findViewById(R.id.tv_movie_release_year);
        tvMovieDuration = findViewById(R.id.tv_movie_duration);
        tvMovieRating = findViewById(R.id.tv_movie_rating);
        btnMovieFavorite = findViewById(R.id.btn_movie_favorite);
        tvMovieOverview = findViewById(R.id.tv_movie_overview);

        Picasso.get().load("https://image.tmdb.org/t/p/w300" + movie.getPoster()).into(ivMovieCover);

        tvMovieTitle.setText(movie.getTitle());
        tvMovieRelease.setText(movie.getRelease_date());
        tvMovieDuration.setText(movie.getDuration() + "min");
        tvMovieRating.setText(movie.getRating() + "/10");
        tvMovieOverview.setText(movie.getOverview());
    }

    public void populateTrailers() {
        rvTrailers = findViewById(R.id.trailers);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvTrailers.setLayoutManager(llm);
        TrailerAdapter adapter = new TrailerAdapter(this, trailers);
        rvTrailers.setAdapter(adapter);
    }

    public void populateReviews() {
        rvReviews = findViewById(R.id.reviews);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvReviews.setLayoutManager(llm);
        ReviewAdapter adapter = new ReviewAdapter(this, reviews);
        rvReviews.setAdapter(adapter);
    }

    private void getMovie(int id) {
        Intent intent = new Intent(this, MovieService.class);
        intent.putExtra("urlString", String.valueOf(id));
        startService(intent);
    }
    private void getTrailers(int id) {
        Intent intent = new Intent(this, TrailerService.class);
        intent.putExtra("urlString", String.valueOf(id));
        startService(intent);
    }
    private void getReviews(int id) {
        Intent intent = new Intent(this, ReviewService.class);
        intent.putExtra("id", id);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter movieFilter = new IntentFilter(MovieService.ACTION);
        registerReceiver(movieReceiver, movieFilter);
        IntentFilter trailerFilter = new IntentFilter(TrailerService.ACTION);
        registerReceiver(trailersReceiver, trailerFilter);
        IntentFilter reviewFilter = new IntentFilter(ReviewService.ACTION);
        registerReceiver(reviewsReceiver, reviewFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(movieReceiver);
        unregisterReceiver(trailersReceiver);
        unregisterReceiver(reviewsReceiver);
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
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addToFavorites(View v) {
        movie.setFavorite(!movie.isFavorite());
    }
}
