package com.akwah.popularmovies.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Ayman on 16/02/2018.
 */

public class NetworkUtils {
    private static final String KEY = "######################################";
    private static final String BASE_URL = "http://api.themoviedb.org/3/discover/movie";
    private static final String SORT_BY_POPULAR = "popularity.desc";
    private static final String SORT_BY_TOP_RATED = "vote_average.desc";
    private static final String SORT_BY_PARAM = "sort_by";
    private static final String KEY_PARAM = "api_key";
    private static final String PAGE_PARAM = "page";

    public static URL getURL(int page, boolean sortByPopular) {
        try {
            Uri uri = Uri.parse(BASE_URL).buildUpon().appendQueryParameter(SORT_BY_PARAM, sortByPopular ? SORT_BY_POPULAR : SORT_BY_TOP_RATED).appendQueryParameter(KEY_PARAM, KEY).appendQueryParameter(PAGE_PARAM, String.valueOf(page)).build();
            URL url = new URL(uri.toString());
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getResponseFromHttp(URL url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\A");
            if (scanner.hasNext())
                return scanner.next();
            return null;
        } catch (IOException ex) {
            return null;
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }
}
