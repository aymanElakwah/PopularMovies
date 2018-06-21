package com.akwah.popularmovies.data;

import android.provider.BaseColumns;

public final class DBContract {
    private DBContract(){}
    public static class FavouriteEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_IMAGE_URL = "image_url";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_DATE = "release_date";
        public static final String COLUMN_OVERVIEW = "overview";
    }
}
