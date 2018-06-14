package com.akwah.popularmovies.utils;

import com.akwah.popularmovies.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ayman on 17/02/2018.
 */

public class JSONParse {
    private static final String RESULTS = "results";
    private static final String POSTER_PATH = "poster_path";
    private static final String TITLE = "original_title";
    private static final String VOTE = "vote_average";
    private static final String DATE = "release_date";
    private static final String OVERVIEW = "overview";

    public static ArrayList<Movie> parseJSON(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray results = jsonObject.getJSONArray(RESULTS);
            ArrayList<Movie> movies = new ArrayList<>(results.length());
            JSONObject element;
            for (int i = 0; i < results.length(); i++) {
                element = (JSONObject) results.get(i);
                String imageURL = element.optString(POSTER_PATH);
                String title = element.optString(TITLE);
                String overview = element.optString(OVERVIEW);
                float rating = (float) element.optDouble(VOTE);
                String date = element.optString(DATE);
                movies.add(new Movie(title, imageURL, overview, rating, date));
            }
            return movies;
        } catch (JSONException ex) {
            return null;
        }
    }
}
