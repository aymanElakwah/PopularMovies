package com.akwah.popularmovies;

import android.os.AsyncTask;

import com.akwah.popularmovies.utils.JSONParse;
import com.akwah.popularmovies.utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Ayman on 24/02/2018.
 */

public class MyTask extends AsyncTask<URL, Void, ArrayList<Movie>> {
    private MyAdapter myAdapter;

    public MyTask(MyAdapter adapter) {
        myAdapter = adapter;
    }

    @Override
    protected ArrayList<Movie> doInBackground(URL... url) {
        String json = myAdapter.getNetworkUtils().getResponseFromHttp(url[0]);
        if (json != null)
            return JSONParse.parseJSON(json);
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        super.onPostExecute(movies);
        myAdapter.appendList(movies);
        if (movies == null)
            myAdapter.noInternetConnection();
    }
}
