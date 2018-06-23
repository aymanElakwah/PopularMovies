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

import com.akwah.popularmovies.data.DBHelper;
import com.akwah.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ayman on 16/02/2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.PosterViewHolder> {

    private ListItemClickListener listener;
    private ArrayList<Movie> movies;
    private static final int MAX_PAGE = 1000;
    private int page = 2;
    private static final String MOVIE_LIST_KEY = "movie_list_key";
    private static final String SORT_BY_KEY = "sort_by";
    private static final String PAGE_INDEX_KEY = "page_index_key";
    private static final String SHOWING_FAV_KEY = "showing_favourites";
    private boolean showingFavourites;
    private boolean sortByPopular;
    private MainActivity mainActivity;
    private final QueryData queryData;

    public MyAdapter(ListItemClickListener listener, MainActivity mainActivity) {
        this.listener = listener;
        this.mainActivity = mainActivity;
        queryData = new QueryData(mainActivity, this, mainActivity.getSupportLoaderManager());
    }

    public void appendList(ArrayList<Movie> movies) {
        if (this.movies == null) {
            this.movies = new ArrayList<>();
            if (movies == null) notifyDataSetChanged();
        }
        if (movies != null) {
            this.movies.addAll(movies);
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
        if (!showingFavourites && position == getItemCount() - 1) {
            if (page < MAX_PAGE)
                queryData.assignLoader(NetworkUtils.getURLString(page++, sortByPopular));
        }
    }

    @Override
    public int getItemCount() {
        if (movies != null)
            return movies.size();
        return 0;
    }

    public class PosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitle;
        private ImageView mPosterImage;
        private Context context;

        private PosterViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            mTitle = itemView.findViewById(R.id.movie_title);
            mPosterImage = itemView.findViewById(R.id.poster_image);
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
            listener.onClick(movies.get(position), position);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIE_LIST_KEY, movies);
        outState.putBoolean(SHOWING_FAV_KEY, showingFavourites);
        outState.putBoolean(SORT_BY_KEY, sortByPopular);
        outState.putInt(PAGE_INDEX_KEY, page);
    }

    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null || !savedInstanceState.containsKey(MOVIE_LIST_KEY)) {
            showingFavourites = false;
            sortByPopular = true;
            page = 2;
            queryData.assignLoader(NetworkUtils.getURLString(1, true));
        } else {
            showingFavourites = savedInstanceState.getBoolean(SHOWING_FAV_KEY, false);
            sortByPopular = savedInstanceState.getBoolean(SORT_BY_KEY, true);
            page = savedInstanceState.getInt(PAGE_INDEX_KEY, 2);
            movies = savedInstanceState.getParcelableArrayList(MOVIE_LIST_KEY);
            if (!showingFavourites && getItemCount() == 0) {
                page = 1;
                mainActivity.showErrorMessage();
            }
            notifyDataSetChanged();
        }
    }

    public void sortByPopular() {
        if (sortByPopular && !showingFavourites)
            return;
        sortByPopular = true;
        sort();
    }

    public void sortByTopRated() {
        if (!sortByPopular && !showingFavourites)
            return;
        sortByPopular = false;
        sort();
    }

    public void showFavourites() {
        if (showingFavourites)
            return;
        mainActivity.hideErrorMessage();
        showingFavourites = true;
        movies = MainActivity.getDataBase().getFavouriteMovies();
        notifyDataSetChanged();
    }

    private void sort() {
        showingFavourites = false;
        movies.clear();
        notifyDataSetChanged();
        queryData.assignLoader(NetworkUtils.getURLString(1, sortByPopular));
        page = 2;
    }

    public boolean getSortByPopular() {
        return sortByPopular;
    }

    public boolean isShowingFavourites() {
        return showingFavourites;
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

    public void removeMovie(int index) {
        if (index < 0)
            return;
        if (showingFavourites) {
            movies.remove(index);
            notifyDataSetChanged();
        }
    }

    public interface ListItemClickListener {
        void onClick(Movie movie, int index);
    }
}
