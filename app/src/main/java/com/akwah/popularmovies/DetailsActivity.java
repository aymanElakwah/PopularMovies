package com.akwah.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.akwah.popularmovies.data.DBHelper;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    public static final String INDEX_KEY = "index_key";
    private static final int STAR_ON = android.R.drawable.star_big_on;
    private static final int STAR_OFF = android.R.drawable.star_big_off;
    private ImageView mPosterImage;
    private TextView mTitle;
    private RatingBar mRatingBar;
    private TextView mReleaseDate;
    private TextView mOverview;
    private MenuItem mStar;
    private boolean favourite;
    private SharedPreferences sharedPreferences;
    private Movie movie;
    public static final int REQUEST_CODE = 1;
    public static final String DATA_KEY = "data_key";
    private Intent data;
    private int movieIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(INDEX_KEY))
            finish();
        setContentView(R.layout.activity_details);
        mPosterImage = (ImageView) findViewById(R.id.details_poster_image);
        mTitle = (TextView) findViewById(R.id.movie_title);
        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        mReleaseDate = (TextView) findViewById(R.id.release_date);
        mOverview = (TextView) findViewById(R.id.overview_text);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        movie = intent.getParcelableExtra(INDEX_KEY);
        Picasso.with(this).load(movie.getImageURL()).placeholder(R.drawable.progress_animation).error(R.drawable.error).into(mPosterImage);
        mTitle.setText(movie.getTitle());
        mRatingBar.setRating(movie.getRating());
        mReleaseDate.setText(movie.getReleaseDate());
        mOverview.setText(movie.getOverview());
        favourite = sharedPreferences.getBoolean(movie.getID() + "", false);
        data = new Intent();
        movieIndex = intent.getIntExtra(DATA_KEY, -1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        mStar = menu.findItem(R.id.favourite);
        checkStar();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favourite:
                favourite = !item.isChecked();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (favourite) {
                    editor.putBoolean(movie.getID() + "", true);
                    MainActivity.getDataBase().addMovie(movie);
                    Toast.makeText(this, "\"" + movie.getTitle() + "\" " + getString(R.string.add_to_favourite_toast), Toast.LENGTH_SHORT).show();
                    data.putExtra(DATA_KEY, -1);
                } else {
                    editor.remove(movie.getID() + "");
                    Toast.makeText(this, "\"" + movie.getTitle() + "\" " + getString(R.string.remove_from_favourite_toast), Toast.LENGTH_SHORT).show();
                    if (MainActivity.getDataBase().remove(movie.getID())) {
                        data.putExtra(DATA_KEY, movieIndex);
                    }
                }
                editor.apply();
                checkStar();
                setResult(RESULT_OK, data);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkStar() {
        if (mStar == null)
            return;
        mStar.setChecked(favourite);
        mStar.setIcon(favourite ? STAR_ON : STAR_OFF);
    }

}
