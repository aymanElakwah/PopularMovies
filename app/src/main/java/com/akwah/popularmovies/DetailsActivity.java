package com.akwah.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    public static final String INDEX_KEY = "index_key";

    private ImageView mPosterImage;
    private TextView mTitle;
    private RatingBar mRatingBar;
    private TextView mReleaseDate;
    private TextView mOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mPosterImage = (ImageView) findViewById(R.id.details_poster_image);
        mTitle = (TextView) findViewById(R.id.movie_title);
        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        mReleaseDate = (TextView) findViewById(R.id.release_date);
        mOverview = (TextView) findViewById(R.id.overview_text);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(INDEX_KEY)) {
            Movie movie = intent.getParcelableExtra(INDEX_KEY);
            Picasso.with(this).load(movie.getImageURL()).error(R.drawable.error).into(mPosterImage);
            mTitle.setText(movie.getTitle());
            mRatingBar.setRating(movie.getRating());
            mReleaseDate.setText(movie.getReleaseDate());
            mOverview.setText(movie.getOverview());
        }
    }
}
