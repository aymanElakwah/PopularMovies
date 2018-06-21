package com.akwah.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.akwah.popularmovies.Movie;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "favourites.db";
    private static final int VERSION_NUMBER = 1;
    private SQLiteDatabase mDB;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
        mDB = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql_statement = "CREATE TABLE " + DBContract.FavouriteEntry.TABLE_NAME + " (" +
                DBContract.FavouriteEntry._ID + " INTEGER PRIMARY KEY, " +
                DBContract.FavouriteEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                DBContract.FavouriteEntry.COLUMN_IMAGE_URL + " TEXT NOT NULL, " +
                DBContract.FavouriteEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                DBContract.FavouriteEntry.COLUMN_RATING + " REAL NOT NULL, " +
                DBContract.FavouriteEntry.COLUMN_DATE + " TEXT NOT NULL " +
                ");";
        sqLiteDatabase.execSQL(sql_statement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public ArrayList<Movie> getFavouriteMovies() {
        Cursor cursor = mDB.query(DBContract.FavouriteEntry.TABLE_NAME, null, null, null, null, null, null);
        ArrayList<Movie> movies = new ArrayList<>(cursor.getCount());
        int id_index = cursor.getColumnIndex(DBContract.FavouriteEntry._ID);
        int title_index = cursor.getColumnIndex(DBContract.FavouriteEntry.COLUMN_TITLE);
        int image_url_index = cursor.getColumnIndex(DBContract.FavouriteEntry.COLUMN_IMAGE_URL);
        int overview_index = cursor.getColumnIndex(DBContract.FavouriteEntry.COLUMN_OVERVIEW);
        int rating_index = cursor.getColumnIndex(DBContract.FavouriteEntry.COLUMN_RATING);
        int date_index = cursor.getColumnIndex(DBContract.FavouriteEntry.COLUMN_DATE);
        while (cursor.moveToNext()) {
            Movie movie = new Movie(cursor.getInt(id_index),
                    cursor.getString(title_index),
                    cursor.getString(image_url_index),
                    cursor.getString(overview_index),
                    cursor.getFloat(rating_index),
                    cursor.getString(date_index));
            movies.add(movie);
        }
        cursor.close();
        return movies;
    }

    public long addMovie(Movie movie) {
        ContentValues cv = new ContentValues();
        cv.put(DBContract.FavouriteEntry._ID, movie.getID());
        cv.put(DBContract.FavouriteEntry.COLUMN_TITLE, movie.getTitle());
        cv.put(DBContract.FavouriteEntry.COLUMN_IMAGE_URL, movie.getAbsoluteImageURL());
        cv.put(DBContract.FavouriteEntry.COLUMN_OVERVIEW, movie.getOverview());
        cv.put(DBContract.FavouriteEntry.COLUMN_RATING, movie.getRating());
        cv.put(DBContract.FavouriteEntry.COLUMN_DATE, movie.getReleaseDate());
        return mDB.insert(DBContract.FavouriteEntry.TABLE_NAME, null, cv);
    }

    public boolean remove(int ID) {
        return mDB.delete(DBContract.FavouriteEntry.TABLE_NAME, DBContract.FavouriteEntry._ID + "=?", new String[]{ID + ""}) > 0;
    }
}
