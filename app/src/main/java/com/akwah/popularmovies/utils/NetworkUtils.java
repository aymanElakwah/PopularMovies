package com.akwah.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.akwah.popularmovies.BuildConfig;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Ayman on 16/02/2018.
 */

public class NetworkUtils {
    private static final String KEY = BuildConfig.MY_API_KEY;
    private static final String BASE_URL = "http://api.themoviedb.org/3/movie";
    private static final String SORT_BY_POPULAR = "popular";
    private static final String SORT_BY_TOP_RATED = "top_rated";
    private static final String KEY_PARAM = "api_key";
    private static final String PAGE_PARAM = "page";
    private ConnectivityManager connectivityManager;

    public NetworkUtils(Context context) {
        this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static URL getURL(int page, boolean sortByPopular) {
        try {
            Uri uri = Uri.parse(BASE_URL).buildUpon().appendPath(sortByPopular ? SORT_BY_POPULAR : SORT_BY_TOP_RATED).appendQueryParameter(KEY_PARAM, KEY).appendQueryParameter(PAGE_PARAM, String.valueOf(page)).build();
            URL url = new URL(uri.toString());
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getResponseFromHttp(URL url) {
        if(!isConnected())
            return null;
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

    private boolean isConnected() {
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
