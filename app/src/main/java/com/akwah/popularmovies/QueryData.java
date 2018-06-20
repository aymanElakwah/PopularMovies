package com.akwah.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.akwah.popularmovies.utils.JSONParse;
import com.akwah.popularmovies.utils.NetworkUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class QueryData implements LoaderManager.LoaderCallbacks<ArrayList<Movie>> {
    private static final int LOADER_ID = 1;
    private final Context context;
    private final MyAdapter myAdapter;
    private static final String URL_KEY = "url_key";
    private final LoaderManager loaderManager;
    private static final String TAG = "LOADER";

    public QueryData(Context context, MyAdapter myAdapter, LoaderManager loaderManager) {
        Log.d(TAG, "QueryData()");
        this.context = context;
        this.myAdapter = myAdapter;
        this.loaderManager = loaderManager;
        loaderManager.initLoader(LOADER_ID, null, this);
    }


    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, final Bundle args) {
        Log.d(TAG, "onCreateLoader()");
        return new AsyncTaskLoader<ArrayList<Movie>>(context) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                Log.d(TAG, "onStartLoading()");
                if (args == null)
                    return;
                Log.d(TAG, "args not null, forceload()");
                myAdapter.showLoading();
                forceLoad();
            }

            @Nullable
            @Override
            public ArrayList<Movie> loadInBackground() {
                Log.d(TAG, "loadInBackground()");
                if (!args.containsKey(URL_KEY))
                    return null;
                String url_text = args.getString(URL_KEY);
                Log.d(TAG, "loadInBackground -> " + url_text);
                URL url;
                try {
                    url = new URL(url_text);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return null;
                }
                String json = NetworkUtils.getResponseFromHttp(url, context);
                if (json != null)
                    return JSONParse.parseJSON(json);
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        Log.d(TAG, "onLoadFinished()");
        myAdapter.hideLoading();
        myAdapter.appendList(data);
        loaderManager.destroyLoader(LOADER_ID);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {
        Log.d(TAG, "onLoadReset");
    }

    public void assignLoader(String url) {
        Log.d(TAG, "assignLoader(" + url + ")");
        Bundle bundle = new Bundle();
        bundle.putString(URL_KEY, url);
        loaderManager.restartLoader(LOADER_ID, bundle, this);
    }
}
