package com.example.android.popularmovies.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popularmovies.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_MOVIE = "movie";
    private static final String COLUMN_MOVIE_ID = "id";
    private static final String COLUMN_MOVIE_TITLE = "title";
    private static final String COLUMN_MOVIE_POSTER = "poster";

    public MovieDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MOVIES_TABLE = "CREATE TABLE " +
                TABLE_MOVIE + " (" +
                COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY," +
                COLUMN_MOVIE_TITLE + " VARCHAR2(255)," +
                COLUMN_MOVIE_POSTER + " VARCHAR2(255)" +
                ")";
        db.execSQL(CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIE);
        onCreate(db);
    }

    public void addMovie(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_MOVIE_ID, movie.getId());
        values.put(COLUMN_MOVIE_TITLE, movie.getTitle());
        values.put(COLUMN_MOVIE_POSTER, movie.getPoster());
        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_MOVIE, null, values);
        db.close();
    }

    public Movie getMovie(String id) {
        String query = "SELECT " + TABLE_MOVIE + ".* FROM " +
                TABLE_MOVIE + " WHERE " + id + " = " +
                COLUMN_MOVIE_ID;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            Movie movie = new Movie();
            movie.setId(cursor.getInt(0));
            movie.setTitle(cursor.getString(1));
            movie.setPoster(cursor.getString(2));
            db.close();
            return movie;
        }
        db.close();
        return null;
    }

    public List<Movie> getMovies() {
        List<Movie> movies = new ArrayList<>();
        String query = "SELECT " + TABLE_MOVIE + ".* FROM " +
                TABLE_MOVIE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        while(cursor.moveToNext()) {
            Movie movie = new Movie();
            movie.setId(cursor.getInt(0));
            movie.setTitle(cursor.getString(1));
            movie.setPoster(cursor.getString(2));
            movies.add(movie);
        }
        db.close();
        return movies;
    }

    public boolean deleteMovie(Movie movie) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_MOVIE, COLUMN_MOVIE_ID + " = ?", new String[]{String.valueOf(movie.getId())});
        return true;
    }
}
