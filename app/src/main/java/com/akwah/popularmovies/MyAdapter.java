package com.akwah.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akwah.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ayman on 16/02/2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.PosterViewHolder> {

    private int size;
    private ListItemClickListener listener;
    private ArrayList<Movie> movies;
    private static final int MAX_PAGE = 1000;
    private int page = 2;
    private static final String MOVIE_LIST_KEY = "movie_list_key";
    private static final String SORT_BY_KEY = "sort_by";
    private static final String PAGE_INDEX_KEY = "page_index_key";
    private boolean sortByPopular;
    private MainActivity mainActivity;
    private final QueryData queryData;

    public MyAdapter(ListItemClickListener listener, MainActivity mainActivity) {
        size = 0;
        this.listener = listener;
        this.mainActivity = mainActivity;
        queryData = new QueryData(mainActivity, this, mainActivity.getSupportLoaderManager());
    }

    public void appendList(ArrayList<Movie> movies) {
        int oldSize = size;
        if (this.movies == null) {
            this.movies = new ArrayList<>();
            size = 0;
            if (movies == null) notifyDataSetChanged();
        }
        if (movies != null) {
            this.movies.addAll(movies);
            size += movies.size();
            mainActivity.hideErrorMessage();
            notifyDataSetChanged();
        } else {
            noInternetConnection();
        }
    }

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poster_layout, parent, false);
        return new PosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PosterViewHolder holder, int position) {
        Log.d("POSITION", position + "");
        holder.bind(movies.get(position));
        if (position == getItemCount() - 1) {
            if (page < MAX_PAGE)
                queryData.assignLoader(NetworkUtils.getURLString(page++, sortByPopular));
        }
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public class PosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitle;
        private ImageView mPosterImage;
        private Context context;

        private PosterViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            mTitle = (TextView) itemView.findViewById(R.id.movie_title);
            mPosterImage = (ImageView) itemView.findViewById(R.id.poster_image);
            itemView.setOnClickListener(this);
        }

        private void bind(Movie movie) {
            mTitle.setText(movie.getTitle());
            // http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg
            Picasso.with(context).load(movie.getImageURL()).error(R.drawable.error).into(mPosterImage);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            listener.onClick(movies.get(position));
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIE_LIST_KEY, movies);
        outState.putBoolean(SORT_BY_KEY, sortByPopular);
        outState.putInt(PAGE_INDEX_KEY, page);
    }

    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null || !savedInstanceState.containsKey(MOVIE_LIST_KEY)) {
            sortByPopular = true;
            page = 2;
            queryData.assignLoader(NetworkUtils.getURLString(1, true));
        } else {
            sortByPopular = savedInstanceState.getBoolean(SORT_BY_KEY, true);
            page = savedInstanceState.getInt(PAGE_INDEX_KEY, 2);
            movies = savedInstanceState.getParcelableArrayList(MOVIE_LIST_KEY);
            size = movies == null ? 0 : movies.size();
            if(size == 0) {
                page = 1;
                mainActivity.showErrorMessage();
            }
            notifyDataSetChanged();
        }
    }

    public void sortByPopular() {
        if (sortByPopular)
            return;
        sortByPopular = true;
        sort();
    }

    public void sortByTopRated() {
        if (!sortByPopular)
            return;
        sortByPopular = false;
        sort();
    }

    private void sort() {
        movies.clear();
        size = 0;
        notifyDataSetChanged();
        queryData.assignLoader(NetworkUtils.getURLString(1, sortByPopular));
        page = 2;
    }

    public boolean getSortByPopular() {
        return sortByPopular;
    }

    private void noInternetConnection() {
        page--;
        mainActivity.showErrorMessage();

    }

    public void tryAgain() {
        queryData.assignLoader(NetworkUtils.getURLString(page++, sortByPopular));
    }

    public void showLoading() {
        mainActivity.showLoading();
    }

    public void hideLoading() {
        mainActivity.hideLoading();
    }

    public interface ListItemClickListener {
        public void onClick(Movie movie);
    }
}
