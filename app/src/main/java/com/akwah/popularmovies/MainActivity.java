package com.akwah.popularmovies;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements MyAdapter.ListItemClickListener {

    private RecyclerView mRecyclerView;
    private MyAdapter myAdapter;
    private View noInternetLayout;
    private Button tryAgain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.posters_rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, getSpanCount());
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        myAdapter = new MyAdapter(this, this);
        mRecyclerView.setAdapter(myAdapter);
        noInternetLayout = findViewById(R.id.no_internet_layout);
        tryAgain = (Button) findViewById(R.id.try_again_button);
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myAdapter.tryAgain();
            }
        });
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
        menu.findItem(myAdapter.getSortByPopular() ? R.id.sort_by_popular : R.id.sort_by_top_rated).setChecked(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_popular:
                item.setChecked(true);
                myAdapter.sortByPopular();
                return true;
            case R.id.sort_by_top_rated:
                item.setChecked(true);
                myAdapter.sortByTopRated();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showErrorMessage() {
        noInternetLayout.setVisibility(View.VISIBLE);
    }

    public void hideErrorMessage() {
        noInternetLayout.setVisibility(View.GONE);
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.INDEX_KEY, movie);
        startActivity(intent);
    }

    private int getSpanCount() {
        Resources res = getResources();
        DisplayMetrics displayMetrics = res.getDisplayMetrics();
        int spanCount = displayMetrics.widthPixels / (res.getDimensionPixelSize(R.dimen.image_width) + 2 * res.getDimensionPixelSize(R.dimen.frame_thickness));
        return spanCount;
    }
}