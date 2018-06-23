package com.akwah.popularmovies.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
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
    private static final String VIDEOS = "videos";
    private static final String REVIEWS = "reviews";

    public static String getURLString(int page, boolean sortByPopular) {
        return Uri.parse(BASE_URL).buildUpon().appendPath(sortByPopular ? SORT_BY_POPULAR : SORT_BY_TOP_RATED).appendQueryParameter(KEY_PARAM, KEY).appendQueryParameter(PAGE_PARAM, String.valueOf(page)).build().toString();
    }

    public static URL getTrailerURL(int movieID) {
        return toURL(Uri.parse(BASE_URL).buildUpon().appendPath(movieID + "").appendPath(VIDEOS).appendQueryParameter(KEY_PARAM, KEY).build().toString());
    }

    public static URL getReviewsURL(int movieID) {
        return toURL(Uri.parse(BASE_URL).buildUpon().appendPath(movieID + "").appendPath(REVIEWS).appendQueryParameter(KEY_PARAM, KEY).build().toString());
    }

    public static URL getURL(int page, boolean sortByPopular) {
        return toURL(getURLString(page, sortByPopular));
    }

    private static URL toURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getResponseFromHttp(URL url, Context context) {
        Log.d("AYMAN", url.toString());
        if (!isConnected(context))
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

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null)
            return false;
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static void watchYoutubeVideo(Context context, String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }
}
