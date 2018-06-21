package com.akwah.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ayman on 17/02/2018.
 */

public class Movie implements Parcelable {
    private static final String BASE_URL = "http://image.tmdb.org/t/p/w185/";
    private final int ID;
    private final String title;
    private final String imageURL;
    private final String overview;
    private final float rating;
    private final String releaseDate;

    public Movie(int ID, String title, String imageURL, String overview, float rating, String releaseDate) {
        this.ID = ID;
        this.title = title;
        this.imageURL = imageURL;
        this.overview = overview;
        this.rating = rating * 0.5f;
        this.releaseDate = releaseDate;
    }

    private Movie(Parcel parcel) {
        ID = parcel.readInt();
        title = parcel.readString();
        imageURL = parcel.readString();
        overview = parcel.readString();
        rating = parcel.readFloat();
        releaseDate = parcel.readString();
    }

    public int getID() {
        return ID;
    }

    public String getTitle() {
        return title;
    }

    public String getImageURL() {
        return BASE_URL + imageURL;
    }

    public String getAbsoluteImageURL() {
        return imageURL;
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
        parcel.writeInt(ID);
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
