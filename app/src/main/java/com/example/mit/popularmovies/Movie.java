package com.example.mit.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private int movieID;
    private String thumbnail;
    private String titleMovie;
    private String releaseDate;
    private String movieOverview;
    private String rating;

    public Movie() {
    }

    public Movie(int movieID, String thumbnail, String titleMovie, String releaseDate, String movieOverview,
                 String rating) {
        this.movieID = movieID;
        this.thumbnail = thumbnail;
        this.titleMovie = titleMovie;
        this.releaseDate = releaseDate;
        this.movieOverview = movieOverview;
        this.rating = rating;
    }

    public String getMovieOverview() {
        return movieOverview;
    }

    public String getRating() {
        return rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getTitleMovie() {
        return titleMovie;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public void setMovieOverview(String movieOverview) {
        this.movieOverview = movieOverview;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setTitleMovie(String titleMovie) {
        this.titleMovie = titleMovie;
    }


    private Movie(Parcel in) {
        movieID = in.readInt();
        thumbnail = in.readString();
        titleMovie = in.readString();
        releaseDate = in.readString();
        movieOverview = in.readString();
        rating = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieID);
        dest.writeString(thumbnail);
        dest.writeString(titleMovie);
        dest.writeString(releaseDate);
        dest.writeString(movieOverview);
        dest.writeString(rating);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
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