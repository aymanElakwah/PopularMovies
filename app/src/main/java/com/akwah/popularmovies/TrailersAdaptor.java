package com.akwah.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akwah.popularmovies.utils.JSONParse;
import com.akwah.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class TrailersAdaptor extends RecyclerView.Adapter<TrailersAdaptor.TrailerViewHolder> implements LoaderManager.LoaderCallbacks<ArrayList<String>> {

    private ArrayList<String> keys;
    private URL trailersURL;
    private static final int LOADER_ID = 2;
    private Context context;
    private DetailsActivity detailsActivity;

    public TrailersAdaptor(DetailsActivity detailsActivity, int movieID) {
        this.context = detailsActivity;
        this.detailsActivity = detailsActivity;
        trailersURL = NetworkUtils.getTrailerURL(movieID);
        detailsActivity.getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trailer_view, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.bind(keys.get(position));
    }

    @Override
    public int getItemCount() {
        if (keys == null)
            return 0;
        return keys.size();
    }

    @Override
    public Loader<ArrayList<String>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<String>>(context) {
            ArrayList<String> keys;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (keys == null)
                    forceLoad();
                else
                    deliverResult(keys);
            }

            @Nullable
            @Override
            public ArrayList<String> loadInBackground() {
                return JSONParse.parseTrailers(NetworkUtils.getResponseFromHttp(trailersURL, context));
            }

            @Override
            public void deliverResult(@Nullable ArrayList<String> data) {
                keys = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<String>> loader, ArrayList<String> data) {
        if (data != null) {
            keys = data;
            notifyDataSetChanged();
            if (data.size() != 0) detailsActivity.showTrailer();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<String>> loader) {

    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView image;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.trailer_image);
            image.setOnClickListener(this);
        }

        public void bind(String key) {
            String url = "https://img.youtube.com/vi/" + key + "/0.jpg";
            Log.d("AYMAN", url);
            Picasso.with(context).load(url).error(R.drawable.trailer_place_holder).placeholder(R.drawable.trailer_place_holder).into(image);
        }

        @Override
        public void onClick(View view) {
            NetworkUtils.watchYoutubeVideo(context, keys.get(getAdapterPosition()));
        }
    }

}
