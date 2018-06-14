package com.akwah.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ayman on 17/02/2018.
 */

public class Movie implements Parcelable {
    private static final String BASE_URL = "http://image.tmdb.org/t/p/w185/";
    private String title;
    private String imageURL;
    private String overview;
    private float rating;
    private String releaseDate;

    public Movie(String title, String imageURL, String overview, float rating, String releaseDate) {
        this.title = title;
        this.imageURL = imageURL;
        this.overview = overview;
        this.rating = rating * 0.5f;
        this.releaseDate = releaseDate;
    }

    private Movie(Parcel parcel) {
        title = parcel.readString();
        imageURL = parcel.readString();
        overview = parcel.readString();
        rating = parcel.readFloat();
        releaseDate = parcel.readString();
    }

    public String getTitle() {
        return title;
    }

    public String getImageURL() {
        return BASE_URL + imageURL;
    }

    public String getOverview() {
        return overview;
    }

    public float getRating() {
        return rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(imageURL);
        parcel.writeString(overview);
        parcel.writeFloat(rating);
        parcel.writeString(releaseDate);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
