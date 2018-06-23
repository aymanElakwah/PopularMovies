package com.akwah.popularmovies;

import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.akwah.popularmovies.data.DBHelper;

public class MainActivity extends AppCompatActivity implements MyAdapter.ListItemClickListener {

    private RecyclerView mRecyclerView;
    private MyAdapter myAdapter;
    private View mNoInternetLayout;
    private Button mTryAgain;
    private ProgressBar mProgressBar;
    private ProgressBar mLoading;
    private static DBHelper mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDB = new DBHelper(this);
        mRecyclerView = findViewById(R.id.posters_rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, getSpanCount());
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        myAdapter = new MyAdapter(this, this);
        mRecyclerView.setAdapter(myAdapter);
        mNoInternetLayout = findViewById(R.id.no_internet_layout);
        mTryAgain = findViewById(R.id.try_again_button);
        mTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTryAgain.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
                myAdapter.tryAgain();
            }
        });
        mProgressBar = findViewById(R.id.progressBar);
        mLoading = findViewById(R.id.loading);
        myAdapter.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        myAdapter.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        int itemID = myAdapter.isShowingFavourites() ? R.id.my_favourites : myAdapter.getSortByPopular() ? R.id.sort_by_popular : R.id.sort_by_top_rated;
        menu.findItem(itemID).setChecked(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_popular:
                setTitle(R.string.sort_by_popular);
                item.setChecked(true);
                myAdapter.sortByPopular();
                return true;
            case R.id.sort_by_top_rated:
                setTitle(R.string.sort_by_top_rated);
                item.setChecked(true);
                myAdapter.sortByTopRated();
                return true;
            case R.id.my_favourites:
                setTitle(R.string.my_favourites);
                item.setChecked(true);
                myAdapter.showFavourites();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == DetailsActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            myAdapter.removeMovie(data.getIntExtra(DetailsActivity.DATA_KEY, -1));
        }
    }

    public void showErrorMessage() {
        mProgressBar.setVisibility(View.GONE);
        mTryAgain.setVisibility(View.VISIBLE);
        mNoInternetLayout.setVisibility(View.VISIBLE);
    }

    public void hideErrorMessage() {
        mNoInternetLayout.setVisibility(View.GONE);
    }

    public void showLoading() {
        mLoading.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        mLoading.setVisibility(View.GONE);
    }

    @Override
    public void onClick(Movie movie, int index) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.INDEX_KEY, movie);
        intent.putExtra(DetailsActivity.DATA_KEY, index);
        startActivityForResult(intent, DetailsActivity.REQUEST_CODE);
    }

    private int getSpanCount() {
        Resources res = getResources();
        DisplayMetrics displayMetrics = res.getDisplayMetrics();
        int spanCount = displayMetrics.widthPixels / (res.getDimensionPixelSize(R.dimen.image_width) + 2 * res.getDimensionPixelSize(R.dimen.frame_thickness));
        return spanCount;
    }

    public static DBHelper getDataBase() {
        return mDB;
    }
}
