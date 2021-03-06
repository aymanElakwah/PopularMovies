package com.akwah.popularmovies.utils;

import android.net.Uri;

import com.akwah.popularmovies.Movie;
import com.akwah.popularmovies.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ayman on 17/02/2018.
 */

public class JSONParse {
    private static final String ID = "id";
    private static final String RESULTS = "results";
    private static final String POSTER_PATH = "poster_path";
    private static final String TITLE = "original_title";
    private static final String VOTE = "vote_average";
    private static final String DATE = "release_date";
    private static final String OVERVIEW = "overview";
    private static final String TRAILER_KEY = "key";
    private static final String CONTENT = "content";
    private static final String AUTHOR = "author";

    public static ArrayList<Movie> parseJSON(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray results = jsonObject.getJSONArray(RESULTS);
            ArrayList<Movie> movies = new ArrayList<>(results.length());
            JSONObject element;
            for (int i = 0; i < results.length(); i++) {
                element = (JSONObject) results.get(i);
                int id = element.optInt(ID);
                String imageURL = element.optString(POSTER_PATH);
                String title = element.optString(TITLE);
                String overview = element.optString(OVERVIEW);
                float rating = (float) element.optDouble(VOTE);
                String date = element.optString(DATE);
                movies.add(new Movie(id, title, imageURL, overview, rating, date));
            }
            return movies;
        } catch (JSONException ex) {
            return null;
        }
    }

    public static ArrayList<String> parseTrailers(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray results = jsonObject.getJSONArray(RESULTS);
            ArrayList<String> uris = new ArrayList<>(results.length());
            JSONObject element;
            for (int i = 0; i < results.length(); i++) {
                element = (JSONObject) results.get(i);
                uris.add(element.optString(TRAILER_KEY));
            }
            return uris;
        } catch (JSONException ex) {
            return null;
        }
    }

    public static ArrayList<Review> parseReviews(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray results = jsonObject.getJSONArray(RESULTS);
            ArrayList<Review> reviews = new ArrayList<>(results.length());
            JSONObject element;
            for (int i = 0; i < results.length(); i++) {
                element = (JSONObject) results.get(i);
                reviews.add(new Review(element.optString(CONTENT), element.optString(AUTHOR)));
            }
            return reviews;
        } catch (JSONException ex) {
            return null;
        }
    }
}
