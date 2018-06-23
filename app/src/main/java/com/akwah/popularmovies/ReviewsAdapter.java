package com.akwah.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akwah.popularmovies.utils.JSONParse;
import com.akwah.popularmovies.utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> implements LoaderManager.LoaderCallbacks<ArrayList<Review>> {

    private DetailsActivity detailsActivity;
    private Context context;
    private ArrayList<Review> reviews;
    private static final int LOADER_ID = 3;
    private URL reviewsURL;

    public ReviewsAdapter(DetailsActivity detailsActivity, int moviesID) {
        this.detailsActivity = detailsActivity;
        context = detailsActivity;
        reviewsURL = NetworkUtils.getReviewsURL(moviesID);
        detailsActivity.getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_view, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.bind(reviews.get(position));
    }

    @Override
    public int getItemCount() {
        if(reviews == null)
            return 0;
        return reviews.size();
    }

    @Override
    public Loader<ArrayList<Review>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<Review>>(context) {
            private ArrayList<Review> reviews;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(reviews == null)
                    forceLoad();
                else
                    deliverResult(reviews);
            }

            @Nullable
            @Override
            public ArrayList<Review> loadInBackground() {
                return JSONParse.parseReviews(NetworkUtils.getResponseFromHttp(reviewsURL, context));
            }

            @Override
            public void deliverResult(@Nullable ArrayList<Review> data) {
                reviews = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Review>> loader, ArrayList<Review> data) {
        if (data != null) {
            reviews = data;
            notifyDataSetChanged();
            if (data.size() != 0) detailsActivity.showReviews();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Review>> loader) {
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        private TextView mContent;
        private TextView mAuthor;
        public ReviewViewHolder(View itemView) {
            super(itemView);
            mContent = itemView.findViewById(R.id.review_content);
            mAuthor = itemView.findViewById(R.id.review_author);
        }

        public void bind(Review review) {
            mContent.setText(review.getContent());
            mAuthor.setText(review.getAuthor());
        }
    }
}
